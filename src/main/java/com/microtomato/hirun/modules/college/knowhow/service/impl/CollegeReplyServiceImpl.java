package com.microtomato.hirun.modules.college.knowhow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.knowhow.mapper.CollegeReplyMapper;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeReply;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeReplyService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * (CollegeReply)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-08-16 16:10:18
 */
@Service("collegeReplyService")
public class CollegeReplyServiceImpl extends ServiceImpl<CollegeReplyMapper, CollegeReply> implements ICollegeReplyService {

    @Autowired
    private CollegeReplyMapper collegeReplyMapper;
    

}