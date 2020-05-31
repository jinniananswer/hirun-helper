package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

import java.io.Serializable;
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
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String createType;

    private Long employeeId;

    private String name;

    private String mobileNo;

    private String identityNo;

    private String sex;

    private String type;

    private String birthdayType;

    private LocalDate birthday;

    private Integer age;

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

    private Integer isSocialSecurity;

    private LocalDate socialSecurityDate;

    private String socialSecurityPlace;

    private String socialSecurityStatus;

    private String firstEducationLevel;

    private String educationLevel;

    private String schoolType;

    private String school;

    private String major;

    private String certificateNo;

    private String techTitle;

    private LocalDate jobDate;

    private Integer jobYear;

    private Integer companyAge;

    private String status;

    private EmployeeJobRoleDTO employeeJobRole;

    private EmployeeContactManDTO contactMan;

    private List<EmployeeWorkExperienceDTO> employeeWorkExperiences;

    private List<EmployeeChildrenDTO> children;
}
