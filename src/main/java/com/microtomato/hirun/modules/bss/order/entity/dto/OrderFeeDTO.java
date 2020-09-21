package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 订单费用审核数据传输对象
 * @author: sunxin
 * @create: 2020-02-05
 **/
@Data
public class OrderFeeDTO {

    private Long orderId;

    private String auditStatus;

    private String  orderStatus;
    /**
     * 工程文员id
     */
    private Long engineeringClerk;

    /**
     * 售后文员id
     */
    private Long serviceClerk;

    /**
     * 项目经理id
     */
    private Long projectManager;

    /**
     * 工程助理id
     */
    private Long engineeringAssistant;

    /**
     * 备注
     */
    private String auditRemark;


}
