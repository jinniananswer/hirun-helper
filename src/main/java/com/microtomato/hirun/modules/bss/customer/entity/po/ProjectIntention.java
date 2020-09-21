package com.microtomato.hirun.modules.bss.customer.entity.po;

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
 * @since 2020-02-15
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_project_intention")
public class ProjectIntention extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "intention_id", type = IdType.AUTO)
    private Long intentionId;

    @TableField("project_id")
    private Long projectId;

    /**
     * 中国风主题
     */
    @TableField("chinesestyle_topic")
    private String chinesestyleTopic;

    /**
     * 欧洲主题
     */
    @TableField("europeanclassics_topic")
    private String europeanclassicsTopic;

    /**
     * 现代源主题
     */
    @TableField("modernsource_topic")
    private String modernsourceTopic;

    /**
     * 其他主题意向
     */
    @TableField("other_topic_req")
    private String otherTopicReq;

    /**
     * 功能意向
     */
    @TableField("func")
    private String func;

    /**
     * 有某某蓝图功能
     */
    @TableField("hasblueprint")
    private String hasblueprint;

    /**
     * 功能特殊要求
     */
    @TableField("func_spec_req")
    private String funcSpecReq;

    /**
     * 总计划投资
     */
    @TableField("total_priceplan")
    private String totalPriceplan;

    /**
     * 基础与木工计划投资
     */
    @TableField("basicandwood_priceplan")
    private String basicandwoodPriceplan;

    /**
     * 暖通及设备投资
     */
    @TableField("hvac_priceplan")
    private String hvacPriceplan;

    /**
     * 主材投资
     */
    @TableField("material_priceplan")
    private String materialPriceplan;

    /**
     * 移动家具
     */
    @TableField("furniture_priceplan")
    private String furniturePriceplan;

    /**
     * 电器投资
     */
    @TableField("electrical_priceplan")
    private String electricalPriceplan;

    /**
     * 计划居住时间
     */
    @TableField("plan_live_time")
    private LocalDateTime planLiveTime;

    /**
     * 设计师作品
     */
    @TableField("designer_opus")
    private String designerOpus;

    /**
     * 感兴趣的木制品
     */
    @TableField("wood_intention")
    private String woodIntention;

    /**
     * 合作银行
     */
    @TableField("cooperative_bank")
    private String cooperativeBank;

    /**
     * 分期金额
     */
    @TableField("quota")
    private String quota;

    /**
     * 分期期数
     */
    @TableField("month_num")
    private String monthNum;

    /**
     * 样板房
     */
    @TableField("sample_house")
    private String sampleHouse;

    /**
     * 只做木制品
     */
    @TableField("only_wood")
    private String onlyWood;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
