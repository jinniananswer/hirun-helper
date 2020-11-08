package com.microtomato.hirun.modules.college.topic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.topic.mapper.ExamTopicOptionMapper;
import com.microtomato.hirun.modules.college.topic.entity.po.ExamTopicOption;
import com.microtomato.hirun.modules.college.topic.service.IExamTopicOptionService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * (ExamTopicOption)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-04 11:39:06
 */
@Service("examTopicOptionService")
public class ExamTopicOptionServiceImpl extends ServiceImpl<ExamTopicOptionMapper, ExamTopicOption> implements IExamTopicOptionService {

    @Autowired
    private ExamTopicOptionMapper examTopicOptionMapper;

    @Override
    public List<ExamTopicOption> queryByTopicId(Long topicId) {
        return this.list(new QueryWrapper<ExamTopicOption>().lambda()
                .eq(ExamTopicOption::getTopicId, topicId)
                .eq(ExamTopicOption::getStatus, "0"));
    }

    @Override
    public boolean saveBatch(List<ExamTopicOption> examTopics) {
        return super.saveBatch(examTopics);
    }

    @Override
    public boolean save(ExamTopicOption examTopicOption) {
        return super.save(examTopicOption);
    }
}