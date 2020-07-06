package com.microtomato.hirun.modules.bss.supply.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 供应商品牌表(SupplySupplierBrand)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-05 17:46:45
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("supply_supplier_brand")
public class SupplierBrand extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 品牌名称 */
    @TableField(value = "name")
    private String name;

    /** 供应商id */
    @TableField(value = "supplier_id")
    private Long supplierId;

    /** 品牌类别 */
    @TableField(value = "brand_type")
    private String brandType;

    /** 成本百分比，例如78代表78% */
    @TableField(value = "cost_rate")
    private Double costRate;

    /** 提成百分比，例如8.5代表8.5% */
    @TableField(value = "royalty_rate")
    private Double royaltyRate;

    /** 毛利润百分比，例如13.5代表13.5% */
    @TableField(value = "gross_profit_rate")
    private Double grossProfitRate;


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

    /** 状态，0代表在用，1为废弃 */
    @TableField(value = "status")
    private String status;

}