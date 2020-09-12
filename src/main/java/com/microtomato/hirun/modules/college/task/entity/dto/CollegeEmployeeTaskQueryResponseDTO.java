package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

@Data
public class CollegeEmployeeTaskQueryResponseDTO {

    private String employeeName;

    private String employeeId;

    private Integer taskNum;

    private Integer studyNum;
}
