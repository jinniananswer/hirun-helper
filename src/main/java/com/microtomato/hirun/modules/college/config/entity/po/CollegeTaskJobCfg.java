package com.microtomato.hirun.modules.college.config.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * (CollegeTaskJobCfg)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-16 03:19:45
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_task_job_cfg")
public class CollegeTaskJobCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "task_job_id", type = IdType.AUTO)
    private Long taskJobId;

    /**
     * 任务标识
     */
    @TableField(value = "task_id")
    private String taskId;

    /**
     * 工作岗位
     */
    @TableField(value = "job_role")
    private String jobRole;

    /**
     * 工作职级
     */
    @TableField(value = "job_grade")
    private String jobGrade;

    /**
     * 工作类型
     */
    @TableField(value = "job_type")
    private String jobType;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

}