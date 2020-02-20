package com.microtomato.hirun.modules.organization.entity.dto;


import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @program: hirun-helper
 * @description: 员工查询条件数据传输对象
 * @author: liuhui7
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeQueryConditionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String mobileNo;

    private String sex;
    /**
     * 员工状态
     */
    private String employeeStatus;
    /**
     * 是否黑名单
     */
    private String isBlackList;

    /**
     * 员工类型
     */
    private String type;
    /**
     * 部门集合
     */
    private String orgId;

    private String otherStatus;

    private String jobRole;

    private String jobRoleNature;

    private String discountRate;

    private String ageStart;

    private String ageEnd;

    private String jobYearStart;

    private String jobYearEnd;

    private LocalDate inDateEnd;

    private LocalDate inDateStart;

    private LocalDate destroyDateStart;

    private LocalDate destroyDateEnd;

    private String companyAgeStart;

    private String companyAgeEnd;

    private String employeeIds;

    private String orgLine;

    private String isRegular;

    private LocalDate regularDateStart;

    private LocalDate regularDateEnd;

}
