package com.microtomato.hirun.modules.bss.service.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 投诉传输对象
 * @author: liuhui
 * @create: 2020-07-21 14:24
 **/
@Data
public class ComplainOrderInfoDTO {


    List<ComplainOrderDTO> insertRecords;

    List<ComplainOrderDTO> removeRecords;

    List<ComplainOrderDTO> updateRecords;

    Long customerId;

    Long orderId;

    String complainNo;
}
