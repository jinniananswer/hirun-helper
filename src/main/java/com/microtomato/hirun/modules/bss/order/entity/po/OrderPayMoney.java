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

    /** 付费方式，见参数 */
    @TableField(value = "payment_type")
    private String paymentType;

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