package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeHistory;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.mapper.EmployeeHistoryMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeHistoryService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-11-05
 */
@Slf4j
@Service
public class EmployeeHistoryServiceImpl extends ServiceImpl<EmployeeHistoryMapper, EmployeeHistory> implements IEmployeeHistoryService {

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IOrgService orgService;

    /**
     * 创建员工在鸿扬入职的历史数据
     *
     * @param employeeId
     */
    @Override
    public void createEntry(Long employeeId, LocalDate eventDate) {
        EmployeeHistory history = new EmployeeHistory();
        this.processStandardData(employeeId, eventDate, history);
        history.setEventType(EmployeeConst.HISTORY_EVENT_ENTRY);
        history.setEventContent("加入鸿扬大家庭的第一天");

        this.save(history);
    }

    /**
     * 创建员工复职的历史数据
     *
     * @param employeeId
     * @param eventDate
     */
    @Override
    public void createRehire(Long employeeId, LocalDate eventDate) {
        EmployeeHistory history = new EmployeeHistory();
        this.processStandardData(employeeId, eventDate, history);
        history.setEventType(EmployeeConst.HISTORY_EVENT_REHIRE);
        history.setEventContent("重新回到鸿扬大家庭的第一天");

        this.save(history);
    }

    /**
     * 创建员工退休返聘的历史数据
     *
     * @param employeeId
     * @param eventDate
     */
    @Override
    public void createRehelloring(Long employeeId, LocalDate eventDate) {
        EmployeeHistory history = new EmployeeHistory();
        this.processStandardData(employeeId, eventDate, history);
        history.setEventType(EmployeeConst.HISTORY_EVENT_REHELLORING);
        history.setEventContent("回到鸿扬大家庭发挥余热的第一天");

        this.save(history);
    }

    @Override
    public void createChangeJobRole(Long employeeId, EmployeeJobRole newJobRole, LocalDate eventDate) {
        EmployeeHistory history = new EmployeeHistory();
        this.processStandardData(employeeId, eventDate, history);
        history.setEventType(EmployeeConst.HISTORY_EVENT_CHANGE_JOB_ROLE);
        Org org = this.orgService.queryByOrgId(newJobRole.getOrgId());
        history.setEventContent("岗位变动，部门："+org.getName()+"，岗位："+staticDataService.getCodeName("JOB_ROLE", newJobRole.getJobRole()) + ",职级："+staticDataService.getCodeName("JOB_GRADE", newJobRole.getJobGrade()));
        this.save(history);
    }

    @Override
    public void createChangeJobGrade(Long employeeId, String newJobGrade, LocalDate eventDate) {
        EmployeeHistory history = new EmployeeHistory();
        this.processStandardData(employeeId, eventDate, history);
        history.setEventType(EmployeeConst.HISTORY_EVENT_CHANGE_JOB_ROLE);
        history.setEventContent("职级变动，新职级："+staticDataService.getCodeName("JOB_GRADE", newJobGrade));
        this.save(history);
    }

    /**
     * 查询员工历史信息
     *
     * @param employeeId
     * @return
     */
    @Override
    public List<EmployeeHistory> queryHistories(Long employeeId) {
        return this.list(new QueryWrapper<EmployeeHistory>().lambda().eq(EmployeeHistory::getEmployeeId, employeeId)
                .eq(EmployeeHistory::getStatus,"1").orderByAsc(EmployeeHistory::getEventDate));
    }

    /**
     * 创建员工调动历史信息
     *
     * @param employeeId
     * @param eventDate
     */
    @Override
    public void createTrans(Long employeeId, LocalDate eventDate, String content, String type) {
        EmployeeHistory history = new EmployeeHistory();
        this.processStandardData(employeeId, eventDate, history);
        if (StringUtils.equals(type, "1")) {
            history.setEventType(EmployeeConst.HISTORY_EVENT_BORROW);
        } else {
            history.setEventType(EmployeeConst.HISTORY_EVENT_TRANS);
        }
        history.setEventContent(content);
        this.save(history);
    }

    /**
     * 创建离职历史信息
     *
     * @param employeeId
     * @param eventDate
     * @param destroyType
     */
    @Override
    public void createDestroy(Long employeeId, LocalDate eventDate, String destroyType) {
        EmployeeHistory history = new EmployeeHistory();
        this.processStandardData(employeeId, eventDate, history);
        history.setEventType(EmployeeConst.HISTORY_EVENT_DESTROY);
        if (StringUtils.equals(destroyType, "5")) {
            history.setEventContent("光荣退休");
        } else {
            history.setEventContent("离开鸿扬大家庭");
        }
        this.save(history);
    }

    /**
     * 处理公用数据
     *
     * @param employeeId
     * @param eventDate
     * @param history
     */
    private void processStandardData(Long employeeId, LocalDate eventDate, EmployeeHistory history) {
        history.setEmployeeId(employeeId);
        //新增状态1、正常2、删除
        history.setStatus("1");
        if (eventDate == null) {
            history.setEventDate(RequestTimeHolder.getRequestTime().toLocalDate());
        } else {
            history.setEventDate(eventDate);
        }
    }
}
