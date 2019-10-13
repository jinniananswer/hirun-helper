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

    @TableId(value = "JOB_ROLE_ID", type = IdType.AUTO)
    private Long jobRoleId;

    @TableField("EMPLOYEE_ID")
    private Long employeeId;

    @TableField("JOB_ROLE")
    private String jobRole;

    @TableField("JOB_ROLE_NATURE")
    private String jobRoleNature;

    @TableField("ORG_ID")
    private Long orgId;

    @TableField("PARENT_EMPLOYEE_ID")
    private Long parentEmployeeId;

    @TableField("START_DATE")
    private LocalDateTime startDate;

    @TableField("END_DATE")
    private LocalDateTime endDate;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_USER_ID")
    private Long createUserId;

    @TableField("CREATE_DATE")
    private LocalDateTime createDate;

    @TableField("UPDATE_USER_ID")
    private Long updateUserId;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;


}
