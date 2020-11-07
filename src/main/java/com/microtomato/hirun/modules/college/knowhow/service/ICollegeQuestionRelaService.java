package com.microtomato.hirun.modules.college.knowhow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestionRela;

import java.util.List;

/**
 * (CollegeQuestionRela)表服务接口
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-08-16 16:14:44
 */
public interface ICollegeQuestionRelaService extends IService<CollegeQuestionRela> {

    /**
     * 根据员工标识、关系类型获取关系数据
     * @param employeeId
     * @param relationType
     * @return
     */
    List<CollegeQuestionRela> queryByEmployeeIdAndRelaType(Long employeeId, String relationType);

    Long getEmployeeByQuestionId(Long questionId);
}