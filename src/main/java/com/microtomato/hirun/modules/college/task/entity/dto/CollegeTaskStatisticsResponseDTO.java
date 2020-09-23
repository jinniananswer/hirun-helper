package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollegeTaskStatisticsResponseDTO {

    /**
     * 任务完成百分比
     */
    private Integer taskFinish;

    /**
     * 任务完成数量
     */
    private Integer taskFinishNum;

    /**
     * 习题完成百分比
     */
    private Integer exercisesFinish;

    /**
     * 习题完成数量
     */
    private Integer exercisesFinishNum;

    /**
     * 全部任务数量
     */
    private Integer allTaskNum;

    /**
     * 考试通过百分比
     */
    private Integer examPass;

    /**
     * 考试通过数量
     */
    private Integer examPassNum;

    /**
     * 所有评分任务平均分
     */
    private Double argTaskScore;

    /**
     * 所有练习评分平均分
     */
    private Double argExercisesScore;

    /**
     * 所有学习评分平均分
     */
    private Double argStudyScore;

    /**
     * 所有考试评分平均分
     */
    private Double argExamScore;

    private CollegeEmployeeTaskScoreDTO allEmployeeTaskScoreInfo;

    private CollegeEmployeeTaskScoreDTO orgEmployeeTaskScoreInfo;




}
