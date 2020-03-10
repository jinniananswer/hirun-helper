package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 工作台待办任务数据传输对象
 * @author: jinnian
 * @create: 2020-02-11 11:43
 **/
@Data
public class PendingTaskDTO {

    private String statusName;

    private Long statusId;

    private String status;

    private String pageUrl;

    private List<PendingOrderDTO> orders;
}
