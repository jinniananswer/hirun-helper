package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 付款类型明细表(OrderPayMoney)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-02-29 11:02:01
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_pay_money")
public class OrderPayMoney extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 订单编号 */
    @TableField(value = "order_id")
    private Long orderId;

    /** 付款单号 */
    @TableField(value = "pay_no")
    private Long payNo;

    /** 付费类型，见参数 */
    @TableField(value = "payment_type")
    private String paymentType;

    /** 付费方式，见finance_acct */
    @TableField(value = "payment_id")
    private Long paymentId;

    /** 费用 */
    @TableField(value = "money")
    private Long money;

    /** 开始时间 */
    @TableField(value = "start_date")
    private LocalDateTime startDate;

    /** 结束时间 */
    @TableField(value = "end_date")
    private LocalDateTime endDate;

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