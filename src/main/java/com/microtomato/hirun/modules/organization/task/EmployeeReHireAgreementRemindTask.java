package com.microtomato.hirun.modules.organization.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * 返聘协议到期提醒
 *
 * @author liuhui
 * @date 2019-11-27
 */
@Slf4j
@Component
public class EmployeeReHireAgreementRemindTask {


    @Autowired
    private INotifyService notifyService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    IOrgHrRelService orgHrRelService;

    @Autowired
    IEmployeeContractService contractService;

    /**
     * 每天凌晨 00：30 开始执行。
     * 查询返聘协议到期前一个月提醒人资续签
     */
    @Scheduled(cron = "0 30 0 * * ?")
    public void scheduled() {

        List<EmployeeContract> contractList = contractService.list(new QueryWrapper<EmployeeContract>().lambda()
                .eq(EmployeeContract::getStatus, EmployeeConst.CONTRACT_STATUS_NORMAL)
                .eq(EmployeeContract::getContractType, "11")
                .apply("(now() between contract_start_time and contract_end_time)"));

        if (contractList.size() <= 0) {
            return;
        }

        for (EmployeeContract employeeContract : contractList) {
            long diffDay = Duration.between(LocalDateTime.now(),employeeContract.getContractEndTime()).toDays();

            if (diffDay != 30 || diffDay != 0) {
                continue;
            } else {
                Long employeeId = employeeContract.getEmployeeId();
                EmployeeInfoDTO infoDTO = employeeService.queryEmployeeInfoByEmployeeId(employeeId);
                if (infoDTO == null) {
                    return;
                }
                Employee hrEmployee = orgHrRelService.queryValidRemindEmployeeId("relation_manager", infoDTO.getOrgId());

                if (hrEmployee == null) {
                    continue;
                }

                //发送消息给对应的人资
                String hrContent = employeeService.getEmployeeNameEmployeeId(hrEmployee.getEmployeeId()) + " 您好！【" +
                        employeeService.getEmployeeNameEmployeeId(infoDTO.getEmployeeId()) + "】的返聘协议【" + diffDay + "】天后到期，请协助办理手续。";
                notifyService.sendMessage(hrEmployee.getEmployeeId(), hrContent, 1473L);

            }
        }
    }


}
