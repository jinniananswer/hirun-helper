package com.microtomato.hirun.modules.finance.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 财务领款单表(FinanceVoucher)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-25 21:25:21
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("finance_voucher")
public class FinanceVoucher extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableId(value = "voucher_no")
    private String voucherNo;

    /** 制单大类 */
    @TableField(value = "type")
    private String type;

    /** 制单小类 */
    @TableField(value = "item")
    private String item;

    /** 领款单日期 */
    @TableField(value = "voucher_date")
    private LocalDate voucherDate;

    /** 领款单总金额 */
    @TableField(value = "total_money")
    private Long totalMoney;

    /** 0-未审核 1-财务审核通过 2-审核不通过 3-出纳付款中 4-待提交会计 5-挂账中 6-会计收单 */
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

    /** 处理员工ID */
    @TableField(value = "voucher_employee_id")
    private Long voucherEmployeeId;

    /** 处理部门 */
    @TableField(value = "org_id")
    private Long orgId;

    /** 制单员 */
    @TableField(value = "create_employee_id")
    private Long createEmployeeId;

    /** 订单ID */
    @TableField(value = "order_id")
    private Long orderId;

    /** 出纳（付款员工） */
    @TableField(value = "cashier_employee_id")
    private Long cashierEmployeeId;

    /** 付款日期 */
    @TableField(value = "pay_date")
    private LocalDate payDate;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}