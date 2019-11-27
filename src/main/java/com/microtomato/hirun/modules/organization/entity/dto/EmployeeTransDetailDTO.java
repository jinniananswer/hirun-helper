package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 待办详细数据传输对象
 * @author: liuhui7
 **/
@Data
public class EmployeeTransDetailDTO {

    private Long Id;

    private Long employeeId;

    private Long relPendingId;

    private String employeeName;

    private Long sourceOrgId;

    private String sourceOrgPath;

    private Long orgId;

    private String orgPath;

    private String sourceJobRole;

    private String sourceJobRoleName;

    private String jobRole;

    private String jobRoleName;

    private String sourceJobRoleNature;

    private String sourceJobRoleNatureName;

    private String jobRoleNature;

    private String jobRoleNatureName;

    private String sourceDiscountRate;

    private String discountRate;

    private Long sourceParentEmployeeId;

    private String sourceParentEmployeeName;

    private Long parentEmployeeId;

    private String parentEmployeeName;

    private Integer homeProv;

    private Integer homeCity;

    private Integer homeRegion;

    private String homeAddress;

    private String homeArea;

    private Integer sourceHomeProv;

    private Integer sourceHomeCity;

    private Integer sourceHomeRegion;

    private String sourceHomeAddress;

    private String sourceHomeArea;

    private String pendingType;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String transType;

}
