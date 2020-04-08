package com.microtomato.hirun.modules.bss.order.entity.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * <p>
 * 供应商品牌表
 * </p>
 *
 * @author sunxin
 * @since 2020-03-29
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_supplier_brand")
public class SupplierBrand extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 品牌名称
     */
    @TableField("name")
    private String name;

    /**
     * 供应商id
     */
    @TableField("supplier_id")
    private Long supplierId;

    /**
     * 品牌类别
     */
    @TableField("brand_type")
    private String brandType;

    /**
     * 成本百分比，例如78代表78%
     */
    @TableField("cost_rate")
    private BigDecimal costRate;

    /**
     * 提成百分比，例如8.5代表8.5%
     */
    @TableField("royalty_rate")
    private BigDecimal royaltyRate;

    /**
     * 毛利润百分比，例如13.5代表13.5%
     */
    @TableField("gross_profit_rate")
    private BigDecimal grossProfitRate;

    @TableField("remark")
    private String remark;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 状态，0代表在用，1为废弃
     */
    @TableField("status")
    private String status;


}
