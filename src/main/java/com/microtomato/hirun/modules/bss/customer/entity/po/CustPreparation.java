package com.microtomato.hirun.modules.bss.customer.entity.po;

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
 * @since 2020-02-01
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustPreparation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("cust_id")
    private Long custId;

    @TableField("prepare_org_id")
    private Long prepareOrgId;

    /**
     * 报备人的id
     */
    @TableField("prepare_employee_id")
    private Long prepareEmployeeId;

    /**
     * 报备时间
     */
    @TableField("prepare_time")
    private LocalDateTime prepareTime;

    /**
     * 录入人id
     */
    @TableField("enter_employee_id")
    private Long enterEmployeeId;

    /**
     * 录入时间
     */
    @TableField("enter_time")
    private LocalDateTime enterTime;

    /**
     * 客户属性
     */
    @TableField("cust_property")
    private String custProperty;

    /**
     * 推荐人姓名
     */
    @TableField("referee_name")
    private String refereeName;

    @TableField("referee_mobile_no")
    private String refereeMobileNo;

    /**
     * 推荐人装修地点
     */
    @TableField("referee_fix_place")
    private String refereeFixPlace;

    @TableField("remark")
    private String remark;

    @TableField("status")
    private String status;

    @TableField("prepartion_expire_time")
    private LocalDateTime prepartionExpireTime;

    @TableField("ruling_employee_id")
    private Long rulingEmployeeId;

    @TableField("ruling_time")
    private LocalDateTime rulingTime;

    @TableField("ruling_remark")
    private String rulingRemark;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
