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

    @TableField("mobile_no")
    private String mobileNo;

    @TableField("home_address")
    private String homeAddress;

    @TableField("native_prov")
    private Integer nativeProv;

    @TableField("native_city")
    private Integer nativeCity;

    @TableField("native_region")
    private Integer nativeRegion;

    @TableField("in_date")
    private LocalDateTime inDate;

    @TableField("regular_date")
    private LocalDateTime regularDate;

    @TableField("destroy_date")
    private LocalDateTime destroyDate;

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

    @TableField("major")
    private String major;

    @TableField("school")
    private String school;

    @TableField("certificate_no")
    private String certificateNo;

    @TableField("before_hirun_year")
    private String beforeHirunYear;

    @TableField("status")
    private String status;

    @TableField("remark")
    private String remark;

    @TableField("create_user_id")
    private Long createUserId;

    @TableField("create_date")
    private LocalDateTime createDate;

    @TableField("update_user_id")
    private Long updateUserId;

    @TableField("update_time")
    private LocalDateTime updateTime;


}
