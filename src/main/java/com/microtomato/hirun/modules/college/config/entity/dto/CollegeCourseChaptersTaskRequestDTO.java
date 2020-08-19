package com.microtomato.hirun.modules.college.config.entity.dto;

import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import lombok.Data;

import java.util.List;

@Data
public class CollegeCourseChaptersTaskRequestDTO {

    private String taskType;

    private String courseId;

    private String courseName;

    private String courseType;

    private String courseStudyOrder;

    private String staffRank;

    private String studyTime;

    private List<CollegeCourseChaptersCfg> courseChaptersList;
}
