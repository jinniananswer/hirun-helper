package com.microtomato.hirun.modules.organization.entity.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.*;

import java.io.Serializable;

/**
 * @author liuhui
 * 员工绩效信息传输对象
 */
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePerformanceInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
    @ExcelProperty(value = "岗位性质",index = 4)
    private String jobRoleNatureName;

    @ColumnWidth(20)
    @ExcelProperty(value = "绩效录入年份(必填。填写格式如：2019)" ,index = 5)
    private String year;

    @ExcelIgnore
    private String performance;

    @ColumnWidth(20)
    @ExcelProperty(value = "绩效成绩(必填。请在单元格中填入对应的值 纯业务类 1=优秀(A1)，2=优秀(A2)，3=优秀(A3)),4=合格,5=待培养,6=待提升,7=待优化" +
            "业务支持类员工 8=核心人员,9=骨干人员,10=一般人员,7=待优化,5=待培养", index = 6)
    private String performanceName;

    @ExcelIgnore
    private Long id;

    @ExcelIgnore
    private String jobRole;

    @ExcelIgnore
    private Long orgId;

    @ColumnWidth(20)
    @ExcelProperty(value = "备注" ,index = 7)
    private String remark;

    @ExcelIgnore
    private String jobRoleNature;



}
