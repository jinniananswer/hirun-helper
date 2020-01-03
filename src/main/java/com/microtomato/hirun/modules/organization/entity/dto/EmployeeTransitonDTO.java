package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 员工异动数据传输对象
 * @author: liuhui7
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTransitonDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long employeeId;

    private Long orgId;

    private String orgName;

    private LocalDate happenDate;

    private String orgNature;

    private String orgType;

    private String orgCity;

    private String jobGrade;

    private String month;

    private String year;

    private String jobRole;

    private String jobRoleNature;
}
