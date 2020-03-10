package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 订单主流程工作人员数据传输对象
 * @author: jinnian
 * @create: 2020-02-03 23:41
 **/
@Data
public class OrderWorkerDTO {

    private Long roleId;

    private String roleName;

    private String name;

    private String status;

    private Long employeeId;
}
