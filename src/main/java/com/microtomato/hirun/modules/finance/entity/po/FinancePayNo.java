package com.microtomato.hirun.modules.finance.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会计付款流水表(FinancePayNo)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-11-24 15:57:13
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("finance_pay_no")
public class FinancePayNo extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 付款编码 */
    @TableField(value = "pay_no")
    private Long payNo;

    /** 付款单号 */
    @TableField(value = "bill_no")
    private String billNo;

    /** 领款单号 */
    @TableField(value = "voucher_no")
    private String voucherNo;

    /** 付款日期 */
    @TableField(value = "pay_date")
    private LocalDate payDate;

    /** 总付款金额 */
    @TableField(value = "total_money")
    private Long totalMoney;

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


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}