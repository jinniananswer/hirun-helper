package com.microtomato.hirun.modules.organization.entity.po;

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
 * @author jinnian
 * @since 2019-09-16
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_org")
public class Org extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "org_id", type = IdType.AUTO)
    private Long orgId;

    @TableField("name")
    private String name;

    /**
     * 1-事业部 2-子公司 3-部门 4--店面 5-小组
     */
    @TableField("type")
    private String type;

    @TableField("contact_no")
    private Long contactNo;

    @TableField("enterprise_id")
    private Long enterpriseId;

    @TableField("parent_org_id")
    private Long parentOrgId;

    @TableField("city")
    private String city;

    @TableField("area")
    private String area;

    @TableField("status")
    private String status;

    @TableField(value="create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value="create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value="update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value="update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;
}
