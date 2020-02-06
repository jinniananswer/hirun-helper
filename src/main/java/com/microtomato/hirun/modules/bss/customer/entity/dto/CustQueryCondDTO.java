package com.microtomato.hirun.modules.bss.customer.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 客户信息查询条件传输对象
 * @author: liuhui
 * @create: 2020-02-05 23:54
 **/
@Data
public class CustQueryCondDTO {

    private String custName;

    private Long designEmployeeId;

    private Long counselorEmployeeId;

    private Long reportEmployeeId;

    private String informationSource;

    private String customerStatus;

    private String customerType;

    private String timeType;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String houseMode;

}
