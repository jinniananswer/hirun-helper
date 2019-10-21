package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工数据传输对象
 * @author: jinnian
 * @create: 2019-10-05 01:44
 **/
@Data
public class EmployeeDTO {

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

    private EmployeeJobRoleDTO employeeJobRole;

    private List<EmployeeWorkExperienceDTO> employeeWorkExperiences;
}