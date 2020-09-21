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
public class AgentMonthAcutalDTO {


    private Integer acutalConsultCount;

    private Integer acutalBindAgentCount;

    private Integer acutalStyleCount;

    private Integer acutalFuncaCount;

    private Integer acutalFuncbCount;

    private Integer acutalFunccCount;

    private Integer acutalCitycabinCount;

    private Integer acutalMeasureCount;

    private Integer acutalBindDesignCount;


}
