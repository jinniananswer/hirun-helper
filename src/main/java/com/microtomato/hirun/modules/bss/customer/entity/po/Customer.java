package com.microtomato.hirun.modules.bss.customer.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
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
 * 客户信息
 * </p>
 *
 * @author liuhui
 * @since 2020-04-20
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_customer")
public class Customer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "cust_id", type = IdType.AUTO)
    private Long custId;

    /**
     * 关键识别号
     */
    @TableField("identify_code")
    private String identifyCode;

    /**
     * 客户名称
     */
    @TableField("cust_name")
    private String custName;

    /**
     * 客户性别
     */
    @TableField("sex")
    private Boolean sex;

    /**
     * 微信昵称
     */
    @TableField("wx_nick")
    private String wxNick;

    /**
     * 客户状态
     */
    @TableField("cust_status")
    private Integer custStatus;

    @TableField("house_id")
    private Long houseId;

    /**
     * 电话号码
     */
    @TableField("mobile_no")
    private String mobileNo;

    /**
     * 楼栋详情
     */
    @TableField("house_detail")
    private String houseDetail;

    /**
     * 户型
     */
    @TableField("house_mode")
    private String houseMode;

    /**
     * 面积
     */
    @TableField("house_area")
    private Integer houseArea;

    /**
     * 客户基本情况
     */
    @TableField("cust_detail")
    private String custDetail;

    /**
     * 最后动作
     */
    @TableField("last_action")
    private String lastAction;

    /**
     * 最后动作时间
     */
    @TableField("last_action_date")
    private LocalDateTime lastActionDate;

    /**
     * 首次计划时间
     */
    @TableField("first_plan_date")
    private LocalDate firstPlanDate;

    /**
     * 恢复时间
     */
    @TableField("restore_date")
    private LocalDate restoreDate;

    @TableField("house_counselor_id")
    private Long houseCounselorId;

    @TableField("cust_agent_id")
    private Long custAgentId;

    @TableField("cust_desigener_id")
    private Long custDesigenerId;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
