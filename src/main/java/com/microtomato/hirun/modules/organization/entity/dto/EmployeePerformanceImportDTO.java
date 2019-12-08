package com.microtomato.hirun.modules.organization.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author liuhui
 * 员工绩效导入传输对象
 */
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePerformanceImportDTO {

    @NotNull(message = "员工编码为必填字段，不能为空")
    @ExcelProperty(index = 0)
    private Long employeeId;

    @NotNull(message = "绩效年份为必填字段，不能为空")
    @ExcelProperty(index = 4)
    private String year;

    @ExcelProperty(index = 5)
    @NotNull(message = "绩效成绩为必填字段，不能为空")
    @Pattern(regexp = "[1-5]{1}", message = "绩效的类型值为1-5中的一个类型")
    private String performance;

    @ExcelProperty(index = 6)
    private String remark;
}
