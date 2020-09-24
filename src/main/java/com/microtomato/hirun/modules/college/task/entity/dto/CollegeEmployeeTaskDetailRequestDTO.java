package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollegeEmployeeTaskDetailRequestDTO {

    private String employeeId;

    private String studyType;

    private Integer page;

    private Integer limit;

    private Integer count;
}
