package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollegeTaskStudyContentResponseDTO {

    private String fileId;

    private String fileUrl;

    private String fileName;
}
