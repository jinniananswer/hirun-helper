package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeHistory;
import com.microtomato.hirun.modules.organization.mapper.EmployeeHistoryMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-11-05
 */
@Slf4j
@Service
public class EmployeeHistoryServiceImpl extends ServiceImpl<EmployeeHistoryMapper, EmployeeHistory> implements IEmployeeHistoryService {

    /**
     * 创建员工在鸿扬入职的历史数据
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

    /**
     * 查询员工历史信息
     * @param employeeId
     * @return
     */
    @Override
    public List<EmployeeHistory> queryHistories(Long employeeId) {
        return this.list(new QueryWrapper<EmployeeHistory>().lambda().eq(EmployeeHistory::getEmployeeId, employeeId).orderByAsc(EmployeeHistory::getEventDate));
    }

    /**
     * 处理公用数据
     * @param employeeId
     * @param eventDate
     * @param history
     */
    private void processStandardData(Long employeeId, LocalDate eventDate, EmployeeHistory history) {
        history.setEmployeeId(employeeId);
        if (eventDate == null) {
            history.setEventDate(RequestTimeHolder.getRequestTime().toLocalDate());
        } else {
            history.setEventDate(eventDate);
        }
    }
}
