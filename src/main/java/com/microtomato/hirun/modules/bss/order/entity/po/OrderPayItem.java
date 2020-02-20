package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 订单支付项明细表
 * </p>
 *
 * @author jinnian
 * @since 2020-02-21
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderPayItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_id")
    private Long orderId;

    /**
     * 付款编码
     */
    @TableField("pay_no")
    private Long payNo;

    /**
     * 收款项编码，见参数sys_pay_item_cfg
     */
    @TableField("pay_item_id")
    private Long payItemId;

    /**
     * 上级费用项编码，见参数sys_pay_item_cfg
     */
    @TableField("parent_pay_item_id")
    private Long parentPayItemId;

    /**
     * 期数
     */
    @TableField("periods")
    private Integer periods;

    /**
     * 0-未审核 1-审核通过 2-审核不通过
     */
    @TableField("audit_status")
    private String auditStatus;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 处理员工
     */
    @TableField("pay_employee_id")
    private Long payEmployeeId;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
