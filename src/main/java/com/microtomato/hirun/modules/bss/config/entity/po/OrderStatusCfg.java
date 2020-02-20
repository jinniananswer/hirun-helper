package com.microtomato.hirun.modules.bss.config.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 订单阶段及状态配置表
 * </p>
 *
 * @author jinnian
 * @since 2020-02-09
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_order_status_cfg")
public class OrderStatusCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类型 H-家装订单 W-木制品订单
     */
    @TableField("type")
    private String type;

    /**
     * 订单阶段
     */
    @TableField("order_stage")
    private Integer orderStage;

    /**
     * 订单状态
     */
    @TableField("order_status")
    private String orderStatus;

    /**
     * 处理页面
     */
    @TableField("page_url")
    private String pageUrl;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
