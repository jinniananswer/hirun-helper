package com.microtomato.hirun.modules.college.config.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class CollegeFixedExamRequestDTO {

    private Integer examMaxNum;

    private String examType;

    private Integer passScore;

    private List<CollegeTopicInfoRequestDTO> studyTopicTypeInfoDetails;

    private List<CollegeFixedExamTaskDTO> taskInfoList;

}
