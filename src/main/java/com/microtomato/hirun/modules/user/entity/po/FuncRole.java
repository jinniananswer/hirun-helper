package com.microtomato.hirun.modules.user.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 失效时间
     */
    @TableField("remove_date")
    private LocalDateTime removeDate;

    /**
     * 创建用户
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 更新用户
     */
    @TableField("update_user_id")
    private Long updateUserId;


}
