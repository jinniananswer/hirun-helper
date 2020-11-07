package com.microtomato.hirun.modules.college.knowhow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestionReplyerCfg;

import java.util.List;

/**
 * (CollegeQuestionReplyerCfg)表服务接口
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-11-07 13:31:08
 */
public interface ICollegeQuestionReplyerCfgService extends IService<CollegeQuestionReplyerCfg> {

    List<Long> queryTeacherByType(String questionType);
}