package com.microtomato.hirun.modules.organization.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.organization.entity.po.*;
import com.microtomato.hirun.modules.organization.service.*;
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
 * 员工休假到期提醒
 *
 * @author liuhui
 * @date 2019-11-27
 */
@Slf4j
@Component
public class EmployeeHoliadyRemindTask {


    @Autowired
    private INotifyService notifyService;

    @Autowired
    private IEmployeeHolidayService holidayService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    IOrgHrRelService orgHrRelService;

    /**
     * 每天凌晨 5:10 开始执行。
     * 查询休假到期数据，进行消息提醒
     */
    @Scheduled(cron = "0 47 12 * * ?")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void scheduled() {

        List<EmployeeHoliday> holidayList = holidayService.list(new QueryWrapper<EmployeeHoliday>().lambda()
                .gt(EmployeeHoliday::getEndTime, LocalDateTime.now())
                .lt(EmployeeHoliday::getStartTime, LocalDateTime.now()));

        if (holidayList.size() <= 0) {
            return;
        }

        for (EmployeeHoliday employeeHoliday : holidayList) {
            int days = TimeUtils.getAbsTimeDiffDay(LocalDateTime.now(), employeeHoliday.getEndTime());

            if (days > 10) {
                continue;
            } else {
                Employee hrEmployee = orgHrRelService.queryValidRemindEmployeeId("archive_manager", employeeHoliday.getEmployeeId());
                if (hrEmployee == null) {
                    continue;
                }
                //发送消息给对应的人资
                String hrContent = employeeService.getEmployeeNameEmployeeId(hrEmployee.getEmployeeId()) + " 你好。【" +
                        employeeService.getEmployeeNameEmployeeId(hrEmployee.getEmployeeId()) + "】假期于" + days + "天后到期。";
                notifyService.sendMessage(hrEmployee.getEmployeeId(), hrContent,1473L);
                //发送消息给休假员工本人
                String employeeContent = employeeService.getEmployeeNameEmployeeId(hrEmployee.getEmployeeId()) + " 你好。你的" +
                        "假期于【" + days + "】天后到期。调整心态，回归工作！";
                notifyService.sendMessage(hrEmployee.getEmployeeId(), employeeContent,1473L);

            }


        }
    }
}
