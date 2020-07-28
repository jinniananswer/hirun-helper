package com.microtomato.hirun.modules.user.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

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
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_user_role")
public class UserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_role_id", type = IdType.AUTO)
    private Long userRoleId;

    @TableField("user_id")
    private Long userId;

    @TableField("role_id")
    private Long roleId;

    /**
     * ALTER TABLE ins_user_role ADD COLUMN is_main_role tinyint(1) NULL COMMENT '是否为主角色' AFTER `role_id`;
     *
     * 是否主角色 (1:主角色； 0:非主角色)
     */
    @TableField("is_main_role")
    private Boolean mainRole;

    public Boolean isMainRole() {
        return this.mainRole;
    }

    /**
     * 生效开始时间
     */
    @TableField("start_date")
    private LocalDateTime startDate;

    /**
     * 失效结束时间
     */
    @TableField("end_date")
    private LocalDateTime endDate;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
