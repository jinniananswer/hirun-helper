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
import org.springframework.beans.BeanUtils;
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
        String previousMonth = String.valueOf(date.get(Calendar.MONTH));

        if (currentMonth.length() < 2) {
            currentMonth = "0" + currentMonth;
        }

        if (previousMonth.length() < 2) {
            previousMonth = "0" + previousMonth;
        }

        Integer planMonth = Integer.valueOf(currentYear + currentMonth);
        Integer previousMonthPlan = Integer.valueOf(previousMonth + previousMonth);

        List<PlanAgentMonth> addList = new ArrayList<>();


        for (SimpleEmployeeDTO employee : employees) {
            PlanAgentMonth existPlanAgentMonth = planAgentMonthService.getOne(new QueryWrapper<PlanAgentMonth>().lambda()
                    .eq(PlanAgentMonth::getMonth, planMonth)
                    .eq(PlanAgentMonth::getEmployeeId, employee.getEmployeeId()));

            if (existPlanAgentMonth != null) {
                continue;
            }

            //查看上个月是否有设定的计划，将上个月的计划继承下来
            PlanAgentMonth existPreviousPlanAgentMonth = planAgentMonthService.getOne(new QueryWrapper<PlanAgentMonth>().lambda()
                    .eq(PlanAgentMonth::getMonth, previousMonthPlan)
                    .eq(PlanAgentMonth::getEmployeeId, employee.getEmployeeId()));


            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, employee.getOrgId());
            PlanAgentMonth planAgentMonth = new PlanAgentMonth();
            planAgentMonth.setEmployeeId(employee.getEmployeeId());
            planAgentMonth.setOrgId(employee.getOrgId());
            planAgentMonth.setMonth(planMonth);
            planAgentMonth.setShopId(orgDO.getBelongShop().getOrgId());
            planAgentMonth.setCompanyId(orgDO.getBelongCompany().getOrgId());

            if (existPreviousPlanAgentMonth != null) {
                planAgentMonth.setPlanBindAgentCount(existPreviousPlanAgentMonth.getPlanBindAgentCount());
                planAgentMonth.setPlanConsultCount(existPreviousPlanAgentMonth.getPlanConsultCount());
                planAgentMonth.setPlanStyleCount(existPreviousPlanAgentMonth.getPlanStyleCount());
                planAgentMonth.setPlanFuncaCount(existPreviousPlanAgentMonth.getPlanFuncaCount());
                planAgentMonth.setPlanFuncbCount(existPreviousPlanAgentMonth.getPlanFuncbCount());
                planAgentMonth.setPlanFunccCount(existPreviousPlanAgentMonth.getPlanFunccCount());
                planAgentMonth.setPlanBindDesignCount(existPreviousPlanAgentMonth.getPlanBindDesignCount());
                planAgentMonth.setPlanMeasureCount(existPreviousPlanAgentMonth.getPlanMeasureCount());
                planAgentMonth.setPlanCitycabinCount(existPreviousPlanAgentMonth.getPlanCitycabinCount());
                addList.add(planAgentMonth);
            } else {
                planAgentMonth.setPlanBindAgentCount(6);
                planAgentMonth.setPlanConsultCount(6);
                planAgentMonth.setPlanStyleCount(6);
                planAgentMonth.setPlanFuncaCount(6);
                planAgentMonth.setPlanFuncbCount(6);
                planAgentMonth.setPlanFunccCount(6);
                planAgentMonth.setPlanBindDesignCount(3);
                planAgentMonth.setPlanMeasureCount(3);
                planAgentMonth.setPlanCitycabinCount(6);
                addList.add(planAgentMonth);
            }

        }

        if (ArrayUtils.isNotEmpty(addList)) {
            planAgentMonthService.saveBatch(addList);
        }
    }

}
