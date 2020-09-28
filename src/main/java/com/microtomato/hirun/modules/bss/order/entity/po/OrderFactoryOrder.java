package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * (OrderFactoryOrder)表实体类
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
@TableName("order_factory_order")
public class OrderFactoryOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 订单ID */
    @TableField(value = "order_id")
    private Long orderId;

    /** 生产批号 */
    @TableField(value = "produce_no")
    private String produceNo;

    /** 驻点人员 */
    @TableField(value = "garrison_name")
    private String garrisonName;

    /** 接收图纸日期 */
    @TableField(value = "receive_picture_date")
    private LocalDate receivePictureDate;

    /** 接收预算日期 */
    @TableField(value = "receive_budget_date")
    private LocalDate receiveBudgetDate;

    /** 接收尺寸表日期 */
    @TableField(value = "receive_size_date")
    private LocalDate receiveSizeDate;

    /** 驻点收到日期 */
    @TableField(value = "receive_garrison_date")
    private LocalDate receiveGarrisonDate;

    /** 订单正本日期 */
    @TableField(value = "order_original_date")
    private LocalDate orderOriginalDate;

    /** 收到订单正本日期 */
    @TableField(value = "receive_order_original_date")
    private LocalDate receiveOrderOriginalDate;

    /** 签单项目 */
    @TableField(value = "sign_item")
    private String signItem;

    /** 产品描述 */
    @TableField(value = "product_description")
    private String productDescription;

    /** 装修验收日期 */
    @TableField(value = "check_date")
    private LocalDate checkDate;

    /** 交付日期 */
    @TableField(value = "deliver_date")
    private LocalDate deliverDate;

    /** 木门预计日期 */
    @TableField(value = "door_expected_date")
    private LocalDate doorExpectedDate;

    /** 木门实际到货日期 */
    @TableField(value = "door_actual_date")
    private LocalDate doorActualDate;

    /** 实际入库日期 */
    @TableField(value = "storage_date")
    private LocalDate storageDate;

    /** 家具预计日期 */
    @TableField(value = "furniture_expected_date")
    private LocalDate furnitureExpectedDate;

    /** 家具实际到货日期 */
    @TableField(value = "furniture_actual_date")
    private LocalDate furnitureActualDate;

    /** 安装日期 */
    @TableField(value = "setup_date")
    private LocalDate setupDate;

    /** 跟踪情况 */
    @TableField(value = "follow_info")
    private String followInfo;

    /** 工厂进度 */
    @TableField(value = "factory_schedule")
    private String factorySchedule;

    /** 返工及变更 */
    @TableField(value = "back_change")
    private String backChange;

    /** 责任方 */
    @TableField(value = "responsible")
    private String responsible;

    /** 不做木制品 */
    @TableField(value = "no_wood")
    private String noWood;

    /** 跟单完毕 */
    @TableField(value = "follow_finish")
    private String followFinish;

    /** 驻点重新接单日期 */
    @TableField(value = "regarrison_date")
    private LocalDate regarrisonDate;

    /** 驻点核尺日期 */
    @TableField(value = "garrison_check_size_date")
    private LocalDate garrisonCheckSizeDate;

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

    /** 状态 0-正常 1-关闭 */
    @TableField(value = "status")
    private String status;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}