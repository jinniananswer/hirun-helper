package com.microtomato.hirun.modules.finance.entity.po;

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
 * 财务领款单明细表(FinanceVoucherItem)表实体类
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
@TableName("finance_voucher_item")
public class FinanceVoucherItem extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 领款单编码 对应FinanceVoucher表ID*/
    @TableField(value = "voucher_id")
    private Long voucherId;

    /** 金额 */
    @TableField(value = "fee")
    private Double fee;

    /** 供应商id 针对材料付款使用 */
    @TableField(value = "supplier_id")
    private Long supplierId;

    /** 供应链id 针对材料付款使用 */
    @TableField(value = "supply_id")
    private Long supplyId;

    private Long orderId;

    /** 项目编码 可以为客户编码，师傅ID等 */
    @TableField(value = "project_id")
    private Long projectId;

    /** 费用科目，见参数finance_item */
    @TableField(value = "voucher_item_id")
    private String voucherItemId;

    /** 上级费用科目，见参数finance_item */
    @TableField(value = "parent_voucher_item_id")
    private String parentVoucherItemId;

    /** 开始时间 */
    @TableField(value = "start_date")
    private LocalDateTime startDate;

    /** 结束时间 */
    @TableField(value = "end_date")
    private LocalDateTime endDate;

    /** 备注 */
    @TableField(value = "remark")
    private String remark;

    /** 归属部门 见org表 */
    @TableField(value = "department")
    private Long department;

    /** 归属店面 见org表 */
    @TableField(value = "storefront")
    private Long storefront;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}