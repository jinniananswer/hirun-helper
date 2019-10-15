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
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    /**
     * 角色类型（'O':组织角色, 'T':职称角色）
     */
    @TableField("role_type")
    private String roleType;

    /**
     * 角色名
     */
    @TableField("role_name")
    private String roleName;

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


    /**
     * 角色描述
     */
    @TableField("remark")
    private String remark;


}
