package com.microtomato.hirun.modules.college.topic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.topic.entity.dto.TopicOptionServiceDTO;
import com.microtomato.hirun.modules.college.topic.entity.dto.TopicServiceDTO;
import com.microtomato.hirun.modules.college.topic.entity.po.ExamTopicOption;
import com.microtomato.hirun.modules.college.topic.mapper.ExamTopicMapper;
import com.microtomato.hirun.modules.college.topic.entity.po.ExamTopic;
import com.microtomato.hirun.modules.college.topic.service.IExamTopicOptionService;
import com.microtomato.hirun.modules.college.topic.service.IExamTopicService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * (ExamTopic)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-04 11:39:06
 */
@Service("examTopicService")
public class ExamTopicServiceImpl extends ServiceImpl<ExamTopicMapper, ExamTopic> implements IExamTopicService {

    @Autowired
    private ExamTopicMapper examTopicMapper;

    @Autowired
    private IExamTopicOptionService examTopicOptionService;

    @Override
    public boolean save(ExamTopic examTopic) {
        return super.save(examTopic);
    }

    @Override
    public IPage<TopicServiceDTO> init(Page<ExamTopic> page) {

        Page<ExamTopic> pages = examTopicMapper.selectPage(page, new QueryWrapper<ExamTopic>().lambda()
                .eq(ExamTopic::getStatus, "0"));

        IPage<TopicServiceDTO> topics = new Page<>(page.getCurrent(), page.getSize());
        topics.setTotal(pages.getTotal());
        topics.setRecords(getTopicInfo(pages.getRecords()));
        return topics;
    }

    private List<TopicServiceDTO> getTopicInfo(List<ExamTopic> list) {
        List<TopicServiceDTO> topicList = new ArrayList<>();
        for (ExamTopic examTopic : list) {
            TopicServiceDTO topic = new TopicServiceDTO();
            BeanUtils.copyProperties(examTopic, topic);
            List<ExamTopicOption> examTopicOptions = examTopicOptionService.queryByTopicId(topic.getTopicId());
            List<TopicOptionServiceDTO> topicOptionServices = new ArrayList<>();
            examTopicOptions.stream().forEach(option -> {
                TopicOptionServiceDTO topicOptionService = new TopicOptionServiceDTO();
                BeanUtils.copyProperties(option, topicOptionService);
                topicOptionServices.add(topicOptionService);
            });
            topic.setTopicOptions(topicOptionServices);
            topicList.add(topic);
        }

        return topicList;
    }

    @Override
    public IPage<TopicServiceDTO> queryByCond(String topicText, Long examId, String type, Page<ExamTopic> page) {
        Page<ExamTopic> pages = examTopicMapper.selectPage(page, new QueryWrapper<ExamTopic>().lambda()
                .eq(null != examId, ExamTopic::getExamId, examId)
                .eq(ExamTopic::getStatus, "0")
                .like(StringUtils.isNotEmpty(topicText), ExamTopic::getName, topicText)
                .eq(StringUtils.isNotEmpty(type), ExamTopic::getType, type));
        List<ExamTopic> list = pages.getRecords();

        IPage<TopicServiceDTO> topics = new Page<>(page.getCurrent(), page.getSize());
        topics.setTotal(pages.getTotal());
        topics.setRecords(getTopicInfo(list));
        return topics;
    }
}