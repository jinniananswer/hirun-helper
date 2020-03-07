package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeDeserializer;
import com.microtomato.hirun.framework.config.JsonLocalDateTimeSerializer;
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
 * 
 * </p>
 *
 * @author liuhui
 * @since 2020-03-07
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderSettlement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_id")
    private Long orderId;

    /**
     * 推迟原因
     */
    @TableField("defer_reason")
    private String deferReason;

    /**
     * 预计验收时间
     */
    @TableField("expect_check_date")
    private LocalDate expectCheckDate;

    /**
     * 预计尾款时间
     */
    @TableField("expect_lastfee_date")
    private LocalDate expectLastfeeDate;

    /**
     * 具体原因
     */
    @TableField("detail_reason")
    private String detailReason;

    /**
     * 时间验收时间
     */
    @TableField("actual_check_date")
    private LocalDate actualCheckDate;

    /**
     * 验收员工
     */
    @TableField("check_employee_id")
    private String checkEmployeeId;

    /**
     * 合同原金额
     */
    @TableField("source_contract_fee")
    private String sourceContractFee;

    /**
     * 变更项目金额
     */
    @TableField("changed_item_fee")
    private String changedItemFee;

    /**
     * 变更后合同金额
     */
    @TableField("changed_contract_fee")
    private String changedContractFee;

    /**
     * 管理员确认时间
     */
    @TableField("admin_check_date")
    private LocalDate adminCheckDate;

    /**
     * 已付金额
     */
    @TableField("payed_money")
    private Long payedMoney;

    /**
     * 结算后应付金额
     */
    @TableField("need_pay_money")
    private String needPayMoney;

    @TableField("org_id")
    private Long orgId;

    @TableField("remark")
    private String remark;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)

    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;


}
