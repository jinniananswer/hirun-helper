package com.microtomato.hirun.modules.bss.plan.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: hirun-helper
 * @description: 客户代表月度计划数据传输对象
 * @create: 2020-05-02 01:10
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentMonthPlanDTO {

    private Long employeeId;

    private Long id;

    private Integer month;

    private String name;

    private String orgPath;

    private Long orgId;

    private String orgName;

    private String jobRoleName;

    private String jobRole;

    private Integer planConsultCount;

    private Integer planBindAgentCount;

    private Integer planStyleCount;

    private Integer planFuncaCount;

    private Integer planFuncbCount;

    private Integer planFunccCount;

    private Integer planCitycabinCount;

    private Integer planMeasureCount;

    private Integer planBindDesignCount;


}
