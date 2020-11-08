package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTaskJobCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeTaskJobCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeTaskJobCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (CollegeTaskJobCfg)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-16 03:19:45
 */
@Service("collegeTaskJobCfgService")
@DataSource(DataSourceKey.SYS)
public class CollegeTaskJobCfgServiceImpl extends ServiceImpl<CollegeTaskJobCfgMapper, CollegeTaskJobCfg> implements ICollegeTaskJobCfgService {

    @Autowired
    private CollegeTaskJobCfgMapper collegeTaskJobCfgMapper;


    @Override
    public List<CollegeTaskJobCfg> queryEffectiveByTaskId(String taskId) {
        return this.list(Wrappers.<CollegeTaskJobCfg>lambdaQuery().eq(CollegeTaskJobCfg::getTaskId, taskId));
    }
}