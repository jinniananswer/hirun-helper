package com.microtomato.hirun.modules.college.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.task.mapper.CollegeStudyTopicCfgMapper;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTopicCfg;
import com.microtomato.hirun.modules.college.task.service.ICollegeStudyTopicCfgService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * (CollegeStudyTopicCfg)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-09-16 01:58:14
 */
@Service("collegeStudyTopicCfgService")
@DataSource(DataSourceKey.SYS)
public class CollegeStudyTopicCfgServiceImpl extends ServiceImpl<CollegeStudyTopicCfgMapper, CollegeStudyTopicCfg> implements ICollegeStudyTopicCfgService {

    @Autowired
    private CollegeStudyTopicCfgMapper collegeStudyTopicCfgMapper;
    

}