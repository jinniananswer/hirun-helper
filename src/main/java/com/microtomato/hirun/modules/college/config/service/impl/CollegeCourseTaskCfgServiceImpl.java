package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseTaskCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeCourseTaskCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseTaskCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (CollegeCourseTaskCfg)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-04 00:19:05
 */
@Service("collegeCourseTaskCfgService")
public class CollegeCourseTaskCfgServiceImpl extends ServiceImpl<CollegeCourseTaskCfgMapper, CollegeCourseTaskCfg> implements ICollegeCourseTaskCfgService {

    @Autowired
    private CollegeCourseTaskCfgMapper collegeCourseTaskCfgMapper;


    @Override
    public List<CollegeCourseTaskCfg> queryByTaskType(String taskType) {
        return this.list(Wrappers.<CollegeCourseTaskCfg>lambdaQuery()
                .eq(CollegeCourseTaskCfg::getTaskType, taskType).eq(CollegeCourseTaskCfg::getStatus, '0')
                .orderByAsc(CollegeCourseTaskCfg::getCourseStudyOrder));
    }
}