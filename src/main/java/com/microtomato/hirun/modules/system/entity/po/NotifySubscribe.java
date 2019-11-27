package com.microtomato.hirun.modules.system.entity.po;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 消息订阅表
 * </p>
 *
 * @author Steven
 * @since 2019-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_notify_subscribe")
public class NotifySubscribe extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 目标的ID
     */
    @TableField("target_id")
    private Long targetId;

    /**
     * 目标类型
     */
    @TableField("target_type")
    private String targetType;

    /**
     * 提醒信息的动作类型
     */
    @TableField("action")
    private String action;

    /**
     * 用户消息所属者
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 是否激活
     */
    @TableField("is_enabled")
    private boolean enable;

}
