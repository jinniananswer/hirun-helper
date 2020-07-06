package com.microtomato.hirun.modules.bss.salary.entity.domain;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 提成计算因子对象
 * @author: jinnian
 * @create: 2020-05-17 18:03
 **/
@Data
public class RoyaltyComputeFact {

    /**
     * 预算总金额
     */
    private Long budget;

    /**
     * 费用计算
     */
    private FeeFact feeFact;
}
