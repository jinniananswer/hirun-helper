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

    @ColumnWidth(20)
    @ExcelProperty("用户编码")
    private Long employeeId;

    @ColumnWidth(20)
    @ExcelProperty("用户姓名")
    private String name;

    @ColumnWidth(20)
    @ExcelProperty("用户电话")
    private String mobileNo;

    @ColumnWidth(20)
    @ExcelProperty("用户证件")
    private String identityNo;

    @ColumnWidth(20)
    @ExcelProperty("用户性别")
    private String sex;

    private String natives;

    private Integer nativeProv;

    private Integer nativeCity;

    private Integer nativeRegion;

    private String nativeAddress;

    private String home;

    private Integer homeProv;

    private Integer homeCity;

    private Integer homeRegion;

    private String homeAddress;

    @ColumnWidth(20)
    @ExcelProperty(value = "入职时间", converter = LocalDateTimeConvert.class)
    private LocalDateTime inDate;

    @ColumnWidth(20)
    @ExcelProperty("员工状态")
    private String employeeStatus;

    @ExcelIgnore
    private Long orgId;

    @ExcelIgnore
    private String orgName;

    @ColumnWidth(20)
    @ExcelProperty("部门")
    private String orgPath;

    private String jobRole;

    @ColumnWidth(20)
    @ExcelProperty("岗位")
    private String jobRoleName;

    @ExcelIgnore
    private String parentEmployeeId;

    private String parentEmployeeName;

    @ExcelIgnore
    private String jobLevel;

    @ColumnWidth(20)
    @ExcelProperty("岗位性质")
    private String jobNature;

    @ColumnWidth(20)
    @ExcelProperty("业务折算比例")
    private String discountRate;

    @ExcelIgnore
    private String isMain;

    private String isBlackList;

    @ColumnWidth(20)
    @ExcelProperty("员工类型")
    private String type;

    private String typeName;

    private String companyAge;

    @ExcelIgnore
    private LocalDate jobDate;

}
