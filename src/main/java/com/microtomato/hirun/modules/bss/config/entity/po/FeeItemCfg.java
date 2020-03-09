package com.microtomato.hirun.modules.bss.config.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 费用项配置表(FeeItemCfg)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-04 23:50:13
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_fee_item_cfg")
public class FeeItemCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 费用项名称 */
    @TableField(value = "name")
    private String name;

    /** 类型：1-设计费 2-工程款 */
    @TableField(value = "type")
    private String type;

    /** 上级费用项 */
    @TableField(value = "parent_fee_item_id")
    private Long parentFeeItemId;

    /** 0表示不分期，1表示分期 */
    @TableField(value = "is_period")
    private Boolean isPeriod;

    /** 0-加 1-减 */
    @TableField(value = "direction")
    private Integer direction;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** U表示有效 */
    @TableField(value = "status")
    private String status;

}