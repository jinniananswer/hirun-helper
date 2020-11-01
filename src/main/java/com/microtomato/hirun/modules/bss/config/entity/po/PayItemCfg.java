package com.microtomato.hirun.modules.bss.config.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 收款项配置表
 * </p>
 *
 * @author jinnian
 * @since 2020-02-23
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_pay_item_cfg")
public class PayItemCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 收款项名称
     */
    @TableField("name")
    private String name;

    /**
     * 上级费用项
     */
    @TableField("parent_pay_item_id")
    private Long parentPayItemId;

    /**
     * 0表示不分期，1表示分期
     */
    @TableField("is_period")
    private Integer isPeriod;

    /**
     * 具体分期值
     */
    @TableField("periods")
    private String periods;

    /**
     * 0-加 1-减
     */
    @TableField("direction")
    private Integer direction;

    /**
     * 其它扩展
     */
    @TableField("extend")
    private String extend;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * U-表示有效
     */
    @TableField("status")
    private String status;


}
