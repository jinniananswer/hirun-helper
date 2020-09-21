package com.microtomato.hirun.modules.bss.config.entity.po;

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

    /** 费用类型 1-设计费 2-工程款 */
    @TableField(value = "fee_type")
    private String feeType;

    /** 期数 */
    @TableField(value = "periods")
    private Integer periods;

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

    /** 是否减掉已发 1-减掉 */
    @TableField(value = "is_minus_send")
    private String isMinusSend;

    /** 减掉哪些提成细项的已发，多个以逗号分隔 **/
    @TableField(value = "minus_item")
    private String minusItem;

    /** 多人参与是否拆分 */
    @TableField(value = "is_split")
    private String isSplit;

    /** 公司ID */
    @TableField(value = "company_id")
    private Long companyId;

    /** 店面 -1表示通配（精装房优仕馆除外） -2表示所有 0表示所有店（精装房除外） 精装房部和优仕馆配置具体的店面ID */
    @TableField(value = "shop_id")
    private Long shopId;

    /** 员工归属部门ID 最细粒度的部门匹配条件 */
    @TableField(value = "org_id")
    private Long orgId;

    /** 角色编码 */
    @TableField(value = "role_id")
    private Long roleId;

    /** 岗位 */
    @TableField(value = "job_role")
    private String jobRole;

    /** 固定员工ID */
    @TableField(value = "employee_id")
    private Long employeeId;

    /** 人员是否在订单工作人员内，0表示在订单外，其它表示在订单内 */
    @TableField(value = "in_order")
    private String inOrder;

    /** 作用方式 1-作用于角色 2-作用在固定的员工身上 */
    @TableField(value = "effect_mode")
    private String effectMode;

    /** 款项是否付齐 1-需要付齐 */
    @TableField(value = "pay_complete")
    private String payComplete;

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