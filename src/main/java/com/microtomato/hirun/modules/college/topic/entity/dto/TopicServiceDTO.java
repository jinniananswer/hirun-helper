package com.microtomato.hirun.modules.college.topic.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.microtomato.hirun.modules.college.topic.entity.po.ExamTopicOption;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author huanghua
 * @date 2020-09-05
 */
@Data
@NoArgsConstructor
public class TopicServiceDTO {

    private Long topicId;

    private String name;

    private String type;

    private String correctAnswer;

    private Integer score;

    private String status;

    private List<ExamTopicOption> topicOptions;
}
