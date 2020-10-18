package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderBudgetDTO {

    /**
     * 订单预算ID
     */
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 预算总费用
     */
    private Integer fee;

    /**
     * 看预算时间
     */
    private LocalDate reportedBudgetDate;

    /**
     * 客户想法
     */
    private String custIdea;

    /**
     * 活动编码
     */
    private String activityId;

    /**
     * 汇总金额是否正确
     */
    private String totalFeeCheckResult;

    /**
     * 所做项目位置标注是否不清
     */
    private String locationRemarkCheckResult;

    /**
     * 格式文字表达是否正确
     */
    private String contentExpressionCheckResult;

    /**
     * 表格上单价是否正确
     */
    private String unitPriceCheckResult;

    /**
     * 徽标是否错误、变形
     */
    private String logoCheckResult;

    /**
     * 表格上单价前后是否一致
     */
    private String unitPriceConsistenceCheckResult;

    /**
     * 表格上材料是否标注清楚
     */
    private String materialRemarkCheckResult;

    /**
     * 序号排列是否清楚
     */
    private String serialNumberCheckResult;

    /**
     * 预算材料与预算表后说明是否统一
     */
    private String materialRemarkConsistenceCheckResult;

    /**
     * 字体大小有没有调整
     */
    private String fontSizeCheckResult;

    /**
     * 对客户自购的材料是否标注清楚
     */
    private String selfPurchaseRemarkCheckResult;

    /**
     * 汇总表与明细表数字前后是否一致
     */
    private String numberConsistenceCheckResult;

    /**
     * 检查时间
     */
    private LocalDate checkDate;

    /**
     * 对审人
     */
    private Long checkEmployeeId;

    /**
     * vr渲染员工
     */
    private Long vrEmployeeId;

    /**
     * su建白模员工
     */
    private Long suEmployeeId;

    /**
     * 备注
     */
    private String remark;
}
