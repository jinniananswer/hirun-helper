package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @program: hirun-helper
 * @description: 员工工作岗位数据传输对象
 * @author: jinnian
 * @create: 2019-10-05 01:58
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeJobRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orgId;

    private String orgName;

    private String orgPath;

    private String jobRole;

    private String jobRoleName;

    private String discountRate;

    private String jobRoleNature;

    private Long parentEmployeeId;

    private String parentEmployeeName;

    private String jobLevel;

    private String jobGrade;

}
