package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author ：xiaocl
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
     * 绘图开始时间
     */
    private LocalDate drawStartDate;

    /**
     * 绘图结束时间
     */
    private LocalDate drawEndDate;

    /**
     * 预约看图时间
     */
    private LocalDate preTime;

    /**
     * 助理设计师
     */
    private List<Long> assistantDesigner;

    /**
     * 制作组长
     */
    private Long productionLeader;

    /**
     * 水电设计师
     */
    private Long hydropowerDesigner;

    /**
     * 绘图助理
     */
    private Long drawingAssistant;

    /**
     * 行政助理
     */
    private Long adminAssistant;

    /**
     * 设计师备注
     */
    private String designerRemarks;

    /**
     * 审核意见
     */
    private String reviewedComments;

    /**
     * 下单审核意见
     */
    private String orderComments;

    /**
     * 设计师
     */
    private Long designer;

    /**
     * 图纸审核员
     */
    private Long drawingAuditor;

    private String drawingAuditorName;

    /**
     * 下单审核员
     */
    private Long customerLeader;

    private String customerLeaderName;


    private String employeeName;

    private  String pageTag;
}
