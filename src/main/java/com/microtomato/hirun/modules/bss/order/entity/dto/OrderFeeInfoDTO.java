package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 订单展示组件费用信息展示DTO
 * @author: jinnian
 * @create: 2020-03-07 13:29
 **/
@Data
public class OrderFeeInfoDTO {

    /**
     * 费用类型名称 1-设计费 2-工程款
     */
    private String typeName;

    /**
     * 费用类型
     */
    private String type;

    /**
     * 期数
     */
    private Integer periods;

    /**
     * 期数名称
     */
    private String periodName;

    /**
     * 总费用
     */
    private Double totalMoney;

    /**
     * 标识
     */
    private Long rowKey;

    /**
     * 树型结构，费用子项信息
     */
    private List<OrderFeeInfoDTO> children;
}
