package com.microtomato.hirun.modules.college.task.entity.dto;


import com.microtomato.hirun.modules.college.task.entity.po.CollegeTaskExperience;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CollegeTaskExperienceRequetDTO {

    private String writtenExperience;

    private List<CollegeTaskExperienceImgResponseDTO> imgExperienceList;
}
