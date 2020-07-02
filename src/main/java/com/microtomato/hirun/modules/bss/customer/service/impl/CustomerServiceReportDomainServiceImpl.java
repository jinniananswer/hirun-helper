package com.microtomato.hirun.modules.bss.customer.service.impl;


import com.microtomato.hirun.modules.bss.customer.entity.dto.*;
import com.microtomato.hirun.modules.bss.customer.service.*;
import com.microtomato.hirun.modules.bss.plan.entity.po.PlanAgentMonth;
import com.microtomato.hirun.modules.bss.plan.service.IPlanAgentMonthService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 客户信息 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-04-20
 */
@Slf4j
@Service
public class CustomerServiceReportDomainServiceImpl implements ICustomerServiceReportDomainService {

    @Autowired
    private IOrgService orgService;

    @Autowired
    private IPlanAgentMonthService planAgentMonthService;

    private static String[] action = {"上门咨询", "客户代表绑定", "风格蓝图二", "功能蓝图二A",
            "功能蓝图二B", "功能蓝图二C", "体验城市木屋", "量房", "设计师绑定"};

    private static String[] legend = {"计划", "实际"};

    private static Integer[] blankData = {0, 0, 0, 0, 0, 0, 0, 0, 0};


    @Override
    public Map<String, List> queryReport(ReportQueryCondDTO param) {
        /*Map<String, List> listMap = new HashMap<>();
        listMap.put("action", Arrays.asList(action));

        Long orgIds = param.getOrgId();

        List<String> shopName = new ArrayList<>();
        if (StringUtils.isBlank(orgIds)) {
            List<Org> orgList = orgService.listByType("4");
            for (Org org : orgList) {
                shopName.add(org.getName());
            }
        }

        String[] orgId = orgIds.split(",");
        for (String id : orgId) {
            Org orgEntity = orgService.getById(id);
            shopName.add(orgEntity.getName());
        }

        listMap.put("shopName", shopName);*/
        return null;
    }

    @Override
    public Map<String, List> queryAgentPlanAcutalReport(ReportQueryCondDTO param) {
        String queryType = param.getQueryType();
        Long employeeId = param.getEmployeeId();
        Map<String, List> listMap = new HashMap<>();
        //拼装横轴数据和legend数据
        listMap.put("action", Arrays.asList(action));
        listMap.put("legendData", Arrays.asList(legend));
        //按员工查询
        if (StringUtils.equals("1", queryType)) {
            listMap.put("planData", this.getEmployeePlanData(employeeId, param.getMonth()));
        }
        //按门店查
        if(StringUtils.equals("2",queryType)){
            listMap.put("planData",this.getShopPlanData(param.getOrgId(),param.getMonth()));
        }
        //按分公司查询
        if(StringUtils.equals("3",queryType)){
            listMap.put("planData",this.getCompanyPlanData(param.getCompanyId(),param.getMonth()));
        }

        return listMap;
    }

    /**
     * 拼装个人的计划数据
     *
     * @param employeeId
     * @param month
     * @return
     */
    private List<Integer> getEmployeePlanData(Long employeeId, Integer month) {
        PlanAgentMonth planAgentMonth = planAgentMonthService.queryAgentPlanByEmployeeId(employeeId, month);
        return this.buildPlanData(planAgentMonth);
    }


    /**
     * 拼装门店的计划数据
     *
     * @param orgId
     * @param month
     * @return
     */
    private List<Integer> getShopPlanData(Long orgId, Integer month) {

        PlanAgentMonth planAgentMonth = planAgentMonthService.queryAgentPlanByShopId(orgId, month);
        return this.buildPlanData(planAgentMonth);
    }

    /**
     * 拼装门店的计划数据
     *
     * @param orgId
     * @param month
     * @return
     */
    private List<Integer> getCompanyPlanData(Long orgId, Integer month) {
        PlanAgentMonth planAgentMonth = planAgentMonthService.queryAgentPlanByCompanyId(orgId, month);
        return this.buildPlanData(planAgentMonth);
    }


    /**
     * 构造计划数据
     * @param planAgentMonth
     * @return
     */
    private List<Integer> buildPlanData(PlanAgentMonth planAgentMonth) {
        List<Integer> planData = new ArrayList<>();

        if (planAgentMonth == null) {
            return Arrays.asList(blankData);
        }
        planData.add(planAgentMonth.getPlanConsultCount() == null ? 0 : planAgentMonth.getPlanConsultCount());
        planData.add(planAgentMonth.getPlanBindAgentCount() == null ? 0 : planAgentMonth.getPlanBindAgentCount());
        planData.add(planAgentMonth.getPlanStyleCount() == null ? 0 : planAgentMonth.getPlanStyleCount());
        planData.add(planAgentMonth.getPlanFuncaCount() == null ? 0 : planAgentMonth.getPlanFuncaCount());
        planData.add(planAgentMonth.getPlanFuncbCount() == null ? 0 : planAgentMonth.getPlanFuncbCount());
        planData.add(planAgentMonth.getPlanFunccCount() == null ? 0 : planAgentMonth.getPlanFunccCount());
        planData.add(planAgentMonth.getPlanCitycabinCount() == null ? 0 : planAgentMonth.getPlanCitycabinCount());
        planData.add(planAgentMonth.getPlanMeasureCount() == null ? 0 : planAgentMonth.getPlanMeasureCount());
        planData.add(planAgentMonth.getPlanBindDesignCount() == null ? 0 : planAgentMonth.getPlanBindDesignCount());

        return planData;
    }
}
