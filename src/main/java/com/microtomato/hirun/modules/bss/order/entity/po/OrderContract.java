package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单合同
 * </p>
 *
 * @author anwx
 * @since 2020-02-23
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderContract extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 合同号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 合同类型
     */
    @TableField("contract_type")
    private String contractType;

    @TableField("contract_no")
    private String contractNo;

    @TableField("sign_date")
    private LocalDate signDate;

    @TableField("start_date")
    private LocalDate startDate;

    @TableField("end_date")
    private LocalDate endDate;

    @TableField("charge_second_fee_date")
    private LocalDate chargeSecondFeeDate;

    @TableField("busi_level")
    private String busiLevel;

    @TableField("environmental_testing_agency")
    private String environmentalTestingAgency;

    @TableField("cash_discount")
    private Integer cashDiscount;

    /**
     * 归属分公司
     */
    @TableField("org_id")
    private Long orgId;

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
