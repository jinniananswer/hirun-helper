package com.microtomato.hirun.modules.bss.supply.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 供应商表(SupplySupplier)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-05 17:41:16
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("supply_supplier")
public class Supplier extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 供应商名称 */
    @TableField(value = "name")
    private String name;

    /** 供应商简称 */
    @TableField(value = "abbreviation")
    private String abbreviation;

    /** 供应商类型 */
    @TableField(value = "supplier_type")
    private String supplierType;

    /** 经办人 */
    @TableField(value = "operator")
    private String operator;

    /** 联系电话 */
    @TableField(value = "mobile_no")
    private String mobileNo;

    /** 座机 */
    @TableField(value = "landline")
    private String landline;

    /** 邮箱 */
    @TableField(value = "mailbox")
    private String mailbox;

    /** 地址 */
    @TableField(value = "address")
    private String address;

    /** 预留1 */
    @TableField(value = "field1")
    private String field1;

    /** 预留2 */
    @TableField(value = "field2")
    private String field2;


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

    /** 联系人 */
    @TableField(value = "contacts")
    private String contacts;

    /** 备用联系电话 */
    @TableField(value = "standby_telephone")
    private String standbyTelephone;

}