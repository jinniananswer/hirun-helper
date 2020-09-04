package com.microtomato.hirun.modules.bss.salary.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工补扣款信息表(SalaryRedebit)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-08-30 20:34:51
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("salary_redebit")
public class SalaryRedebit extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField(value = "employee_id")
    private Long employeeId;

    /** 补扣款类型 */
    @TableField(value = "redebit_item")
    private String redebitItem;

    /** 工资项目 */
    @TableField(value = "salary_item")
    private String salaryItem;

    /** 费用 */
    @TableField(value = "money")
    private Long money;

    /** 工资月份 */
    @TableField(value = "salary_month")
    private Integer salaryMonth;

    /** 录入员工 */
    @TableField(value = "in_employee_id")
    private Long inEmployeeId;

    /** 录入日期 */
    @TableField(value = "in_date")
    private LocalDate inDate;

    /** 审核员工 */
    @TableField(value = "audit_employee_id")
    private Long auditEmployeeId;

    /** 审核状态，审核状态， 0-已保存 1-待审核 2-审核通过 3-审核不通过  */
    @TableField(value = "audit_status")
    private String auditStatus;

    /** 审核时间 */
    @TableField(value = "audit_time")
    private LocalDateTime auditTime;

    /** 原因说明 */
    @TableField(value = "reason")
    private String reason;

    /** 开始时间 */
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    /** 结束时间 */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /** 税金，单位分 */
    @TableField(value = "create_employee_id")
    private Long createEmployeeId;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}