package com.microtomato.hirun.modules.bss.plan.controller;
import com.microtomato.hirun.modules.bss.plan.entity.dto.AgentMonthPlanDTO;
import com.microtomato.hirun.modules.bss.plan.entity.dto.AgentMonthPlanQueryDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryMonthlyDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.microtomato.hirun.modules.bss.plan.service.IPlanAgentMonthService;
import com.microtomato.hirun.framework.annotation.RestResult;
import java.util.ArrayList;
import java.util.List;

/**
 * (PlanAgentMonth)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-06-30 00:13:36
 */
@RestController
@RequestMapping("/api/bss.plan/plan-agent-month")
public class PlanAgentMonthController {

    /**
     * 服务对象
     */
    @Autowired
    private IPlanAgentMonthService planAgentMonthService;


    /**
     * 界面查询客户代表月度报表数据，如果该月还未录入，也需要能查询出员工数据来，以供数据录入
     * @param param
     * @return
     */
    @GetMapping("/queryAgentPlan")
    @RestResult
    public List<AgentMonthPlanDTO> queryAgentPlan(AgentMonthPlanQueryDTO param) {
        if (StringUtils.isNotBlank(param.getOrgId())) {
            String[] orgIdArray = param.getOrgId().split(",");
            List<Long> orgIds = new ArrayList<>();
            for (String s : orgIdArray) {
                orgIds.add(Long.parseLong(s));
            }
            param.setOrgIds(orgIds);
        }
        return planAgentMonthService.queryAgentPlan(param);
    }

    @PostMapping("/saveAgentPlan")
    @RestResult
    public void saveAgentPlan(@RequestBody List<AgentMonthPlanDTO> dtoList) {
        this.planAgentMonthService.saveAgentPlan(dtoList);
    }

}