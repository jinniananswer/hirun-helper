package com.microtomato.hirun.modules.user.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author Steven
 * @since 2019-11-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_role_mapping")
public class RoleMapping extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Id ins_org.org_id
     */
    @TableField("org_id")
    private Long orgId;

    /**
     *  code_value, select * from sys_static_data t where t.code_type="JOB_ROLE"
     */
    @TableField("job_role")
    private String jobRole;

    /**
     * Id ins_role.role_id
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 10
     */
    @TableField("is_enabled")
    private Boolean enabled;


}
