package com.microtomato.hirun.modules.bss.customer.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 客户回访信息数据传输对象
 * @author: liuhui
 * @create: 2020-02-01 23:54
 **/
@Data
public class CustVisitInfoDTO {

    private String visitObject;

    private Long visitEmployeeId;

    private String visitEmployeeName;

    private String visitType;

    private String visitTypeName;

    private String visitWay;

    private String visitContent;

    private LocalDateTime visitTime;

}
