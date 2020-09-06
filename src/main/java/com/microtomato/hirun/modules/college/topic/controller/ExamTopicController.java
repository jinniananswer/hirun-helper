package com.microtomato.hirun.modules.college.topic.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestionRela;
import com.microtomato.hirun.modules.college.topic.entity.dto.TopicServiceDTO;
import com.microtomato.hirun.modules.college.topic.entity.po.ExamTopic;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.college.topic.service.IExamTopicService;

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

    @GetMapping("init")
    @RestResult
    public IPage<TopicServiceDTO> init(Page<ExamTopic> page) {
        return examTopicService.init(page);
    }


}