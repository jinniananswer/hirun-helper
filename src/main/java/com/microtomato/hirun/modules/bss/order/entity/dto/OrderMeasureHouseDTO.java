package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;


/**
 * @author ：mmzs
 * @date ：Created in 2020/2/4 20:58
 * @description：数据传输对象
 * @modified By：
 * @version: 1$
 */
@Data
public class OrderMeasureHouseDTO  {

    private Long orderId;

    private String measureArea;

    private LocalDate measureTime;

    private String customerComments;

    private List<Long> assistantDesigner;
    
    private Long designer;

    private List<OrderWorkerActionDTO> orderWorkActions;

    private String employeeName;
}
