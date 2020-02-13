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
 * @author liuhui
 * @since 2020-02-14
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_prepare_config")
public class PrepareConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 是否限制家装顾问责任楼盘1、限制，2、不限制
     */
    @TableField("is_limit_consult")
    private Integer isLimitConsult;

    /**
     * 对应上一个字段，如果限制则填写限制的部门
     */
    @TableField("limit_org_id")
    private String limitOrgId;

    /**
     * 客户限制报备次数
     */
    @TableField("limit_cust_prepare_times")
    private Integer limitCustPrepareTimes;

    /**
     * 客户限制报备周期
     */
    @TableField("limit_cust_prepare_cycle")
    private Integer limitCustPrepareCycle;

    /**
     * 部门限制7天内报备次数
     */
    @TableField("limit_org_prepare_times")
    private Integer limitOrgPrepareTimes;

    /**
     * 限制部门7天内报备次数的部门ID集合
     */
    @TableField("limit_prepare_org_id")
    private String limitPrepareOrgId;

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
