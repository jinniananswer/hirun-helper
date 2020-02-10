package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 订单费用表
 * </p>
 *
 * @author sunxin
 * @since 2020-02-05
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderFee extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_id")
    private Long orderId;

    /**
     * 费用项编码
     */
    @TableField("fee_item_id")
    private Long feeItemId;

    /**
     * 上级费用项编码
     */
    @TableField("parent_fee_item_id")
    private Long parentFeeItemId;

    /**
     * 期数
     */
    @TableField("period")
    private Integer period;

    /**
     * 收费方向 1-收费 2-退费
     */
    @TableField("direction")
    private String direction;

    /**
     * 销售价
     */
    @TableField("sale_price")
    private Integer salePrice;

    /**
     * 退费类型，见静态参数BACK_TYPE
     */
    @TableField("back_type")
    private String backType;

    /**
     * 返还费用，折扣，减免等
     */
    @TableField("back_fee")
    private Integer backFee;

    /**
     * 折扣比例
     */
    @TableField("discount_rate")
    private Integer discountRate;

    /**
     * 应收费用
     */
    @TableField("fee")
    private Integer fee;

    /**
     * 实收费用
     */
    @TableField("act_fee")
    private Integer actFee;

    /**
     * 本次处理费用的员工ID
     */
    @TableField("fee_employee_id")
    private Long feeEmployeeId;

    /**
     * 合同ID
     */
    @TableField("contract_id")
    private Long contractId;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
