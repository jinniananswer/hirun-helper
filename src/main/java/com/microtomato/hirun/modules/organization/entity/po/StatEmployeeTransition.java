package com.microtomato.hirun.modules.organization.entity.po;

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
 * 
 * </p>
 *
 * @author liuhui
 * @since 2019-12-22
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class StatEmployeeTransition extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 年份
     */
    @TableField("year")
    private String year;

    /**
     * 月份
     */
    @TableField("month")
    private String month;

    @TableField("org_id")
    private Long orgId;

    @TableField("org_nature")
    private String orgNature;

    @TableField("job_role")
    private String jobRole;

    @TableField("job_role_nature")
    private String jobRoleNature;

    @TableField("org_city")
    private String orgCity;

    @TableField("org_type")
    private String orgType;

    @TableField("job_grade")
    private String jobGrade;

    @TableField("employee_entry_quantity")
    private Float employeeEntryQuantity;

    /**
     * 新入职员工id集合
     */
    @TableField("entry_employee_id")
    private String entryEmployeeId;

    @TableField("employee_destroy_quantity")
    private Float employeeDestroyQuantity;

    /**
     * 离职employee_id集合
     */
    @TableField("destroy_employee_id")
    private String destroyEmployeeId;

    @TableField("employee_holiday_quantity")
    private Float employeeHolidayQuantity;

    /**
     * 休假员工集合
     */
    @TableField("holiday_employee_id")
    private String holidayEmployeeId;

    /**
     * 员工调动-转入
     */
    @TableField("employee_trans_in_quantity")
    private Float employeeTransInQuantity;

    /**
     * 员工转入id集合
     */
    @TableField("trans_in_employee_id")
    private String transInEmployeeId;

    /**
     * 员工调动（转出）
     */
    @TableField("employee_trans_out_quantity")
    private Float employeeTransOutQuantity;

    /**
     * 员工转出id集合
     */
    @TableField("trans_out_employee_id")
    private String transOutEmployeeId;

    /**
     * 员工借调转入
     */
    @TableField("employee_borrow_in_quantity")
    private Float employeeBorrowInQuantity;

    /**
     * 员工借入id集合
     */
    @TableField("borrow_in_employee_id")
    private String borrowInEmployeeId;

    /**
     * 员工借调转出
     */
    @TableField("employee_borrow_out_quantity")
    private Float employeeBorrowOutQuantity;

    /**
     * 员工借出id集合
     */
    @TableField("borrow_out_employee_id")
    private String borrowOutEmployeeId;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
