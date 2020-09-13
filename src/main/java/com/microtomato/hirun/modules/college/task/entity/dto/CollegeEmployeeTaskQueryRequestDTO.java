package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollegeEmployeeTaskQueryRequestDTO {

    private String employeeName;

    private Long employeeId;

    private String taskType;

    private String studyId;

    private String studyName;

    private Integer page;

    private Integer limit;

    private Integer count;
}
