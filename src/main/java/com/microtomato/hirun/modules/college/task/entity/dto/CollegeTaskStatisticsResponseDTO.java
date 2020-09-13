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
     * 任务未完成百分比
     */
    private Integer taskUnFinish;

    /**
     * 任务未完成数量
     */
    private Integer taskUnFinishNum;

    /**
     * 习题完成百分比
     */
    private Integer exercisesFinish;

    /**
     * 习题完成数量
     */
    private Integer exercisesFinishNum;

    /**
     * 习题进行中百分比
     */
    private Integer exercisesUnderWay;

    /**
     * 习题进行中数量
     */
    private Integer exercisesUnderWayNum;

    /**
     * 习题未完成百分比
     */
    private Integer exercisesUnFinish;

    /**
     * 习题未完成数量
     */
    private Integer exercisesUnFinishNum;

    /**
     * 全部任务数量
     */
    private Integer allTaskNum;

    /**
     * 考试完成百分比
     */
    private Integer examFinish;

    /**
     * 考试完成数量
     */
    private Integer examFinishNum;

    /**
     * 考试未完成百分比
     */
    private Integer examUnFinish;

    /**
     * 考试未完成数量
     */
    private Integer examUnFinishNum;

    /**
     * 考试通过百分比
     */
    private Integer examPass;

    /**
     * 考试通过数量
     */
    private Integer examPassNum;

    /**
     * 考试未通过百分比
     */
    private Integer examUnPass;

    /**
     * 考试未通过数量
     */
    private Integer examUnPassNum;
}
