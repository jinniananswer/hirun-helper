package com.microtomato.hirun.modules.organization.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author liuhui
 * 员工绩效导入传输对象
 */
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePerformanceImportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "员工编码为必填字段，不能为空")
    @ExcelProperty(index = 0)
    private Long employeeId;

    @NotNull(message = "绩效年份为必填字段，不能为空")
    @ExcelProperty(index = 5)
    private String year;

    @ExcelProperty(index = 6)
    @NotNull(message = "绩效成绩为必填字段，不能为空")
    private String performance;

    @ExcelProperty(index = 7)
    private String remark;
}
