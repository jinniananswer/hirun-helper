package com.microtomato.hirun.modules.bss.order.entity.dto;



import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
     * 开始时间
     */
    private LocalDate startTime;

    /**
     * 结束时间
     */

    private LocalDate endTime;

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

    private String designerName;

    private Long financeEmployeeId;

    private String financeEmployeeName;

    private List<OrderWorkerActionDTO> orderWorkActions;

    private String employeeName;
}
