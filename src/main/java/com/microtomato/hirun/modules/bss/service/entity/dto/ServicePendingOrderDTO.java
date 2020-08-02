package com.microtomato.hirun.modules.bss.service.entity.dto;


import lombok.Data;

import java.time.LocalDateTime;


/**
 * @program: hirun-helper
 * @description: 服务中心待办传输对象
 * @author: liuhui
 * @create: 2020-07-12 14:24
 **/
@Data
public class ServicePendingOrderDTO {


    private Long custId;

    private String code;

    private String type;

    private String custName;

    private String mobileNo;

    private Long orderId;

    private String decorateAddress;

    private String status;

    private String statusName;

    private LocalDateTime acceptTime;
}
