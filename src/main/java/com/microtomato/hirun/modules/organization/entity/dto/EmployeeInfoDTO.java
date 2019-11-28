package com.microtomato.hirun.modules.organization.entity.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.microtomato.hirun.framework.harbour.excel.convert.LocalDateTimeConvert;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 员工数据传输对象
 * @author: liuhui7
 **/
@Data
public class EmployeeInfoDTO {

    @ExcelIgnore
    private Long employeeId;

    @ColumnWidth(20)
    @ExcelProperty("员工姓名")
    private String name;

    @ColumnWidth(20)
    @ExcelProperty("员工类型")
    private String typeName;

    @ColumnWidth(20)
    @ExcelProperty("员工电话")
    private String mobileNo;

    @ColumnWidth(20)
    @ExcelProperty("员工证件")
    private String identityNo;

    @ColumnWidth(20)
    @ExcelProperty("员工性别")
    private String sex;

    @ColumnWidth(20)
    @ExcelProperty("员工状态")
    private String employeeStatusName;

    @ExcelIgnore
    private String employeeStatus;

    @ExcelIgnore
    private Integer nativeProv;

    @ExcelIgnore
    private Integer nativeCity;

    @ExcelIgnore
    private Integer nativeRegion;

    @ExcelIgnore
    private Integer homeProv;

    @ExcelIgnore
    private Integer homeCity;

    @ExcelIgnore
    private Integer homeRegion;

    @ColumnWidth(20)
    @ExcelProperty(value = "入职时间", converter = LocalDateTimeConvert.class)
    private LocalDateTime inDate;

    @ColumnWidth(20)
    @ExcelProperty("户籍地址")
    private String nativeArea;

    @ColumnWidth(20)
    @ExcelProperty("详细户籍")
    private String nativeAddress;

    @ColumnWidth(20)
    @ExcelProperty("现住址")
    private String homeArea;

    @ColumnWidth(20)
    @ExcelProperty("详细住址")
    private String homeAddress;

    @ExcelIgnore
    private Long orgId;

    @ExcelIgnore
    private String orgName;

    @ColumnWidth(20)
    @ExcelProperty("部门")
    private String orgPath;

    @ExcelIgnore
    private String jobRole;

    @ColumnWidth(20)
    @ExcelProperty("岗位")
    private String jobRoleName;

    @ExcelIgnore
    private Long parentEmployeeId;

    @ColumnWidth(20)
    @ExcelProperty("上级员工")
    private String parentEmployeeName;

    @ExcelIgnore
    private String jobLevel;

    @ExcelIgnore
    private String jobRoleNature;

    @ColumnWidth(20)
    @ExcelProperty("岗位性质")
    private String jobRoleNatureName;

    @ColumnWidth(20)
    @ExcelProperty("业务折算比例")
    private String discountRate;

    @ExcelIgnore
    private String isMain;

    @ExcelIgnore
    private String isBlackList;

    @ExcelIgnore
    private String type;

    @ColumnWidth(20)
    @ExcelProperty("工作年限")
    private String companyAge;

    @ExcelIgnore
    private LocalDate jobDate;

    @ExcelIgnore
    private String educationLevel;

    @ColumnWidth(20)
    @ExcelProperty("最高学历")
    private String educationLevelName;

    @ExcelIgnore
    private String firstEducationLevel;

    @ColumnWidth(20)
    @ExcelProperty("第一学历")
    private String firstEducationLevelName;

    @ExcelIgnore
    private String schoolType;

    @ColumnWidth(20)
    @ExcelProperty("学校类型")
    private String schoolTypeName;

    @ColumnWidth(20)
    @ExcelProperty("技术职称")
    private String techTitle;


}
