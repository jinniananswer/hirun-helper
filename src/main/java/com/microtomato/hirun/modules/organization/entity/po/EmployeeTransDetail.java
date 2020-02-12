package com.microtomato.hirun.modules.organization.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author liuhui
 * @since 2019-10-29
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_employee_trans_detail")
public class EmployeeTransDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("rel_pending_id")
    private Long relPendingId;

    @TableField("employee_id")
    private Long employeeId;

    @TableField("trans_type")
    private String transType;

    @TableField("source_org_id")
    private Long sourceOrgId;

    @TableField("source_job_role")
    private String sourceJobRole;

    @TableField("source_job_grade")
    private String sourceJobGrade;

    @TableField("source_job_role_nature")
    private String sourceJobRoleNature;

    @TableField("source_discount_rate")
    private String sourceDiscountRate;

    @TableField("source_parent_employee_id")
    private Long sourceParentEmployeeId;

    @TableField("source_home_prov")
    private Integer sourceHomeProv;

    @TableField("source_home_city")
    private Integer sourceHomeCity;

    @TableField("source_home_region")
    private Integer sourceHomeRegion;

    @TableField("source_home_address")
    private String sourceHomeAddress;

    @TableField("org_id")
    private Long orgId;

    @TableField("job_role")
    private String jobRole;

    @TableField("job_role_nature")
    private String jobRoleNature;

    @TableField("discount_rate")
    private String discountRate;

    @TableField("parent_employee_id")
    private Long parentEmployeeId;

    @TableField("home_prov")
    private Integer homeProv;

    @TableField("home_city")
    private Integer homeCity;

    @TableField("home_region")
    private Integer homeRegion;

    @TableField("home_address")
    private String homeAddress;

    @TableField("start_time")
    private LocalDateTime startTime;

    @TableField("end_time")
    private LocalDateTime endTime;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("job_grade")
    private String jobGrade;
}
