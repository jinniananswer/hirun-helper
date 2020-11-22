package com.microtomato.hirun.modules.bss.supply.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 供应订单明细表(SupplyOrderDetail)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-15 11:26:08
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("supply_order_detail")
public class SupplyOrderDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 供应链id */
    @TableField(value = "supply_id")
    private Long supplyId;

    /** 供应商id */
    @TableField(value = "supplier_id")
    private Long supplierId;

    /** 仓库id */
    @TableField(value = "storehouse_id")
    private Long storehouseId;

    /** 材料id */
    @TableField(value = "material_id")
    private Long materialId;

    /** 明细费用 */
    @TableField(value = "fee")
    private Long fee;

    /** 材料数量 */
    @TableField(value = "num")
    private Integer num;

    /** 材料数量 */
    @TableField(value = "total_money")
    private Long totalMoney;

    /** 备注 */
    @TableField(value = "remark")
    private String remark;

    /** 审核状态 */
    @TableField(value = "audit_status")
    private String auditStatus;

    /** 审核人 */
    @TableField(value = "audit_employee_id")
    private Long auditEmployeeId;

    /** 审核备注 */
    @TableField(value = "audit_comment")
    private String auditComment;

    /** 下单人 */
    @TableField(value = "create_employee_id")
    private Long createEmployeeId;

    /** 开始时间 */
    @TableField(value = "start_date")
    private LocalDateTime startDate;

    /** 结束时间 */
    @TableField(value = "end_date")
    private LocalDateTime endDate;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 状态，暂0代表在用，1为废弃 */
    @TableField(value = "status")
    private String status;

}