package com.microtomato.hirun.modules.organization.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 员工固定工资表(EmployeeSalary)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-02 00:25:10
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("employee_salary")
public class EmployeeSalary extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField(value = "employee_id")
    private Long employeeId;

    /** 工资年月 */
    @TableField(value = "salary_month")
    private Integer salaryMonth;

    /** 基本工资，单位分 */
    @TableField(value = "basic")
    private Long basic;

    /** 职称工资，单位分 */
    @TableField(value = "rank")
    private Long rank;

    /** 绩效工资，单位分 */
    @TableField(value = "performance")
    private Long performance;

    /** 职务津贴，单位分 */
    @TableField(value = "duty")
    private Long duty;

    /** 加班补贴，单位分 */
    @TableField(value = "overtime")
    private Long overtime;

    /** 浮动奖励，单位分 */
    @TableField(value = "float_award")
    private Long floatAward;

    /** 其他补贴，单位分 */
    @TableField(value = "other")
    private Long other;

    /** 补发金额，单位分 */
    @TableField(value = "back_pay")
    private Long backPay;

    /** 提成费，单位分 */
    @TableField(value = "royalties")
    private Long royalties;

    /** 医疗保险，单位分 */
    @TableField(value = "medical")
    private Long medical;

    /** 养老保险，单位分 */
    @TableField(value = "overage")
    private Long overage;

    /** 失业保险，单位分 */
    @TableField(value = "unemployment")
    private Long unemployment;

    /** 大病医疗，单位分 */
    @TableField(value = "serious_ill")
    private Long seriousIll;

    /** 税金，单位分 */
    @TableField(value = "tax")
    private Long tax;

    /** 备注 */
    @TableField(value = "remark")
    private String remark;

    /** 审核状态， 0-初始保存 1-待审核 2-审核通过 3-审核不通过 */
    @TableField(value = "audit_status")
    private String auditStatus;

    /** 审核备注 */
    @TableField(value = "audit_remark")
    private String auditRemark;

    /** 开始时间 */
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    /** 结束时间 */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /** 税金，单位分 */
    @TableField(value = "create_employee_id")
    private Long createEmployeeId;

    /** 审核员工 */
    @TableField(value = "audit_employee_id")
    private Long auditEmployeeId;

    /** 是否被修改 1-被修改 其它值或null表示未被修改 */
    @TableField(value = "is_modified")
    private String isModified;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}