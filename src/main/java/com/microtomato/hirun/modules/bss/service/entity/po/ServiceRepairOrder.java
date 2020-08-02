package com.microtomato.hirun.modules.bss.service.entity.po;

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
 * (ServiceRepairOrder)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("service_repair_order")
public class ServiceRepairOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;
    


    @TableField(value = "id")
    private Long id;


    @TableField(value = "repair_no")
    private String repairNo;


    @TableField(value = "customer_id")
    private Long customerId;

    /** 维修工种 */
    @TableField(value = "repair_worker_type")
    private String repairWorkerType;

    /** 是否收费 */
    @TableField(value = "is_fee")
    private String isFee;

    /** 维修项目 */
    @TableField(value = "repair_item")
    private String repairItem;

    /** 维修原因 */
    @TableField(value = "repair_reason")
    private String repairReason;

    /** 责任分摊 */
    @TableField(value = "duty_share")
    private String dutyShare;

    /** 维修材料 */
    @TableField(value = "repair_material")
    private String repairMaterial;

    /** 施工组长 */
    @TableField(value = "build_headman")
    private String buildHeadman;


    @TableField(value = "repair_worker_count")
    private String repairWorkerCount;

    /** 维修人员 */
    @TableField(value = "repair_worker")
    private String repairWorker;

    /** 受理时间 */
    @TableField(value = "accept_time")
    private LocalDateTime acceptTime;

    /** 取单时间 */
    @TableField(value = "offer_time")
    private LocalDate offerTime;

    /** 回单时间 */
    @TableField(value = "receipt_time")
    private LocalDate receiptTime;

    /** 预计维修时间 */
    @TableField(value = "plan_repair_date")
    private LocalDate planRepairDate;

    /** 维修实际开始时间 */
    @TableField(value = "actual_repair_start_date")
    private LocalDate actualRepairStartDate;

    /** 维修实际完成时间 */
    @TableField(value = "actual_repair_end_date")
    private LocalDate actualRepairEndDate;

    /** 维修回访 */
    @TableField(value = "repair_visit_content")
    private String repairVisitContent;


    @TableField(value = "repair_visit_time")
    private LocalDate repairVisitTime;

    /** 维修满意度 */
    @TableField(value = "repair_satisfaction")
    private String repairSatisfaction;


    @TableField(value = "repair_fee")
    private Integer repairFee;

    /** 收费奖励 */
    @TableField(value = "repair_award_fee")
    private Integer repairAwardFee;

    /** 维修材料费用 */
    @TableField(value = "repair_material_fee")
    private Integer repairMaterialFee;

    /** 维修工资 */
    @TableField(value = "repair_worker_salary")
    private Integer repairWorkerSalary;

    /** 工资开具时间 */
    @TableField(value = "actual_worker_salary_date")
    private LocalDate actualWorkerSalaryDate;


    @TableField(value = "remark")
    private String remark;


    @TableField(value = "service_employee_id")
    private Long serviceEmployeeId;


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