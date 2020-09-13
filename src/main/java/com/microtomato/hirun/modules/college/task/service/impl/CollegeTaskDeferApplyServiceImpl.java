package com.microtomato.hirun.modules.college.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeTaskDeferApply;
import com.microtomato.hirun.modules.college.task.mapper.CollegeTaskDeferApplyMapper;
import com.microtomato.hirun.modules.college.task.service.ICollegeTaskDeferApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (CollegeTaskDeferApply)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-13 14:01:01
 */
@Service("collegeTaskDeferApplyService")
public class CollegeTaskDeferApplyServiceImpl extends ServiceImpl<CollegeTaskDeferApplyMapper, CollegeTaskDeferApply> implements ICollegeTaskDeferApplyService {

    @Autowired
    private CollegeTaskDeferApplyMapper collegeTaskDeferApplyMapper;


}