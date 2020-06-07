package com.microtomato.hirun.modules.bss.order.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * (OrderWorkerAction)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-06-07 15:16:56
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_worker_action")
public class OrderWorkerAction extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField(value = "order_id")
    private Long orderId;

    /** order_worker表的主键 */
    @TableField(value = "worker_id")
    private Long workerId;

    /**
     * 参与的订单状态
     */
    @TableField("order_status")
    private String orderStatus;

    /**
     * 动作发生时的当前归属部门
     */
    @TableField("org_id")
    private Long orgId;

    /**
     * 动作发生时的当前岗位
     */
    @TableField("job_role")
    private String jobRole;

    /**
     * 动作发生时的当前岗位
     */
    @TableField("job_grade")
    private String jobGrade;


    /** 动作编码，参见sys_static_data:ORDER_WORKER_ACTION */
    @TableField(value = "action")
    private String action;

    /** 开始时间 */
    @TableField(value = "start_date")
    private LocalDateTime startDate;

    /** 结束时间 */
    @TableField(value = "end_date")
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