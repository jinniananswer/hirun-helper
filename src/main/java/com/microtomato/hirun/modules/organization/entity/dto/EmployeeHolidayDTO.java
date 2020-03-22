package com.microtomato.hirun.modules.organization.entity.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.microtomato.hirun.framework.harbour.excel.convert.LocalDateTimeConvert;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 员工休假传输对象
 * @author: liuhui7
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeHolidayDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long employeeId;

    private LocalDateTime inDate;

    private Long orgId;

    private String jobRole;

    private String jobRoleNature;

    private String jobGrade;

    private String orgNature;

    private String employeeInMonth;
}
