package com.microtomato.hirun.modules.bss.order.entity.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 订单优惠项
 * </p>
 *
 * @author anwx
 * @since 2020-02-26
 */
@Data
public class OrderDiscountItemDTO {

    /**
     * 优惠项id
     */
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    private Long employeeId;

    private String employeeName;

    /**
     * 优惠项目
     */
    private String discountItem;

    private String discountItemName;

    /**
     * 合同优惠金额
     */
    private Integer contractDiscountFee;

    /**
     * 结算优惠金额
     */
    private Integer settleDiscountFee;

    /**
     * 优惠审批人
     */
    private Long approveEmployeeId;

    private String approveEmployeeName;

    /**
     * 审批时间
     */
    private LocalDateTime approveTime;

    /**
     * 状态
     */
    private String status;

    private String statusName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
