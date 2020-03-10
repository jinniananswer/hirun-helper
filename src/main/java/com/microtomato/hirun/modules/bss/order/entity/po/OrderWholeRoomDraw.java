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
 * @date ：Created in 2020/2/6 22:50
 * @description：订单全房图流程
 * @modified By：
 * @version: 1.0$
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderWholeRoomDraw extends BaseEntity {
    /**
     * 订单全房图信息
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    private LocalDate startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private LocalDate endTime;

    /**
     * 预约看图时间
     */
    @TableField(value = "pre_time")
    private LocalDate preTime;

    /**
     * 助理设计师
     */
    @TableField(value = "assistant_designer")
    private String assistantDesigner;

    /**
     * 制作组长
     */
    @TableField(value = "production_leader")
    private String productionLeader;

    /**
     * 水电设计师
     */
    @TableField(value = "hydropower_designer")
    private String hydropowerDesigner;

    /**
     * 绘图助理
     */
    @TableField(value = "drawing_assistant")
    private String drawingAssistant;

    /**
     * 行政助理
     */
    @TableField(value = "admin_assistant")
    private String adminAssistant;

    /**
     * 设计师备注
     */
    @TableField(value = "designer_remarks")
    private String designerRemarks;

    /**
     * 审核意见
     */
    @TableField(value = "reviewed_comments")
    private String reviewedComments;

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
