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
 * 消息表
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
@TableName("sys_notify")
public class Notify extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息内容
     */
    @TableField("content")
    private String content;

    /**
     * 消息类型（1: 公告 announce，2: 提醒 remind，3：信息 message）
     */
    @TableField("notify_type")
    private Integer notifyType;

    /**
     * 目标ID
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
     * 发送者ID
     */
    @TableField("sender_id")
    private Long senderId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
