package com.microtomato.hirun.modules.bss.plan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.plan.entity.dto.AgentMonthAcutalDTO;
import com.microtomato.hirun.modules.bss.plan.entity.dto.AgentMonthPlanDTO;
import com.microtomato.hirun.modules.bss.plan.entity.dto.AgentMonthPlanQueryDTO;
import com.microtomato.hirun.modules.bss.plan.entity.po.PlanAgentMonth;

import java.util.List;

/**
 * (PlanAgentMonth)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-06-30 00:13:30
 */
public interface IPlanAgentMonthService extends IService<PlanAgentMonth> {
    /**
     *查询客户代表月度计划
     * @param param
     * @return
     */
    List<AgentMonthPlanDTO> queryAgentPlan(AgentMonthPlanQueryDTO param);

    /**
     * 保存月度计划
     * @param dtoList
     */
    void saveAgentPlan(List<AgentMonthPlanDTO> dtoList);

    /**
     * 根据员工查询报表计划报表数据
     * @param employee
     * @param month
     * @return
     */
    PlanAgentMonth queryAgentPlanByEmployeeId(Long employee,Integer month);

    /**
     * 根据门店查询报表计划报表数据
     * @param orgId
     * @param month
     * @return
     */
    PlanAgentMonth queryAgentPlanByShopId(Long orgId,Integer month);

    /**
     * 根据分公司查询报表计划报表数据
     * @param orgId
     * @param month
     * @return
     */
    PlanAgentMonth queryAgentPlanByCompanyId(Long orgId,Integer month);

    /**
     * 查询集团公司的计划报表数据
     * @param month
     * @return
     */
    PlanAgentMonth queryAgentPlanByBu(Integer month);

    /**
     *
     * @param employeeId
     * @param startTime
     * @param endTime
     * @return
     */
    AgentMonthAcutalDTO queryAgentAcutalByEmployeeId(String employeeId, String startTime, String endTime);
}