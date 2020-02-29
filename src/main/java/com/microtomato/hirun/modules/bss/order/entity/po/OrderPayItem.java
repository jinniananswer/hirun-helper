package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 订单支付项明细表(OrderPayItem)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-02-29 11:02:02
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_pay_item")
public class OrderPayItem extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField(value = "order_id")
    private Long orderId;

    /** 付款编码 */
    @TableField(value = "pay_no")
    private Long payNo;

    /** 收款项编码，见参数sys_pay_item_cfg */
    @TableField(value = "pay_item_id")
    private Long payItemId;

    /** 上级费用项编码，见参数sys_pay_item_cfg */
    @TableField(value = "parent_pay_item_id")
    private Long parentPayItemId;

    /** 期数 */
    @TableField(value = "periods")
    private Integer periods;

    /** 金额 */
    @TableField(value = "fee")
    private Long fee;

    /** 备注 */
    @TableField(value = "remark")
    private String remark;

    /** 开始时间 */
    @TableField(value = "start_date")
    private LocalDateTime startDate;

    /** 结束时间 */
    @TableField(value = "end_date")
    private LocalDateTime endDate;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}