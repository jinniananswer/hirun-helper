package com.microtomato.hirun.modules.organization.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
    private String createTime;

    @TableField(value="create_user_id", fill = FieldFill.INSERT)
    private String createUserId;

    @TableField(value="update_time", fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

    @TableField(value="update_user_id", fill = FieldFill.INSERT_UPDATE)
    private String updateUserId;
    
}
