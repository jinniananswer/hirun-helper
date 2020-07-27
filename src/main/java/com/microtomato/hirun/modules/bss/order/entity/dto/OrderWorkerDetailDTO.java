package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 订单工作人员及详情信息
 * @author: jinnian
 * @create: 2020-07-27 14:58
 **/
@Data
public class OrderWorkerDetailDTO {

    private Long id;

    private Long orderId;

    private Long roleId;

    private String roleName;

    private String name;

    private String status;

    private String statusName;

    private Long employeeId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String workerStatus;

    private List<OrderWorkerActionDetailDTO> actionDetails;
}
