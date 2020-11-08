package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CollegeTaskExperienceRequestDTO {

    private Long taskId;

    private String experience;

    private String experienceImgFileId;

    private String taskImgFileId;
}
