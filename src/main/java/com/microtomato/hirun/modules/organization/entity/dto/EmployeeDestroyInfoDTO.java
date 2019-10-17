package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 员工销户信息传输对象
 * @author:
 **/
@Data
public class EmployeeDestroyInfoDTO {

    private Long employeeId;

    private String name;

    private String mobileNo;

    private String identityNo;

    private LocalDateTime destroyDate;

    private String destroyWay;

    private Integer destroyTimes;

    private String destroyReason;

    private String isBlackList;

    private LocalDate socialStopDate;
}
