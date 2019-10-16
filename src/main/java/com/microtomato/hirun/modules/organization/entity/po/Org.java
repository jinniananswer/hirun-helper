package com.microtomato.hirun.modules.organization.entity.po;

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
 * 
 * </p>
 *
 * @author jinnian
 * @since 2019-09-16
 */
@Data
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

}
