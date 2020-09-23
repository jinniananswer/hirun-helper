package com.microtomato.hirun.modules.college.task.service;

import com.microtomato.hirun.modules.organization.entity.po.Employee;

import java.util.List;

public interface ITaskDomainOpenService {
    /**
     * 批量给新员工发布固定任务
     * @param employeeIdList
     */
    void fixedTaskReleaseByEmployeeList(List<Long> employeeIdList);

    void fixedTaskReleaseByTaskList(List<Long> studyTaskIdList);

    void taskReleaseByTaskList(List<Long> studyTaskIdList);
}
