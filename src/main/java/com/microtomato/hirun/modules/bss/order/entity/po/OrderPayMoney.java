package com.microtomato.hirun.modules.bss.order.entity.po;

import java.time.LocalDate;
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
 * @date 2020-02-26 15:53:53
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

    /** 收款时间 */
    @TableField(value = "pay_date")
    private LocalDate payDate;

    /** 本次处理费用的员工ID */
    @TableField(value = "pay_employee_id")
    private Long payEmployeeId;

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