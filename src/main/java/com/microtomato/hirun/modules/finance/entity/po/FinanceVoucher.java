package com.microtomato.hirun.modules.finance.entity.po;

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


    /** 领款单类型 1-材料单 2-施工队领款单 3-其他领款单 */
    @TableField(value = "voucher_type")
    private Long voucherType;

    /** 领款单日期 */
    @TableField(value = "voucher_date")
    private LocalDateTime voucherDate;

    /** 领款单总金额 */
    @TableField(value = "total_money")
    private Double totalMoney;

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


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}