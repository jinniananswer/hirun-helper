package com.microtomato.hirun.modules.bss.plan.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.bss.plan.entity.po.PlanAgentMonth;
import com.microtomato.hirun.modules.bss.plan.service.IPlanAgentMonthService;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.SimpleEmployeeDTO;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 每月1号生成客户代表计划
 *
 * @author liuhui
 * @date 2020-07-09
 */
@Slf4j
@Component
public class CreateAgentMonthPlanTask {


    @Autowired
    private IPlanAgentMonthService planAgentMonthService;

    @Autowired
    private IEmployeeService employeeService;

    /**
     * 每月1号执行。
     * 查询合同记录提醒人资续签
     * 每个月1号 的 1:00 开始执行
     *
     */
    @Scheduled(cron = "0 0 1 1 * ?")
    public void scheduled() {
        List<SimpleEmployeeDTO> employees = employeeService.queryEmployeeByRoleAndOrg(7L, "15,16,17,18,19");
        if (ArrayUtils.isEmpty(employees)) {
            return;
        }

        Calendar date = Calendar.getInstance();
        String currentYear = String.valueOf(date.get(Calendar.YEAR));
        String currentMonth = String.valueOf(date.get(Calendar.MONTH) + 1);

        if(currentMonth.length()<2){
            currentMonth="0"+currentMonth;
        }
        Integer planMonth = Integer.valueOf(currentYear + currentMonth);

        List<PlanAgentMonth> addList = new ArrayList<>();


        for (SimpleEmployeeDTO employee : employees) {
            PlanAgentMonth existPlanAgentMonth = planAgentMonthService.getOne(new QueryWrapper<PlanAgentMonth>().lambda()
                    .eq(PlanAgentMonth::getMonth, planMonth)
                    .eq(PlanAgentMonth::getEmployeeId, employee.getEmployeeId()));

            if (existPlanAgentMonth != null) {
                continue;
            }
            PlanAgentMonth planAgentMonth=new PlanAgentMonth();
            OrgDO orgDO= SpringContextUtils.getBean(OrgDO.class,employee.getOrgId());

            planAgentMonth.setEmployeeId(employee.getEmployeeId());
            planAgentMonth.setOrgId(employee.getOrgId());
            planAgentMonth.setMonth(planMonth);
            planAgentMonth.setPlanBindAgentCount(6);
            planAgentMonth.setPlanConsultCount(6);
            planAgentMonth.setPlanStyleCount(6);
            planAgentMonth.setPlanFuncaCount(6);
            planAgentMonth.setPlanFuncbCount(6);
            planAgentMonth.setPlanFunccCount(6);
            planAgentMonth.setPlanBindDesignCount(3);
            planAgentMonth.setPlanMeasureCount(3);
            planAgentMonth.setPlanCitycabinCount(6);
            planAgentMonth.setShopId(orgDO.getBelongShop().getOrgId());
            planAgentMonth.setCompanyId(orgDO.getBelongCompany().getOrgId());
            addList.add(planAgentMonth);
        }

        if(ArrayUtils.isNotEmpty(addList)){
            planAgentMonthService.saveBatch(addList);
        }
    }

}
