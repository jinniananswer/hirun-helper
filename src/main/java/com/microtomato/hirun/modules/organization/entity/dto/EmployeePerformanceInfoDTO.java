package com.microtomato.hirun.modules.organization.entity.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author liuhui
 * 员工绩效信息传输对象
 */

@Data
public class EmployeePerformanceInfoDTO{

    @ColumnWidth(20)
    @ExcelProperty(value = "员工编号",index = 0)
    private Long employeeId;

    @ColumnWidth(20)
    @ExcelProperty(value = "姓名",index = 1)
    private String employeeName;


    @ColumnWidth(20)
    @ExcelProperty(value = "岗位",index = 2)
    private String jobRoleName;


    @ColumnWidth(20)
    @ExcelProperty(value = "部门名称",index = 3)
    private String orgPath;

    @ColumnWidth(20)
    @ExcelProperty(value = "绩效录入年份(必填。填写格式如：2019)" ,index = 4)
    private String year;

    @ColumnWidth(20)
    @ExcelProperty(value = "绩效成绩(必填。请在单元格中填入对应的值 1=下 2=中下 3=中 4=中上 5=上)" ,index = 5)
    private String performance;

    @ExcelIgnore
    private Long id;

    @ExcelIgnore
    private String jobRole;

    @ExcelIgnore
    private Long orgId;

    @ColumnWidth(20)
    @ExcelProperty(value = "备注" ,index = 6)
    private String remark;

}
