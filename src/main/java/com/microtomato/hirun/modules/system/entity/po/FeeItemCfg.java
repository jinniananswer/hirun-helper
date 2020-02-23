package com.microtomato.hirun.modules.system.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 费用项配置表
 * </p>
 *
 * @author sunxin
 * @since 2020-02-06
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

    /**
     * 费用项名称
     */
    @TableField("name")
    private String name;

    /**
     * 上级费用项
     */
    @TableField("parent_fee_item_id")
    private Long parentFeeItemId;

    /**
     * 0-不分期 1-分期
     */
    @TableField("is_period")
    private Integer isPeriod;

    /**
     * 0-加 1-减
     */
    @TableField("direction")
    private Integer direction;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 状态，U表示有效
     */
    @TableField("status")
    private String status;


}
