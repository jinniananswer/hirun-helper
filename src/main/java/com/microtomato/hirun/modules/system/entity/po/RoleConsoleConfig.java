package com.microtomato.hirun.modules.system.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 角色控制台配置(RoleConsoleConfig)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-27 22:04:30
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("role_console_config")
public class RoleConsoleConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 角色ID */
    @TableField(value = "role_id")
    private Long roleId;

    /** type为1时配-1，type为2时配订单状态值，多个以逗号分隔 */
    @TableField(value = "type")
    private String type;

    /** 1-hr H-家装订单 W-木制品订单 */
    @TableField(value = "pending_type")
    private String pendingType;

    /** 1-员工入职对比 2-订单数据对比 */
    @TableField(value = "chart_type")
    private String chartType;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}