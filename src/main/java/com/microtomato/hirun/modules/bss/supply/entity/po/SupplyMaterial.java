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
 * 材料表(SupplyMaterial)表实体类
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
@TableName("supply_material")
public class SupplyMaterial extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 供应商id */
    @TableField(value = "supplier_id")
    private Long supplierId;


    @TableField(value = "name")
    private String name;

    /** 材料类别 */
    @TableField(value = "material_type")
    private String materialType;

    /** 材料品牌 */
    @TableField(value = "material_brand")
    private String materialBrand;

    /** 材料单位 */
    @TableField(value = "material_unit")
    private String materialUnit;

    /** 成本价 */
    @TableField(value = "cost_price")
    private Integer costPrice;

    /** 销售价 */
    @TableField(value = "sale_price")
    private Integer salePrice;


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

    /** 材料规格 */
    @TableField(value = "standard")
    private String standard;

    /** 老系统材料id */
    @TableField(value = "material_code")
    private String materialCode;

}