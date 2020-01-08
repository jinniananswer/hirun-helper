package com.microtomato.hirun.modules.organization.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeContract;
import com.microtomato.hirun.modules.organization.service.IEmployeeContractService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgHrRelService;
import com.microtomato.hirun.modules.system.service.INotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工转正以及新员工未签合同提醒
 *
 * @author liuhui
 * @date 2019-11-27
 */
@Slf4j
@Component
public class EmployeeRegularRemindTask {


    @Autowired
    private INotifyService notifyService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    IOrgHrRelService orgHrRelService;

    @Autowired
    IEmployeeContractService contractService;

    /**
     * 查询员工数据，到期前7天进行员工转正以及新员工未签合同提醒
     */
    @Scheduled(cron = "0 30 0 * * ?")
    public void scheduled() {

        List<Employee> employeeList = employeeService.list(new QueryWrapper<Employee>().lambda()
                .eq(Employee::getStatus, EmployeeConst.STATUS_NORMAL));
        //转正提醒
        this.employeeRegularRemind(employeeList);
        //新员工签订合同提醒
        this.employeeSignFirstContractRemind(employeeList);
    }

    private void employeeRegularRemind(List<Employee> employeeList) {

        if (employeeList.size() <= 0) {
            return;
        }

        for (Employee employee : employeeList) {
            if (employee.getRegularDate() == null) {
                continue;
            }
            long diffDay = Duration.between(LocalDateTime.now(),employee.getRegularDate()).toDays();

            if (diffDay != 7 || diffDay != 0) {
                continue;
            } else {

                Long employeeId = employee.getEmployeeId();
                EmployeeInfoDTO infoDTO = employeeService.queryEmployeeInfoByEmployeeId(employeeId);

                if (infoDTO == null) {
                    continue;
                }
                Employee hrEmployee = orgHrRelService.queryValidRemindEmployeeId("archive_manager", infoDTO.getOrgId());

                if (hrEmployee == null) {
                    continue;
                }

                //发送消息给对应的人资
                String hrContent = employeeService.getEmployeeNameEmployeeId(hrEmployee.getEmployeeId()) + " 您好！【" +
                        employeeService.getEmployeeNameEmployeeId(employeeId) + "】" + diffDay + "天后转正，请协助办理手续。";
                notifyService.sendMessage(hrEmployee.getEmployeeId(), hrContent, 1473L);
            }
        }
    }

    private void employeeSignFirstContractRemind(List<Employee> employeeList) {
        if (employeeList.size() <= 0) {
            return;
        }
        for (Employee employee : employeeList) {
            if (employee.getInDate() == null) {
                continue;
            }
            //判断新员工入职时间与系统上线时间，在系统上线之前的数据不再提醒，因为后续需要判断是否已签订第一份合同，而老数据可能没有第一份合同
            if(TimeUtils.compareTwoTime(employee.getInDate(),TimeUtils.stringToLocalDateTime("2019-12-01 00:00:00","yyyy-MM-dd HH:mm:ss"))<0){
                continue;
            }
            //判断是否已经签订了第一份合同
            List<EmployeeContract> list=contractService.list(new QueryWrapper<EmployeeContract>().lambda()
                    .eq(EmployeeContract::getEmployeeId,employee.getEmployeeId())
                    .eq(EmployeeContract::getContractType,"1")
                    .eq(EmployeeContract::getStatus,"1"));

            if(list.size()>0){
                continue;
            }

            EmployeeInfoDTO infoDTO = employeeService.queryEmployeeInfoByEmployeeId(employee.getEmployeeId());

            if (infoDTO == null) {
                continue;
            }
            Employee hrEmployee = orgHrRelService.queryValidRemindEmployeeId("archive_manager", infoDTO.getOrgId());

            if (hrEmployee == null) {
                continue;
            }

            //发送消息给对应的人资
            String hrContent = employeeService.getEmployeeNameEmployeeId(hrEmployee.getEmployeeId()) + " 您好！新入职员工【" +
                    employeeService.getEmployeeNameEmployeeId(employee.getEmployeeId()) + "】还未签订劳动合同，请尽快与签订合同。";
            notifyService.sendMessage(hrEmployee.getEmployeeId(), hrContent, 1473L);

        }
    }
}
