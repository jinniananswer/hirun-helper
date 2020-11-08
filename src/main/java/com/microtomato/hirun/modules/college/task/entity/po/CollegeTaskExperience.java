package com.microtomato.hirun.modules.college.task.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * (CollegeTaskExperience)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-11-04 03:51:32
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_task_experience")
public class CollegeTaskExperience extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "task_experience_id", type = IdType.AUTO)
    private Long taskExperienceId;

    /**
     * 任务标识
     */
    @TableField(value = "task_id")
    private String taskId;

    /**
     * 心得内容
     */
    @TableField(value = "experience_content")
    private String experienceContent;

    /**
     * 心得类型 0-文字，1-图片
     */
    @TableField(value = "experience_type")
    private String experienceType;

    /**
     * 描述类型:0-文字描述 1-图片描述
     */
    @TableField(value = "desc_type")
    private String descType;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}