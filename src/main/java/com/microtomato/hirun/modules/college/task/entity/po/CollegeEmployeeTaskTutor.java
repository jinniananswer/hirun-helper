package com.microtomato.hirun.modules.college.task.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * (CollegeEmployeeTaskTutor)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-04 22:57:43
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_employee_task_tutor")
public class CollegeEmployeeTaskTutor extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "task_tutor_id", type = IdType.AUTO)
    private Long taskTutorId;

    /**
     * 员工任务标识
     */
    @TableField(value = "task_id")
    private String taskId;

    /**
     * 导师标识
     */
    @TableField(value = "tutor_id")
    private String tutorId;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

}