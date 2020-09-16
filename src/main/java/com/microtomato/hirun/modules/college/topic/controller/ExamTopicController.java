package com.microtomato.hirun.modules.college.topic.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.mybatis.sequence.impl.ExamTopicSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.knowhow.consts.KnowhowConsts;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestionRela;
import com.microtomato.hirun.modules.college.topic.entity.dto.TopicOptionServiceDTO;
import com.microtomato.hirun.modules.college.topic.entity.dto.TopicServiceDTO;
import com.microtomato.hirun.modules.college.topic.entity.po.ExamTopic;
import com.microtomato.hirun.modules.college.topic.entity.po.ExamTopicOption;
import com.microtomato.hirun.modules.college.topic.service.IExamTopicOptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.college.topic.service.IExamTopicService;

import java.util.ArrayList;
import java.util.List;

/**
 * (ExamTopic)表控制层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-04 11:39:06
 */
@RestController
@RequestMapping("/api/ExamTopic")
public class ExamTopicController {

    /**
     * 服务对象
     */
    @Autowired
    private IExamTopicService examTopicService;

    @Autowired
    private IExamTopicOptionService examTopicOptionService;

    @Autowired
    private IDualService dualService;

    @GetMapping("init")
    @RestResult
    public IPage<TopicServiceDTO> init(Page<ExamTopic> page) {
        return examTopicService.init(page);
    }

    @GetMapping("queryByCond")
    @RestResult
    public IPage<TopicServiceDTO> queryByCond(String topicText, Long examId, String type, Page<ExamTopic> page) {
        return examTopicService.queryByCond(topicText, examId, type, page);
    }

    @PostMapping("updateByTopic")
    @RestResult
    public void updateByTopic(@RequestBody ExamTopic topic) {
        this.examTopicService.updateById(topic);
    }

    @PostMapping("updateTopicOption")
    @RestResult
    public void updateTopicOption(@RequestBody ExamTopicOption option) {
        this.examTopicOptionService.updateById(option);
    }

    @PostMapping("deleteTopicBatch")
    @RestResult
    public void deleteTopicBatch(@RequestBody List<ExamTopic> topics) {
        List<Long> topicIds = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(topics)) {
            for (ExamTopic topic : topics) {
                topicIds.add(topic.getTopicId());
            }
            this.examTopicService.removeByIds(topicIds);
        }
    }

    @PostMapping("addTopic")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void addTopic(@RequestBody TopicServiceDTO topic) {
        ExamTopic examTopic = ExamTopic.builder().build();
        BeanUtils.copyProperties(topic, examTopic);
        examTopic.setStatus("0");

        Long topicId = dualService.nextval(ExamTopicSeq.class);
        examTopic.setTopicId(topicId);

        this.examTopicService.save(examTopic);

        List<TopicOptionServiceDTO> topicOptions = topic.getTopicOptions();
        topicOptions.stream().forEach(option -> {
            option.setTopicId(topicId);
            ExamTopicOption examTopicOption = new ExamTopicOption();
            BeanUtils.copyProperties(option, examTopicOption);
            this.examTopicOptionService.save(examTopicOption);
        });
        // 批量新增数据源不正确待处理
//        this.examTopicOptionService.saveBatch(topicOptions);
    }
}