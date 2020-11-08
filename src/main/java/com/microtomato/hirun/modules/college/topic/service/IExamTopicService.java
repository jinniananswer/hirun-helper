package com.microtomato.hirun.modules.college.topic.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.topic.entity.dto.TopicServiceDTO;
import com.microtomato.hirun.modules.college.topic.entity.po.ExamTopic;

import java.util.List;

/**
 * (ExamTopic)表服务接口
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-04 11:39:06
 */
public interface IExamTopicService extends IService<ExamTopic> {

    IPage<TopicServiceDTO> init(Page<ExamTopic> page);

    IPage<TopicServiceDTO> queryByCond(String topicText, Long examId, String type, Page<ExamTopic> page);

    List<TopicServiceDTO> queryByTopicIds(List<Long> topicIds);
}