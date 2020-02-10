package com.microtomato.hirun.modules.bss.customer.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 客户咨询提交
 * @author: liuhui
 * @create: 2020-02-09 23:54
 **/
@Data
public class CustConsultDTO {

    private Long orderId;

    private Long custServiceEmployeeId;

    private Long designCupboardEmployeeId;

    private Long mainMaterialKeeperEmployeeId;

    private Long cupboardKeeperEmployeeId;

    private String consultRemark;


}
