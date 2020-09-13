package com.microtomato.hirun.modules.college.task.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * (CollegeTaskDeferApply)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-13 14:01:01
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_task_defer_apply")
public class CollegeTaskDeferApplyRequestDTO extends BaseEntity {


    private Long taskDeferId;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 学习结束时间
     */
    private LocalDateTime studyEndDate;

    /**
     * 审批状态
     */
    private LocalDateTime approvalStatus;

    private String employeeName;

    private String studyName;

    private String chaptersName;

}