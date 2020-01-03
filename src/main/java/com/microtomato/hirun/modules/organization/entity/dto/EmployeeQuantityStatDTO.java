package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工在职数据传输对象
 * @author: liuhui7
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeQuantityStatDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orgId;

    private String orgName;

    private String year;

    private Float employeeNum;

    private Float lessMonthNum;

    private Float moreMonthNum;

    private String orgType;

    private String city;

    private String jobGrade;

    private String month;

    private Float janurayCount;

    private Float februaryCount;

    private Float marchCount;

    private Float aprilCount;

    private Float mayCount;

    private Float juneCount;

    private Float julyCount;

    private Float augustCount;

    private Float septemberCount;

    private Float octoberCount;

    private Float novemberCount;

    private Float decemberCount;

    private Long parentOrgId;

    private String inMonths;

    private String orgNature;

    private String jobRole;

    private String jobRoleNature;
}
