package com.microtomato.hirun.modules.bss.supply.entity.po;

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
    private Double totalFee;

    /** 1-主营材料下单 2-主营材料退单 3-材料入库 4-材料出库 */
    @TableField(value = "supply_order_type")
    private String supplyOrderType;


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

    /** 状态：暂0代表在用，1为废弃 */
    @TableField(value = "supply_status")
    private String supplyStatus;

}