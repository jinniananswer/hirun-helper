package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.config.mapper.CollegeTopicLabelCfgMapper;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTopicLabelCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeTopicLabelCfgService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * (CollegeTopicLabelCfg)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-20 01:28:12
 */
@Service("collegeTopicLabelCfgService")
public class CollegeTopicLabelCfgServiceImpl extends ServiceImpl<CollegeTopicLabelCfgMapper, CollegeTopicLabelCfg> implements ICollegeTopicLabelCfgService {

    @Autowired
    private CollegeTopicLabelCfgMapper collegeTopicLabelCfgMapper;
    

}