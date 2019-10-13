package com.microtomato.hirun.modules.organization.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuhui
 * @since 2019-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_employee")
public class Employee extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "EMPLOYEE_ID", type = IdType.AUTO)
    private Integer employeeId;

    @TableField("USER_ID")
    private Integer userId;

    @TableField("NAME")
    private String name;

    @TableField("SEX")
    private String sex;

    @TableField("IDENTITY_NO")
    private String identityNo;

    @TableField("MOBILE_NO")
    private String mobileNo;

    @TableField("HOME_ADDRESS")
    private String homeAddress;

    @TableField("NATIVE_PROV")
    private Integer nativeProv;

    @TableField("NATIVE_CITY")
    private Integer nativeCity;

    @TableField("NATIVE_REGION")
    private Integer nativeRegion;

    @TableField("IN_DATE")
    private LocalDateTime inDate;

    @TableField("REGULAR_DATE")
    private LocalDateTime regularDate;

    @TableField("DESTROY_DATE")
    private LocalDateTime destroyDate;

    @TableField("JOB_DATE")
    private LocalDate jobDate;

    /**
     * 1-全职　2-兼职  3-外聘
     */
    @TableField("WORK_NATURE")
    private String workNature;

    @TableField("WORKPLACE")
    private String workplace;

    @TableField("EDUCATION_LEVEL")
    private String educationLevel;

    @TableField("MAJOR")
    private String major;

    @TableField("SCHOOL")
    private String school;

    @TableField("CERTIFICATE_NO")
    private String certificateNo;

    @TableField("BEFORE_HIRUN_YEAR")
    private String beforeHirunYear;

    @TableField("STATUS")
    private String status;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_USER_ID")
    private Integer createUserId;

    @TableField("CREATE_DATE")
    private LocalDateTime createDate;

    @TableField("UPDATE_USER_ID")
    private Integer updateUserId;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;


}
