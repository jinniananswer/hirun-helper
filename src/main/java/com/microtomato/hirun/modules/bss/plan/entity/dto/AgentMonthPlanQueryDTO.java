package com.microtomato.hirun.modules.bss.plan.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 客户代表月度计划查询数据传输对象
 * @author: liuhui
 * @create: 2020-05-02 14:38
 **/
@Data
public class AgentMonthPlanQueryDTO {

    private String name;

    private List<Long> orgIds;

    private String orgId;

    private Integer month;
}
