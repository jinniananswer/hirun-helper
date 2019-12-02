package com.microtomato.hirun.modules.organization.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeHoliday;
import com.microtomato.hirun.modules.organization.service.IEmployeeHolidayService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgHrRelService;
import com.microtomato.hirun.modules.system.service.INotifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工退休到期提醒
 *
 * @author liuhui
 * @date 2019-11-27
 */
@Slf4j
@Component
public class EmployeeRetireRemindTask {


    @Autowired
    private INotifyService notifyService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    IOrgHrRelService orgHrRelService;

    /**
     * 每天凌晨 5:10 开始执行。
     * 查询休假到期数据，进行消息提醒
     */
    @Scheduled(cron = "0 47 12 * * ?")
    public void scheduled() {

        List<Employee> employeeList = employeeService.list(new QueryWrapper<Employee>().lambda()
                .gt(Employee::getStatus, EmployeeConst.STATUS_NORMAL));

        if (employeeList.size() <= 0) {
            return;
        }

        for (Employee employee : employeeList) {
            if (employee.getBirthday() == null) {
                continue;
            }
            LocalDate today = LocalDate.now();
            int age = TimeUtils.getAbsDateDiffYear(employee.getBirthday(), today);
            if (age == 55 && StringUtils.equals(employee.getSex(), EmployeeConst.EMPLOYEE_SEX_WOMAN)) {
                Employee hrEmployee = orgHrRelService.queryValidRemindEmployeeId("archive_manager", employee.getEmployeeId());
                if (hrEmployee == null) {
                    continue;
                }
                //发送消息给对应的人资
                String hrContent = employeeService.getEmployeeNameEmployeeId(hrEmployee.getEmployeeId()) + " 您好！。【" +
                        employeeService.getEmployeeNameEmployeeId(hrEmployee.getEmployeeId()) + "】已到退休年龄，请协助办理手续。";
                notifyService.sendMessage(hrEmployee.getEmployeeId(), hrContent, 1473L);

            } else if(age == 60 && StringUtils.equals(employee.getSex(), EmployeeConst.EMPLOYEE_SEX_MAN)){
                Employee hrEmployee = orgHrRelService.queryValidRemindEmployeeId("archive_manager", employee.getEmployeeId());
                if (hrEmployee == null) {
                    continue;
                }
                //发送消息给对应的人资
                String hrContent = employeeService.getEmployeeNameEmployeeId(hrEmployee.getEmployeeId()) + " 您好！。【" +
                        employeeService.getEmployeeNameEmployeeId(hrEmployee.getEmployeeId()) + "】已到退休年龄，请协助办理手续。";
                notifyService.sendMessage(hrEmployee.getEmployeeId(), hrContent, 1473L);
            }else{
                continue;
            }


        }
    }


}
