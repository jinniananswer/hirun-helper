package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ：mmzs
 * @date ：Created in 2020/2/4 21:12
 * @description：量房信息
 * @modified By：
 * @version: 1$
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderMeasureHouse extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 量房面积
     */
    @TableField("measure_area")
    private String measureArea;

    /**
     * 量房时间
     */
    @TableField(value = "measure_time")
    private LocalDateTime measureTime;

    /**
     * 客户意见
     */
    @TableField("customer_comments")
    private String customerComments;

    /**
     * 助理设计师
     */
    @TableField("assistant_designer")
    private String assistantDesigner;

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
