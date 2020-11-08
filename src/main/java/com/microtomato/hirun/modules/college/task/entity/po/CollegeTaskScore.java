package com.microtomato.hirun.modules.college.task.entity.po;

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
 * (CollegeTaskScore)表实体类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-22 13:17:45
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("college_task_score")
public class CollegeTaskScore extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField(value = "task_id")
    private Long taskId;


    @TableField(value = "score_type")
    private String scoreType;


    @TableField(value = "score")
    private Integer score;


    @TableField(value = "time")
    private LocalDateTime time;

}