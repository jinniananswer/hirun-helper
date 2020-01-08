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
 * 员工合同到期提醒
 *
 * @author liuhui
 * @date 2019-11-27
 */
@Slf4j
@Component
public class EmployeeContractRemindTask {


    @Autowired
    private INotifyService notifyService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    IOrgHrRelService orgHrRelService;

    @Autowired
    IEmployeeContractService contractService;

    /**
     * 每天凌晨 1：30 开始执行。
     * 查询合同记录提醒人资续签
     */
    @Scheduled(cron = "0 30 0 * * ?")
    public void scheduled() {

        List<EmployeeContract> contractList = contractService.list(new QueryWrapper<EmployeeContract>().lambda()
                .eq(EmployeeContract::getStatus, EmployeeConst.CONTRACT_STATUS_NORMAL)
                .apply("(now() between contract_start_time and contract_end_time and contract_type in(1,2))"));

        if (contractList.size() <= 0) {
            return;
        }

        for (EmployeeContract employeeContract : contractList) {

            Long contractId = employeeContract.getId();
            List<EmployeeContract> contracts = contractService.list(new QueryWrapper<EmployeeContract>().lambda()
                    .eq(EmployeeContract::getParentContractId, contractId)
                    .eq(EmployeeContract::getContractType, EmployeeConst.CONTRACT_TYPE_POSTPONE)
                    .eq(EmployeeContract::getStatus, EmployeeConst.CONTRACT_STATUS_NORMAL)
                    .apply("contract_end_time > now()"));

            if (contracts.size() <= 0) {
                //没有时间变更协议就看合同是否快到期，如果合同如果快到期则提醒签订合同变更协议
                long diffDay = Duration.between(LocalDateTime.now(), employeeContract.getContractEndTime()).toDays();
                if (diffDay != 40 || diffDay != 0) {
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
                            employeeService.getEmployeeNameEmployeeId(infoDTO.getEmployeeId()) + "】的合同【" + diffDay + "】天后到期，请协助签订时间变更协议手续。";
                    notifyService.sendMessage(hrEmployee.getEmployeeId(), hrContent, 1473L);
                }
            } else {
                //存在时间变更协议，判断时间变更协议是否快到期，到期则提醒签订合同
                long agreementDiffDay = Duration.between(LocalDateTime.now(), contracts.get(0).getContractEndTime()).toDays();
                if (agreementDiffDay != 30 && agreementDiffDay != 0) {
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
                            employeeService.getEmployeeNameEmployeeId(infoDTO.getEmployeeId()) + "】的时间变更协议【" + agreementDiffDay + "】天后到期，请协助签订合同手续。";
                    notifyService.sendMessage(hrEmployee.getEmployeeId(), hrContent, 1473L);
                }

            }
        }
    }

}
