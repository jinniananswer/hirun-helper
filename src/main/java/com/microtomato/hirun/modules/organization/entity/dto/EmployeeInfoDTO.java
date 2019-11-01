package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 员工数据传输对象
 * @author: liuhui7
 **/
@Data
public class EmployeeInfoDTO {

    private Long employeeId;

    private String name;

    private String mobileNo;

    private String identityNo;

    private String sex;

    private String natives;

    private Integer nativeProv;

    private Integer nativeCity;

    private Integer nativeRegion;

    private String nativeAddress;

    private String home;

    private Integer homeProv;

    private Integer homeCity;

    private Integer homeRegion;

    private String homeAddress;

    private LocalDateTime inDate;

    private String employeeStatus;

    private Long orgId;

    private String orgName;

    private String orgPath;

    private String jobRole;

    private String jobRoleName;

    private String parentEmployeeId;

    private String parentEmployeeName;

    private String jobLevel;

    private String jobNature;

    private String discountRate;

    private String isMain;

    private String isBlackList;
}
