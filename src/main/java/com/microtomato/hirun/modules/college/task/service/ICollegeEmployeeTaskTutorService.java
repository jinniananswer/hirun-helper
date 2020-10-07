package com.microtomato.hirun.modules.college.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTaskTutor;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * (CollegeEmployeeTaskTutor)表服务接口
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-04 22:53:05
 */
public interface ICollegeEmployeeTaskTutorService extends IService<CollegeEmployeeTaskTutor> {

    List<CollegeEmployeeTaskTutor> queryEffectiveByTaskId(String taskId);

    void addByTaskIdAndSelectTutor(String taskId, String selectTutor);
}