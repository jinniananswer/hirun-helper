package com.microtomato.hirun.modules.college.task.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class CollegeTaskExamDetailResponseDTO {

    private Integer maxExamNum;

    private Integer currentExamNum;

    private String examDesc;

    private Boolean topicFlag;

    private List<ExamTopicResponseDTO> examTopicList;
}
