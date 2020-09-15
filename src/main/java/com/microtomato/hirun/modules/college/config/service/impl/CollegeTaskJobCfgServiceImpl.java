package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTaskJobCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeTaskJobCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeTaskJobCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (CollegeTaskJobCfg)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-16 03:19:45
 */
@Service("collegeTaskJobCfgService")
public class CollegeTaskJobCfgServiceImpl extends ServiceImpl<CollegeTaskJobCfgMapper, CollegeTaskJobCfg> implements ICollegeTaskJobCfgService {

    @Autowired
    private CollegeTaskJobCfgMapper collegeTaskJobCfgMapper;


}