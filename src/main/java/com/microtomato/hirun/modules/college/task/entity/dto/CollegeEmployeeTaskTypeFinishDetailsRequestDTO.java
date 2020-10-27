package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CollegeEmployeeTaskTypeFinishDetailsRequestDTO {

    private String taskType;

    private List<CollegeEmployeeTaskDetailResponseDTO> taskList;
}
