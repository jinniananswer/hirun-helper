package com.microtomato.hirun.modules.bss.supply.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 供应订单表(SupplyOrder)表实体类
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
@TableName("supply_order")
public class SupplyOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 主营系统订单号，暂可以为空 */
    @TableField(value = "order_id")
    private Long orderId;


    /** 总费用 */
    @TableField(value = "total_fee")
    private Long totalFee;

    /** 1-主营材料下单 2-主营材料退单 3-材料入库 4-材料出库 */
    @TableField(value = "supply_order_type")
    private String supplyOrderType;

    /** 下单人 */
    @TableField(value = "create_employee_id")
    private Long createEmployeeId;

    /** 备注 */
    @TableField(value = "remark")
    private String remark;

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

}