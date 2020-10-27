package com.microtomato.hirun.modules.college.wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.wiki.mapper.CollegeWikiReplyMapper;
import com.microtomato.hirun.modules.college.wiki.entity.po.CollegeWikiReply;
import com.microtomato.hirun.modules.college.wiki.service.ICollegeWikiReplyService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * (CollegeWikiReply)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-28 02:20:48
 */
@Service("collegeWikiReplyService")
public class CollegeWikiReplyServiceImpl extends ServiceImpl<CollegeWikiReplyMapper, CollegeWikiReply> implements ICollegeWikiReplyService {

    @Autowired
    private CollegeWikiReplyMapper collegeWikiReplyMapper;
    

}