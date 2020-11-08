package com.microtomato.hirun.modules.college.task.entity.dto;


import lombok.Data;

@Data
public class CollegeEmployeeTaskScoreDTO {

    /**
     * 当前任务评分在员工排名
     */
    private Integer employeeTaskScoreRanking;

    /**
     * 当前任务评分超过多少员工百分比
     */
    private String employeeTaskScoreCxceedPercentage;

    /**
     * 当前练习评分在员工排名
     */
    private Integer employeeExercisesScoreRanking;

    /**
     * 当前练习评分超过多少员工百分比
     */
    private String employeeExercisesScoreCxceedPercentage;

    /**
     * 当前学习评分在员工排名
     */
    private Integer employeeStudyScoreRanking;

    /**
     * 当前学习评分超过多少员工百分比
     */
    private String employeeStudyScoreCxceedPercentage;

    /**
     * 当前考试评分在员工排名
     */
    private Integer employeeExamScoreRanking;

    /**
     * 当前考试评分超过多少员工百分比
     */
    private String employeeExamScoreCxceedPercentage;
}
