package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CollegeEmployeeTaskTypeFinishDetailsRequestDTO {

    private List<CollegeEmployeeTaskDetailResponseDTO> courseTaskList;

    private List<CollegeEmployeeTaskDetailResponseDTO> coursewareTaskList;

    private List<CollegeEmployeeTaskDetailResponseDTO> practiceTaskList;
}
