package com.microtomato.hirun.modules.organization.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.consts.HrPendingConst;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeTransDetail;
import com.microtomato.hirun.modules.organization.entity.po.HrPending;
import com.microtomato.hirun.modules.organization.entity.po.OrgHrRel;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IEmployeeTransDetailService;
import com.microtomato.hirun.modules.organization.service.IHrPendingService;
import com.microtomato.hirun.modules.organization.service.IOrgHrRelService;
import com.microtomato.hirun.modules.system.service.INotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工借调生成归还待办给原部门的人资
 *
 * @author liuhui
 * @date 2019-11-28
 */
@Slf4j
@Component
public class EmployeeTransReturnTask {

    @Autowired
    private IEmployeeTransDetailService transDetailService;

    @Autowired
    private INotifyService notifyService;

    @Autowired
    private IOrgHrRelService orgHrRelService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IHrPendingService hrPendingService;


    /**
     * 每天 开始执行。
     * 查询当天应该归还的员工数据，然后给该员工原归属分公司人资生成一条待办任务
     */
    @Scheduled(cron = "0 38 23 * * ?")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void scheduled() {
        List<EmployeeTransDetail> transDetailList = transDetailService.queryVaildTransDetail("1");
        if (transDetailList.size() <= 0) {
            return;
        }

        for (EmployeeTransDetail transDetail : transDetailList) {
            int days = TimeUtils.getAbsTimeDiffDay(LocalDateTime.now(), transDetail.getEndTime());
            //确保低于10天的借调能在最后一天也能生成一条待办任务
            if (days != 10 && days != 0) {
                continue;
            } else {
                Long sourceOrgId = transDetail.getSourceOrgId();
                //查询部门与HR的关系，确认消息应该往谁发送
                OrgHrRel sourceOrgHrRel = orgHrRelService.queryValidQrgHrRel(sourceOrgId);
                if (sourceOrgHrRel == null) {
                    continue;
                }

                if (sourceOrgHrRel.getArchiveManagerEmployeeId() == null) {
                    continue;
                }
                //判断配置关系的员工是否正常
                Employee employee = employeeService.getOne(new QueryWrapper<Employee>().lambda()
                        .eq(Employee::getEmployeeId, sourceOrgHrRel.getArchiveManagerEmployeeId())
                        .eq(Employee::getStatus, EmployeeConst.STATUS_NORMAL));
                if (employee == null) {
                    continue;
                }

                //判断归还任务是否已经创建了待办
                List<HrPending> pendingList = hrPendingService.queryPendingByEmployeeIdAndType(transDetail.getEmployeeId(), HrPendingConst.PENDING_TYPE_RETURN, HrPendingConst.PENDING_STATUS_1);
                if (pendingList.size() > 0) {
                    continue;
                } else {
                    //创建一条待办
                    HrPending hrPending = new HrPending();
                    hrPending.setEmployeeId(transDetail.getEmployeeId());
                    hrPending.setPendingExecuteId(sourceOrgHrRel.getArchiveManagerEmployeeId());
                    hrPending.setPendingStatus(HrPendingConst.PENDING_STATUS_1);
                    hrPending.setPendingType(HrPendingConst.PENDING_TYPE_RETURN);
                    hrPending.setStartTime(transDetail.getEndTime());
                    hrPending.setEndTime(TimeUtils.getForeverTime());
                    hrPending.setRemark("借调归还确认");
                    hrPendingService.save(hrPending);
                    Long hrPendingId = hrPending.getId();

                    //创建一条确认信息
                    EmployeeTransDetail employeeTransDetail = new EmployeeTransDetail();
                    employeeTransDetail.setRelPendingId(hrPendingId);
                    employeeTransDetail.setEmployeeId(transDetail.getEmployeeId());
                    employeeTransDetail.setSourceJobRole(transDetail.getSourceJobRole());
                    employeeTransDetail.setSourceOrgId(transDetail.getSourceOrgId());
                    employeeTransDetail.setSourceJobRoleNature(transDetail.getSourceJobRoleNature());
                    employeeTransDetail.setSourceDiscountRate(transDetail.getSourceDiscountRate());
                    employeeTransDetail.setTransType(HrPendingConst.PENDING_TYPE_RETURN);
                    employeeTransDetail.setSourceParentEmployeeId(transDetail.getSourceParentEmployeeId());
                    employeeTransDetail.setSourceHomeRegion(transDetail.getSourceHomeRegion());
                    employeeTransDetail.setSourceHomeProv(transDetail.getSourceHomeProv());
                    employeeTransDetail.setSourceHomeCity(transDetail.getSourceHomeCity());
                    employeeTransDetail.setSourceHomeAddress(transDetail.getSourceHomeAddress());
                    employeeTransDetail.setStartTime(transDetail.getEndTime());
                    employeeTransDetail.setEndTime(TimeUtils.getForeverTime());

                    transDetailService.save(employeeTransDetail);
                    //发送消息
                    String content = employeeService.getEmployeeNameEmployeeId(sourceOrgHrRel.getArchiveManagerEmployeeId()) + " 您好。【" +
                            employeeService.getEmployeeNameEmployeeId(transDetail.getEmployeeId()) + "】借调日期于" + days + "日后到期。请到时到待办任务界面确认该员工回归原部门。";
                    notifyService.sendMessage(sourceOrgHrRel.getArchiveManagerEmployeeId(), content, 1L);
                }
            }
        }
    }

}
