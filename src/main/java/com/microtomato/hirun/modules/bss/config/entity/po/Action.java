package com.microtomato.hirun.modules.bss.config.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 动作定义表
 * </p>
 *
 * @author liuhui
 * @since 2020-04-27
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_action")
public class Action extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "action_id", type = IdType.AUTO)
    private Long actionId;

    @TableField("action_code")
    private String actionCode;

    @TableField("action_name")
    private String actionName;

    @TableField("action_desc")
    private String actionDesc;

    /**
     * HOUSE_COUNSELOR:家装顾问动作
     */
    @TableField("action_type")
    private String actionType;

    @TableField("order_no")
    private Integer orderNo;

    /**
     * 0:无效;1:有效
     */
    @TableField("status")
    private String status;


}
