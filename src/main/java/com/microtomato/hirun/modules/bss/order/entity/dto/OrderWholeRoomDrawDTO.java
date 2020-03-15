package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ：mmzs
 * @date ：Created in 2020/3/10
 * @description：1
 * @modified By：
 * @version: 1$
 */
@Data
public class OrderWholeRoomDrawDTO {
    /**
     * 订单全房图信息
     */
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 开始时间
     */
    private LocalDate startTime;

    /**
     * 结束时间
     */
    private LocalDate endTime;

    /**
     * 预约看图时间
     */
    private LocalDate preTime;

    /**
     * 助理设计师
     */
    private String assistantDesigner;

    /**
     * 制作组长
     */
    private String productionLeader;

    /**
     * 水电设计师
     */
    private String hydropowerDesigner;

    /**
     * 绘图助理
     */
    private String drawingAssistant;

    /**
     * 行政助理
     */
    private String adminAssistant;

    /**
     * 设计师备注
     */
    private String designerRemarks;

    /**
     * 审核意见
     */
    private String reviewedComments;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建员工
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新员工
     */
    private Long updateUserId;

    private Long designer;
}
