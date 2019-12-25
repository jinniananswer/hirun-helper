package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

import java.io.Serializable;
/**
 * @program: hirun-helper
 * @description: 员工异动数据统计传输对象
 * @author: liuhui7
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTransitionStatDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orgId;

    private String orgName;

    private String year;

    private String month;

    private Integer employeeEntryQuantity;

    private String entryEmployeeName;

    private Integer employeeDestroyQuantity;

    private String destroyEmployeeName;

    private Integer employeeHolidayQuantity;

    private String holidayEmployeeName;

    private Integer employeeTransInQuantity;

    private String transInEmployeeName;

    private Integer employeeTransOutQuantity;

    private String transOutEmployeeName;

    private Integer employeeBorrowInQuantity;

    private String borrowInEmployeeName;

    private Integer employeeBorrowOutQuantity;

    private String borrowOutEmployeeName;

}
