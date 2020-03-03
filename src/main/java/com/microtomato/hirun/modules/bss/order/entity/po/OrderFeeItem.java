package com.microtomato.hirun.modules.bss.order.entity.po;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 订单费用明细表(OrderFeeItem)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-03 21:57:46
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_fee_item")
public class OrderFeeItem extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField(value = "order_id")
    private Long orderId;

    /** 费用项编码，见参数sys_fee_item_cfg */
    @TableField(value = "fee_item_id")
    private Long feeItemId;

    /** 上级费用项编码，见参数sys_fee_item_cfg */
    @TableField(value = "parent_fee_item_id")
    private Long parentFeeItemId;

    /** 期数 */
    @TableField(value = "periods")
    private Integer periods;

    /** 应收金额,是累计值 */
    @TableField(value = "fee")
    private Long fee;

    /** 实收金额 */
    @TableField(value = "act_fee")
    private Long actFee;

    /** 本次处理费用的员工ID */
    @TableField(value = "fee_employee_id")
    private Long feeEmployeeId;

    /** 处理费用的部门 */
    @TableField(value = "org_id")
    private Long orgId;

    /** 备注 */
    @TableField(value = "remark")
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