package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * (OrderFactoryOrderFollow)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-09-26 16:32:41
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_factory_order_follow")
public class OrderFactoryOrderFollow extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 工厂订单ID */
    @TableField(value = "factory_order_id")
    private Long factoryOrderId;

    /** 跟单时间 */
    @TableField(value = "follow_date")
    private LocalDate followDate;

    /** 性质 */
    @TableField(value = "nature")
    private String nature;

    /** 内容 */
    @TableField(value = "content")
    private String content;

    /** 责任人 */
    @TableField(value = "responsible")
    private String responsible;

    /** 下单日期 */
    @TableField(value = "order_date")
    private LocalDate orderDate;

    /** 下单人 */
    @TableField(value = "order_man")
    private String orderMan;

    /** 送货日期 */
    @TableField(value = "deliver_date")
    private LocalDate deliverDate;

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