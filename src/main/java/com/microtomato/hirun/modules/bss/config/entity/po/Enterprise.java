package com.microtomato.hirun.modules.bss.config.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author sunxin
 * @since 2020-03-10
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_enterprise")
public class Enterprise extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "enterprise_id", type = IdType.AUTO)
    private Long enterpriseId;

    @TableField("name")
    private String name;

    /**
     * 手机/固话/邮箱/微信/QQ
     */
    @TableField("org_code")
    private String orgCode;

    @TableField("taxpayer_no")
    private String taxpayerNo;

    @TableField("industry_type")
    private String industryType;

    @TableField("address")
    private String address;

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
