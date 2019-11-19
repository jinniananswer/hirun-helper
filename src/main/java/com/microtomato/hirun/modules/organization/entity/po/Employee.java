package com.microtomato.hirun.modules.organization.entity.po;

import com.baomidou.mybatisplus.annotation.*;
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

    @TableId(value = "employee_id", type = IdType.AUTO)
    private Long employeeId;

    @TableField("user_id")
    private Long userId;

    @TableField("name")
    private String name;

    @TableField("sex")
    private String sex;

    @TableField("identity_no")
    private String identityNo;

    @TableField("birthday_type")
    private String birthdayType;

    @TableField("birthday")
    private LocalDate birthday;

    @TableField("mobile_no")
    private String mobileNo;

    @TableField("type")
    private String type;

    @TableField("home_prov")
    private Integer homeProv;

    @TableField("home_city")
    private Integer homeCity;

    @TableField("home_region")
    private Integer homeRegion;

    @TableField("home_address")
    private String homeAddress;

    @TableField("native_prov")
    private Integer nativeProv;

    @TableField("native_city")
    private Integer nativeCity;

    @TableField("native_region")
    private Integer nativeRegion;

    @TableField("native_address")
    private String nativeAddress;

    @TableField("in_date")
    private LocalDateTime inDate;

    @TableField("regular_date")
    private LocalDateTime regularDate;

    @TableField("destroy_date")
    private LocalDateTime destroyDate;

    @TableField("destroy_way")
    private String destroyWay;

    @TableField("destroy_reason")
    private String destroyReason;

    @TableField("destroy_times")
    private Integer destroyTimes;

    @TableField("job_date")
    private LocalDate jobDate;

    /**
     * 1-全职　2-兼职  3-外聘
     */
    @TableField("work_nature")
    private String workNature;

    @TableField("workplace")
    private String workplace;

    @TableField("education_level")
    private String educationLevel;

    @TableField("first_education_level")
    private String firstEducationLevel;

    @TableField("major")
    private String major;

    @TableField("school")
    private String school;

    @TableField("school_type")
    private String schoolType;

    @TableField("tech_title")
    private String techTitle;

    @TableField("certificate_no")
    private String certificateNo;

    @TableField("is_social_security")
    private Integer isSocialSecurity;

    @TableField("social_security_date")
    private LocalDate socialSecurityDate;

    @TableField("social_security_place")
    private String socialSecurityPlace;

    @TableField("social_security_status")
    private String socialSecurityStatus;

    @TableField("before_hirun_year")
    private String beforeHirunYear;

    @TableField("status")
    private String status;

    @TableField("remark")
    private String remark;

    @TableField(value="create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value="create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value="update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value="update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

}
