package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 财务待办数据传输对象
 * @author: jinnian
 * @create: 2020-03-01 14:45
 **/
@Data
public class FinancePendingTaskDTO {

    private String statusName;

    private String status;

    private List<FinancePendingOrderDTO> orders;
}
