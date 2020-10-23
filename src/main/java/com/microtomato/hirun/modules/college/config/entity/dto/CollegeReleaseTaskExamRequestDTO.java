package com.microtomato.hirun.modules.college.config.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class CollegeReleaseTaskExamRequestDTO {

    private Integer examMaxNum;

    private String examType;

    private Integer passScore;

    private Integer examTime;

    private List<CollegeTopicInfoRequestDTO> studyTopicTypeInfoDetails;

    private List<CollegeReleaseExamTaskDTO> taskInfoList;

}
