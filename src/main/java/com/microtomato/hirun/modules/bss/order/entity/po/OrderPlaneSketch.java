package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeDeserializer;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeSerializer;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ：mmzs
 * @date ：Created in 2020/2/6 19:56
 * @description：平面图信息
 * @modified By：
 * @version: 1.0$
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlaneSketch extends BaseEntity {

    /**
     * 订单平面图信息
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 设计方案数
     */
    @TableField("designer_plan_num")
    private Long designerPlanNum;

    /**
     * 套内面积
     */
    @TableField("indoor_area")
    private String indoorArea;

    /**
     * 设计费标准
     */
    @TableField("design_fee_standard")
    private Integer designFeeStandard;

    /**
     * 设计费主题
     */
    @TableField("design_theme")
    private String designTheme;

    /**
     * 合同设计费
     */
    @TableField("contract_design_fee")
    private Integer contractDesignFee;

    /**
     * 开始时间
     */
    @TableField("start_date")
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    @TableField("end_date")
    private LocalDateTime endDate;


    /**
     * 平面图开始时间
     */
    @TableField("plane_sketch_start_date")
    private LocalDate planeSketchStartDate;

    /**
     * 平面图结束时间
     */
    @TableField("plane_sketch_end_date")
    private LocalDate planeSketchEndDate;

    /**
     * 第一次看图时间
     */
    @TableField("first_look_time")
    private LocalDate firstLookTime;

    /**
     * 客户意见
     */
    @TableField("customer_comments")
    private String customerComments;

    /**
     * 创建时间
     */
    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建员工
     */
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 更新员工
     */
    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;
}
