package com.microtomato.hirun.modules.finance.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @TableField(value = "voucher_no")
    private String voucherNo;

    /** 金额 */
    @TableField(value = "fee")
    private Long fee;

    /** 供应商id 针对材料付款使用 */
    @TableField(value = "supplier_id")
    private Long supplierId;

    /** 供应链id 针对材料付款使用 */
    @TableField(value = "supply_id")
    private Long supplyId;

    /** 项目类型 1-公司员工 2-非公司员工 3-工人 */
    @TableField(value = "project_type")
    private String projectType;

    /** 项目编码 可以为客户编码，师傅ID等 */
    @TableField(value = "project_id")
    private Long projectId;

    @TableField(value = "project_name")
    private String projectName;

    /** 费用科目，见参数finance_item */
    @TableField(value = "finance_item_id")
    private String financeItemId;

    /** 上级费用科目，见参数finance_item */
    @TableField(value = "parent_finance_item_id")
    private String parentFinanceItemId;

    /** 差旅类型 */
    @TableField(value = "traffic_type")
    private String trafficType;

    /** 差旅日期 */
    @TableField(value = "traffic_date")
    private LocalDate trafficDate;

    /** 交通起点 */
    @TableField(value = "traffic_begin")
    private String trafficBegin;

    /** 交通终点 */
    @TableField(value = "traffic_end")
    private String trafficEnd;

    /** 交通费 */
    @TableField(value = "traffic_Fee")
    private Long trafficFee;

    /** 出差补助 */
    @TableField(value = "allowance")
    private Long allowance;

    /** 酒店费 */
    @TableField(value = "hotel_fee")
    private Long hotelFee;

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
    @TableField(value = "org_id")
    private Long orgId;

    /** 归属店面 见org表 */
    @TableField(value = "shop_id")
    private Long shopId;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}