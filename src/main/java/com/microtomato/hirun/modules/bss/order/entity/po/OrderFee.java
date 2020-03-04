package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 订单费用表(OrderFee)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-04 23:05:59
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_fee")
public class OrderFee extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField(value = "order_id")
    private Long orderId;

    /** 费用编号 */
    @TableField(value = "fee_no")
    private Long feeNo;

    /** 类型 1-设计费 2-工程款 */
    @TableField(value = "type")
    private String type;

    /** 期数 */
    @TableField(value = "periods")
    private Integer periods;

    /** 合同总金额 */
    @TableField(value = "total_fee")
    private Long totalFee;

    /** 应收金额（总费用减去客户已收费用） */
    @TableField(value = "need_pay")
    private Long needPay;

    /** 实收金额 */
    @TableField(value = "pay")
    private Long pay;


    @TableField(value = "contract_id")
    private Long contractId;

    /** 本次处理费用的员工ID */
    @TableField(value = "fee_employee_id")
    private Long feeEmployeeId;

    /** 费用部门 */
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