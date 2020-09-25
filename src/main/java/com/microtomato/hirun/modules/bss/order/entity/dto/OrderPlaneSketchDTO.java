package com.microtomato.hirun.modules.bss.order.entity.dto;



import lombok.Data;

import java.time.LocalDate;

/**
 * @author ：mmzs
 * @date ：Created in
 * @description：1
 * @modified By：
 * @version: 1$
 */
@Data
public class OrderPlaneSketchDTO {

    /**
     * 订单平面图信息
     */
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 设计方案数
     */
    private Long designerPlanNum;

    private Long projectAssistant;

    /**
     * 套内面积
     */
    private String indoorArea;

    /**
     * 设计费标准
     */
    private Integer designFeeStandard;

    /**
     * 设计费主题
     */
    private String designTheme;

    /**
     * 合同设计费
     */
    private Integer contractDesignFee;


    /**
     * 平面图开始时间
     */
    private LocalDate planeSketchStartDate;

    /**
     * 平面图结束时间
     */

    private LocalDate planeSketchEndDate;

    /**
     * 第一次看图时间
     */
    private LocalDate firstLookTime;

    /**
     * 客户意见
     */
    private String customerComments;

    private Long designer;

    private String designerName;

    private Long financeEmployeeId;

    private String financeEmployeeName;

    private String employeeName;

    private Long assistantDesigner ;
}
