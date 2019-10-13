package com.microtomato.hirun.modules.organization.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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

    @TableField("create_user_id")
    private Long createUserId;

    @TableField("create_date")
    private LocalDateTime createDate;

    @TableField("update_user_id")
    private Long updateUserId;

    @TableField("update_time")
    private LocalDateTime updateTime;


}
