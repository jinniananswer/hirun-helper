package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTaskEmployeeCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeTaskEmployeeCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeTaskEmployeeCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (CollegeTaskEmployeeCfg)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-11-07 22:36:23
 */
@Service("collegeTaskEmployeeCfgService")
@DataSource(DataSourceKey.SYS)
public class CollegeTaskEmployeeCfgServiceImpl extends ServiceImpl<CollegeTaskEmployeeCfgMapper, CollegeTaskEmployeeCfg> implements ICollegeTaskEmployeeCfgService {

    @Autowired
    private CollegeTaskEmployeeCfgMapper collegeTaskEmployeeCfgMapper;


    @Override
    public List<CollegeTaskEmployeeCfg> queryEffectiveByTaskId(String taskId) {
        return this.list(Wrappers.<CollegeTaskEmployeeCfg>lambdaQuery()
                .eq(CollegeTaskEmployeeCfg::getTaskId, taskId)
                .eq(CollegeTaskEmployeeCfg::getStatus, "0"));
    }
}