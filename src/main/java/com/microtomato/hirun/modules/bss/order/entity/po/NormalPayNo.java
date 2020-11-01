package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 非主营支付流水表
 * </p>
 *
 * @author sunxin
 * @since 2020-03-26
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("normal_pay_no")
public class NormalPayNo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 付款编码
     */
    @TableField("pay_no")
    private Long payNo;

    /**
     * 付款日期
     */
    @TableField("pay_date")
    private LocalDate payDate;

    /**
     * 总收款金额
     */
    @TableField("total_money")
    private Long totalMoney;

    /**
     * 0-未审核 1-审核通过 2-审核不通过
     */
    @TableField("audit_status")
    private String auditStatus;

    /**
     * 审核员工ID
     */
    @TableField("audit_employee_id")
    private Long auditEmployeeId;

    /**
     * 审核意见
     */
    @TableField("audit_comment")
    private String auditComment;

    /**
     * 开始时间
     */
    @TableField("start_date")
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    @TableField("end_date")
    private LocalDateTime endDate;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 处理员工
     */
    @TableField("pay_employee_id")
    private Long payEmployeeId;

    /**
     * 处理部门
     */
    @TableField("org_id")
    private Long orgId;

    /** 收据编号 */
    @TableField(value = "receipt_no")
    private String receiptNo;

    /**
     * 收单会计
     */
    @TableField("finance_employee_id")
    private Long financeEmployeeId;

    /**
     * 收单意见
     */
    @TableField("receive_comment")
    private Long receiveComment;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
