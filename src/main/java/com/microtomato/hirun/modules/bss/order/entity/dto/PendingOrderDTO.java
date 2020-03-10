package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 待办任务中的客户信息数据传输对象
 * @author: jinnian
 * @create: 2020-02-11 11:45
 **/
@Data
public class PendingOrderDTO {

    private Long custId;

    private String custName;

    private String mobileNo;

    private Long orderId;

    private String decorateAddress;

    private String status;
}
