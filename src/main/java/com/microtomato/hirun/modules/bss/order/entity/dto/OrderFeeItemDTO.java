package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 订单费用项明细数据对象
 * @author: jinnian
 * @create: 2020-06-14 22:24
 **/
@Data
public class OrderFeeItemDTO {
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
