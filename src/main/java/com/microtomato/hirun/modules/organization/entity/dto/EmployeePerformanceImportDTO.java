package com.microtomato.hirun.modules.organization.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author liuhui
 * 员工绩效导入传输对象
 */

@Data
public class EmployeePerformanceImportDTO {

    @ExcelProperty(index = 0)
    private Long employeeId;

    @ExcelProperty(index = 4)
    private String year;

    @ExcelProperty(index = 5)
    private String performance;

    private String resultInfo;

    @ExcelProperty(index = 6)
    private String remark;
}
