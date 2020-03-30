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
 * 非主营支付项明细表
 * </p>
 *
 * @author sunxin
 * @since 2020-03-30
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class NormalPayItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 付款编码
     */
    @TableField("pay_no")
    private Long payNo;

    /**
     * 收款项编码，见参数sys_collection_item_cfg
     */
    @TableField("pay_item_id")
    private Long payItemId;

    /**
     * 上级费用项编码，见参数sys_collection_item_cfg
     */
    @TableField("parent_pay_item_id")
    private Long parentPayItemId;

    /**
     * 项目id，包含人员，地州，品牌等，需分别转换
     */
    @TableField("project")
    private Long project;

    /**
     * 金额
     */
    @TableField("fee")
    private Long fee;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 开始时间
     */
    @TableField("start_date")
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    @TableField("end_date")
    private LocalDateTime endDate;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
