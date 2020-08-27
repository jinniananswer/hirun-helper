package com.microtomato.hirun.modules.college.config.entity.dto;

import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import lombok.Data;

import java.util.List;

@Data
public class CollegeCourseChaptersTaskRequestDTO {

    private String taskType;

    private String studyId;

    private String studyName;

    private String studyType;

    private String studyOrder;

    private String staffRank;

    private String studyTime;

    private List<CollegeCourseChaptersCfg> courseChaptersList;
}
