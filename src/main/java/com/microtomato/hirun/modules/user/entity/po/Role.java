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
 * 角色表 (归属组织的角色，职级的角色等)
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
@TableName("ins_role")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ROLE_ID
     */
    @TableId(value = "ROLE_ID", type = IdType.AUTO)
    private Integer roleId;

    /**
     * 角色类型（'O':组织角色, 'T':职称角色）
     */
    @TableField("ROLE_TYPE")
    private String roleType;

    /**
     * 角色名
     */
    @TableField("ROLE_NAME")
    private String roleName;

    /**
     * 是否有效（0:有效）
     */
    @TableField("STATUS")
    private String status;

    /**
     * 创建时间
     */
    @TableField("CREATE_DATE")
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 失效时间
     */
    @TableField("REMOVE_DATE")
    private LocalDateTime removeDate;

    /**
     * 创建用户
     */
    @TableField("CREATE_USER_ID")
    private Integer createUserId;

    /**
     * 更新用户
     */
    @TableField("UPDATE_USER_ID")
    private Integer updateUserId;

    /**
     * 角色描述
     */
    @TableField("REMARK")
    private String remark;


}
