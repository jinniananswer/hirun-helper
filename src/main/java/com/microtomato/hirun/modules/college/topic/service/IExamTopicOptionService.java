package com.microtomato.hirun.modules.college.topic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.topic.entity.po.ExamTopicOption;

import java.util.List;

/**
 * (ExamTopicOption)表服务接口
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-04 11:39:06
 */
public interface IExamTopicOptionService extends IService<ExamTopicOption> {

    /**
     * 根据题目标识获取选项内容
     * @param topicId
     * @return
     */
    List<ExamTopicOption> queryByTopicId(Long topicId);
}