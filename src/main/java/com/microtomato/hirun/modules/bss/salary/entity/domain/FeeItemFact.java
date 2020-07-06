package com.microtomato.hirun.modules.bss.salary.entity.domain;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 费用项数据类
 * @author: jinnian
 * @create: 2020-05-17 22:50
 **/
@Data
public class FeeItemFact {

    /**
     * 类型 设计费 工程款 主材 橱柜等
     */
    private String type;

    /**
     * 期数 首期 二期 尾款
     */
    private Integer periods;

    /**
     * 费用项名称
     */
    private Long feeItemId;

    /**
     * 应收金额
     */
    private Long fee;

    /**
     * 实收金额
     */
    private Long actFee;
}
