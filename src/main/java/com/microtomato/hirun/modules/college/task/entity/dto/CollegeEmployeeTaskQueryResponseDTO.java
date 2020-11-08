package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

@Data
public class CollegeEmployeeTaskQueryResponseDTO {

    private String employeeName;

    private String employeeId;

    private String orgName;

    private String jobRoleName;

    private String mobileNo;

    private String sex;

    private Integer taskNum;

    private Integer finishNum;

    private Long argScore;

    private String evaluate;
}
