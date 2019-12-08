package com.microtomato.hirun.modules.organization.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author jinnian
 * @since 2019-10-09
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_employee_job_role")
public class EmployeeJobRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "job_role_id", type = IdType.AUTO)
    private Long jobRoleId;

    @TableField("employee_id")
    private Long employeeId;

    @TableField("job_role")
    private String jobRole;

    @TableField("job_role_nature")
    private String jobRoleNature;

    @TableField("discount_rate")
    private String discountRate;

    @TableField("is_main")
    private String isMain;

    @TableField("org_id")
    private Long orgId;

    @TableField("parent_employee_id")
    private Long parentEmployeeId;

    @TableField("start_date")
    private LocalDateTime startDate;

    @TableField("end_date")
    private LocalDateTime endDate;

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
