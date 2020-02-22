package com.microtomato.hirun.modules.bss.config.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 订单状态转换配置表
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
@TableName("sys_order_status_trans_cfg")
public class OrderStatusTransCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单阶段状态ID,-1表示任意状态
     */
    @TableField("order_status_id")
    private Long orderStatusId;

    /**
     * 当前处理角色
     */
    @TableField("oper_code")
    private String operCode;

    /**
     * 下一订单阶段状态ID
     */
    @TableField("next_order_status_id")
    private Long nextOrderStatusId;

    /**
     * 是否更新前续订单状态，1-是，0-否
     */
    @TableField("is_update_previous")
    private Integer isUpdatePrevious;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
