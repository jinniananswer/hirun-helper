package com.microtomato.hirun.modules.college.task.entity.dto;


import com.microtomato.hirun.modules.college.task.entity.po.CollegeTaskExperience;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CollegeTaskExperienceScoreResponseDTO {

    private String writtenExperience;

    private List<String> imgExperienceList;

    private List<String> experienceDescImgList;

    private Integer taskScore;

    private Integer experienceScore;

    private Integer imgScore;
}
