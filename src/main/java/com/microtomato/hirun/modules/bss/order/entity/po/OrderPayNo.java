package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单支付流水表表(OrderPayNo)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-06 00:00:08
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_pay_no")
public class OrderPayNo extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField(value = "order_id")
    private Long orderId;

    /** 付款编码 */
    @TableField(value = "pay_no")
    private Long payNo;

    /** 付款日期 */
    @TableField(value = "pay_date")
    private LocalDate payDate;

    /** 总收款金额 */
    @TableField(value = "total_money")
    private Long totalMoney;

    /** 0-未审核 1-审核通过 2-审核不通过 */
    @TableField(value = "audit_status")
    private String auditStatus;

    /** 审核员工ID */
    @TableField(value = "audit_employee_id")
    private Long auditEmployeeId;

    /** 审核意见 */
    @TableField(value = "audit_comment")
    private String auditComment;

    /** 开始时间 */
    @TableField(value = "start_date")
    private LocalDateTime startDate;

    /** 结束时间 */
    @TableField(value = "end_date")
    private LocalDateTime endDate;

    /** 备注 */
    @TableField(value = "remark")
    private String remark;

    /** 处理员工 */
    @TableField(value = "pay_employee_id")
    private Long payEmployeeId;

    /** 处理部门 */
    @TableField(value = "org_id")
    private Long orgId;

    /** 收据编号 */
    @TableField(value = "receipt_no")
    private String receiptNo;

    /** 收单会计 */
    @TableField(value = "finance_employee_id")
    private Long financeEmployeeId;

    /** 收单备注 */
    @TableField(value = "receive_comment")
    private String receiveComment;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}