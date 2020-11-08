package com.microtomato.hirun.modules.college.topic.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.microtomato.hirun.modules.college.topic.entity.po.ExamTopicOption;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author huanghua
 * @date 2020-09-05
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class TopicServiceDTO {

    private Long topicId;

    private String name;

    private String type;

    private String correctAnswer;

    private Integer score;

    private String status;

    private String answer;

    private Long topicNum;

    private List<String> labelIds;

    @Builder.Default
    private Boolean isAnswer = Boolean.FALSE;

    private List<TopicOptionServiceDTO> topicOptions;
}
