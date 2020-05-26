package com.microtomato.hirun.modules.bss.salary.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 员工工资提成明细(SalaryRoyaltyDetail)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 17:57:21
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("salary_royalty_detail")
public class SalaryRoyaltyDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField(value = "employee_id")
    private Long employeeId;

    /** 部门ID */
    @TableField(value = "org_id")
    private Long orgId;

    /** 岗位 */
    @TableField(value = "job_role")
    private String jobRole;

    /** 角色ID */
    @TableField(value = "role_id")
    private Long roleId;

    /** 发放月份 */
    @TableField(value = "salary_month")
    private Integer salaryMonth;

    /** 订单ID */
    @TableField(value = "order_id")
    private Long orderId;

    /** 订单状态，也代表计算提成的阶段 */
    @TableField(value = "order_status")
    private String orderStatus;

    /** 提成类型：大类 1-设计提成 2-经营提成 见sys_static_data表 royalties_type */
    @TableField(value = "type")
    private String type;

    /** 提成细项 见sys_static_data表 royalties_item */
    @TableField(value = "item")
    private String item;

    /** 提成模式 1-按百分点 2-固定奖励 */
    @TableField(value = "mode")
    private String mode;

    /** 提成值 */
    @TableField(value = "value")
    private String value;

    /** 基准费用点，是设计费还是合同金额，见static表datum_fee_type */
    @TableField(value = "datum_fee_type")
    private String datumFeeType;

    /** 用于计算的基准费用 */
    @TableField(value = "datum_fee")
    private Long datumFee;

    /** 按公式计算出来的提成费用 */
    @TableField(value = "total_royalty")
    private Long totalRoyalty;

    /** 匹配的配置策略ID */
    @TableField(value = "strategy_id")
    private Long strategyId;

    /** 已经发放的提成金额 */
    @TableField(value = "already_fetch")
    private Long alreadyFetch;

    /** 本月可提取金额 */
    @TableField(value = "this_month_fetch")
    private Long thisMonthFetch;

    /** 备注 */
    @TableField(value = "remark")
    private String remark;

    /** 审核状态， 0-待审核 1-审核通过 2-审核不通过 2-已发放 */
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

    /** 创建员工 */
    @TableField(value = "create_employee_id")
    private Long createEmployeeId;

    /** 审核员工 */
    @TableField(value = "audit_employee_id")
    private Long auditEmployeeId;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}