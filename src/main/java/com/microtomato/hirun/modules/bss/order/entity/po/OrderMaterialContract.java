package com.microtomato.hirun.modules.bss.order.entity.po;

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
 * (OrderMaterialContract)表实体类
 *
 * @author liuhui7
 * @version 1.0.0
 * @date 2020-09-24 00:05:40
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_material_contract")
public class OrderMaterialContract extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField(value = "order_id")
    private Long orderId;

    /** 材料类型 */
    @TableField(value = "material_type")
    private String materialType;

    /** 品牌类型 */
    @TableField(value = "brand_code")
    private String brandCode;

    /** 合同金额 */
    @TableField(value = "contract_fee")
    private Long contractFee;

    /** 优惠金额 */
    @TableField(value = "discount_fee")
    private Long discountFee;


    @TableField(value = "actual_fee")
    private Long actualFee;

    @TableField(value = "pay_time")
    private LocalDateTime payTime;


    @TableField(value = "start_date")
    private LocalDateTime startDate;


    @TableField(value = "end_date")
    private LocalDateTime endDate;


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