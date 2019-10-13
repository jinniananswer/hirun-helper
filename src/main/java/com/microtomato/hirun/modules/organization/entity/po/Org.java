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

    @TableId(value = "ORG_ID", type = IdType.AUTO)
    private Long orgId;

    @TableField("NAME")
    private String name;

    /**
     * 1-事业部 2-子公司 3-部门 4--店面 5-小组
     */
    @TableField("TYPE")
    private String type;

    @TableField("CONTACT_NO")
    private Long contactNo;

    @TableField("ENTERPRISE_ID")
    private Long enterpriseId;

    @TableField("PARENT_ORG_ID")
    private Long parentOrgId;

    @TableField("CITY")
    private String city;

    @TableField("AREA")
    private String area;

    @TableField("STATUS")
    private String status;

    @TableField("CREATE_USER_ID")
    private Long createUserId;

    @TableField("CREATE_DATE")
    private LocalDateTime createDate;

    @TableField("UPDATE_USER_ID")
    private Long updateUserId;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;


}
