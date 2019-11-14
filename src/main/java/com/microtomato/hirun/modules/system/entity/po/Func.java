package com.microtomato.hirun.modules.system.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * 
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
@TableName("sys_func")
public class Func extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "func_id", type = IdType.AUTO)
    private Long funcId;

    /**
     * 权限编码
     */
    @TableField("func_code")
    private String funcCode;

    /**
     * 权限类型（'0' 菜单权限，'M' 操作权限）
     */
    @TableField("type")
    private String type;

    /**
     * 权限描述
     */
    @TableField("func_desc")
    private String funcDesc;

    /**
     * 状态（'0' 有效）
     */
    @TableField("status")
    private String status;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
