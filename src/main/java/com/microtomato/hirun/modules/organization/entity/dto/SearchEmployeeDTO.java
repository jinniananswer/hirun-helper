package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 搜索员工数据传输类
 * @author: jinnian
 * @create: 2019-10-21 23:11
 **/
@Data
public class SearchEmployeeDTO {

    private String employeeId;

    private String name;

    private String mobileNo;

    private String identityNo;

    private String sex;

    private String birthdayType;

    private String birthday;

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

    private LocalDateTime regularDate;

    private String workNature;

    private String discountRate;

    private String firstEducationLevel;

    private String educationLevel;

    private String schoolType;

    private String school;

    private String major;

    private String certificateNo;

    private String techTitle;

    private LocalDate jobDate;

    private String orgId;

    private String orgName;

    private String orgPath;

    private String jobRole;

    private String jobRoleName;

    private String jobNature;

    private String parentEmployeeId;

    private String parentEmployeeName;

    private String jobLevel;
}
