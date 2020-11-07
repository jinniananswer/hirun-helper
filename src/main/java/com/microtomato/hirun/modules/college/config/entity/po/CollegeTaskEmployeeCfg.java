package com.microtomato.hirun.modules.college.config.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * (CollegeTaskEmployeeCfg)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-11-07 22:36:22
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_task_employee_cfg")
public class CollegeTaskEmployeeCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "task_employee_id", type = IdType.AUTO)
    private Long taskEmployeeId;

    /**
     * 任务标识
     */
    @TableField(value = "task_id")
    private String taskId;

    /**
     * 指定员工标识
     */
    @TableField(value = "employee_id")
    private String employeeId;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

}