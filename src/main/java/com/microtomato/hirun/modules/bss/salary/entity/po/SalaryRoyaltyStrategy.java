package com.microtomato.hirun.modules.bss.salary.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 员工提成策略配置(SalaryRoyaltyStrategy)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 18:05:14
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("salary_royalty_strategy")
public class SalaryRoyaltyStrategy extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 策略描述 */
    @TableField(value = "description")
    private String description;

    /** 订单状态，也代表计算提成的阶段 */
    @TableField(value = "order_status")
    private String orderStatus;

    /** 流程动作 如量房，画平面，见static表 royalties_action */
    @TableField(value = "action")
    private String action;

    /** 提成类型：大类 1-设计提成 2-经营提成 见sys_static_data表 royalties_type */
    @TableField(value = "type")
    private String type;

    /** 提成细项 见sys_static_data表 royalties_item */
    @TableField(value = "item")
    private String item;

    /** 提成模式 1-按百分点 2-固定奖励 */
    @TableField(value = "mode")
    private String mode;

    /** 基准费用点，是设计费还是合同金额，见static表datum_fee_type */
    @TableField(value = "datum_fee_type")
    private String datumFeeType;

    /** 提成值 */
    @TableField(value = "value")
    private String value;

    /** 计算公式 */
    @TableField(value = "formula")
    private String formula;

    /** 是否减掉已发 */
    @TableField(value = "is_minus_send")
    private String isMinusSend;

    /** 多人参与是否拆分 */
    @TableField(value = "is_split")
    private String isSplit;

    /** 角色编码 */
    @TableField(value = "role_id")
    private Long roleId;

    /** 岗位 */
    @TableField(value = "job_role")
    private String jobRole;

    /** 固定员工ID */
    @TableField(value = "employee_id")
    private Long employeeId;

    /** 作用方式 1-作用于角色 2-作用在固定的员工身上 */
    @TableField(value = "effect_mode")
    private String effectMode;

    /** 匹配条件，基于spring spel的动态表达式 */
    @TableField(value = "match_condition")
    private String matchCondition;

    /** 状态 U表示有效 */
    @TableField(value = "status")
    private String status;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}