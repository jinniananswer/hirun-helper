package com.microtomato.hirun.modules.college.topic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.topic.mapper.CollegeTopicLabelRelMapper;
import com.microtomato.hirun.modules.college.topic.entity.po.CollegeTopicLabelRel;
import com.microtomato.hirun.modules.college.topic.service.ICollegeTopicLabelRelService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * (CollegeTopicLabelRel)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-20 01:22:12
 */
@Service("collegeTopicLabelRelService")
public class CollegeTopicLabelRelServiceImpl extends ServiceImpl<CollegeTopicLabelRelMapper, CollegeTopicLabelRel> implements ICollegeTopicLabelRelService {

    @Autowired
    private CollegeTopicLabelRelMapper collegeTopicLabelRelMapper;
    

}