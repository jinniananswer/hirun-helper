package com.microtomato.hirun.modules.bss.order.entity.po;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * (OrderInspect)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-08-11 18:07:44
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_inspect")
public class OrderInspect extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField(value = "order_id")
    private Long orderId;

    /** 申报时间 */
    @TableField(value = "apply_date")
    private LocalDate applyDate;

    /** 机构 */
    @TableField(value = "institution")
    private String institution;

    /** 送达日期 */
    @TableField(value = "offer_date")
    private LocalDate offerDate;

    /** 检测状态 */
    @TableField(value = "check_status")
    private String checkStatus;

    /** 领取状态 */
    @TableField(value = "receive_status")
    private String receiveStatus;

    /** 领取人 */
    @TableField(value = "receive_people")
    private String receivePeople;

    /** 领取时间 */
    @TableField(value = "receive_date")
    private LocalDate receiveDate;

    /** 领取情况 */
    @TableField(value = "receive_remark")
    private String receiveRemark;

    /** 通知情况 */
    @TableField(value = "notice_remark")
    private String noticeRemark;

    /** 备注 */
    @TableField(value = "remark")
    private String remark;

    /** 保修开始时间 */
    @TableField(value = "guarantee_start_date")
    private LocalDate guaranteeStartDate;

    /** 保修结束时间 */
    @TableField(value = "guarantee_end_date")
    private LocalDate guaranteeEndDate;

    /** 是否保修 */
    @TableField(value = "is_guarantee")
    private String isGuarantee;

    /** 结算状态 */
    @TableField(value = "settle_status")
    private String settleStatus;

    /** 检测结算时间 */
    @TableField(value = "check_settle_date")
    private LocalDate checkSettleDate;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}