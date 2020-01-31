package com.microtomato.hirun.modules.bss.order.entity.po;

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
 * 订单预算表
 * </p>
 *
 * @author anwenxuan
 * @since 2020-01-29
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderBudget extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单预算ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 预算总费用
     */
    @TableField("fee")
    private Integer fee;

    /**
     * 看预算时间
     */
    @TableField("reported_budget_date")
    private LocalDate reportedBudgetDate;

    /**
     * 客户想法
     */
    @TableField("cust_idea")
    private String custIdea;

    /**
     * 活动编码
     */
    @TableField("activity_id")
    private String activityId;

    /**
     * 汇总金额是否正确
     */
    @TableField("total_fee_check_result")
    private String totalFeeCheckResult;

    /**
     * 所做项目位置标注是否不清
     */
    @TableField("location_remark_check_result")
    private String locationRemarkCheckResult;

    /**
     * 格式文字表达是否正确
     */
    @TableField("content_expression_check_result")
    private String contentExpressionCheckResult;

    /**
     * 表格上单价是否正确
     */
    @TableField("unit_price_check_result")
    private String unitPriceCheckResult;

    /**
     * 徽标是否错误、变形
     */
    @TableField("logo_check_result")
    private String logoCheckResult;

    /**
     * 表格上单价前后是否一致
     */
    @TableField("unit_price_consistence_check_result")
    private String unitPriceConsistenceCheckResult;

    /**
     * 表格上材料是否标注清楚
     */
    @TableField("material_remark_check_result")
    private String materialRemarkCheckResult;

    /**
     * 序号排列是否清楚
     */
    @TableField("serial_number_check_result")
    private String serialNumberCheckResult;

    /**
     * 预算材料与预算表后说明是否统一
     */
    @TableField("material_remark_consistence_check_result")
    private String materialRemarkConsistenceCheckResult;

    /**
     * 字体大小有没有调整
     */
    @TableField("font_size_check_result")
    private String fontSizeCheckResult;

    /**
     * 对客户自购的材料是否标注清楚
     */
    @TableField("self_purchase_remark_check_result")
    private String selfPurchaseRemarkCheckResult;

    /**
     * 汇总表与明细表数字前后是否一致
     */
    @TableField("number_consistence_check_result")
    private String numberConsistenceCheckResult;

    /**
     * 检查时间
     */
    @TableField("check_date")
    private LocalDate checkDate;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
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
