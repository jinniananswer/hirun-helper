package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 订单参与员工与员工所做动作关联产品的集合
 * @author: jinnian
 * @create: 2020-06-07 15:27
 **/
@Data
public class OrderWorkerActionDTO {

    private Long orderId;

    private Long roleId;

    private Long employeeId;

    /**
     * 参与的订单状态
     */
    private String orderStatus;

    /**
     * 动作发生时的当前归属部门
     */
    private Long orgId;

    /**
     * 动作发生时的当前岗位
     */
    private String jobRole;

    /**
     * 动作发生时的当前岗位
     */
    private String jobGrade;

    private String action;
}
