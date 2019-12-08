package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工档案数据传输对象（含中文翻译）
 * @author: jinnian
 * @create: 2019-11-17 02:58
 **/
@Data
@Builder
public class EmployeeArchiveDTO {

    private Long employeeId;

    private String name;

    private String sexName;

    private String identityNo;

    private LocalDate birthday;

    private String age;

    private String mobileNo;

    private String typeName;

    private String homeArea;

    private String homeAddress;

    private String nativeArea;

    private String nativeAddress;

    private LocalDateTime inDate;

    private LocalDateTime regularDate;

    private String companyAge;

    private String educationLevelName;

    private String firstEducationLevelName;

    private String major;

    private String school;

    private String schoolTypeName;

    private String techTitle;

    private String certificateNo;

    private String isSocialSecurityName;

    private LocalDate socialSecurityDate;

    private LocalDate socialSecurityEnd;

    private String socialSecurityPlace;

    private String socialSecurityStatusName;

    private String statusName;

    private String jobRoleName;

    private String jobRoleNatureName;

    private String discountRate;

    private String orgPath;

    private String parentEmployeeName;

    private List<EmployeeWorkExperienceDTO> workExperiences;

    private List<EmployeeChildrenDTO> children;

    private List<EmployeeHistoryDTO> histories;

}
