package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CollegeLoginTaskInfoResponseDTO {

    private String employeeId;

    private List<CollegeEmployeeTaskDetailResponseDTO> todayTaskList;

    private List<CollegeEmployeeTaskDetailResponseDTO> tomorrowTaskList;

    private List<CollegeEmployeeTaskDetailResponseDTO> finishTaskList;
}
