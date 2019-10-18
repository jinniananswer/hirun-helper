package com.microtomato.hirun.modules.user.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 角色权限关系表
 * </p>
 *
 * @author Steven
 * @since 2019-09-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_func_role")
public class FuncRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "func_role_id", type = IdType.AUTO)
    private Long funcRoleId;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 权限ID
     */
    @TableField("func_id")
    private Long funcId;

    /**
     * 是否有效（0:有效）
     */
    @TableField("status")
    private String status;

    /**
     * 失效时间
     */
    @TableField("remove_date")
    private LocalDateTime removeDate;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
