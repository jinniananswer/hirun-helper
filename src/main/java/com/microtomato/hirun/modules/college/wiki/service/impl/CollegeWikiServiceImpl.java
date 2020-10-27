package com.microtomato.hirun.modules.college.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.topic.entity.po.ExamTopic;
import com.microtomato.hirun.modules.college.wiki.mapper.CollegeWikiMapper;
import com.microtomato.hirun.modules.college.wiki.entity.po.CollegeWiki;
import com.microtomato.hirun.modules.college.wiki.service.ICollegeWikiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * (CollegeWiki)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-28 02:20:47
 */
@Service("collegeWikiService")
public class CollegeWikiServiceImpl extends ServiceImpl<CollegeWikiMapper, CollegeWiki> implements ICollegeWikiService {

    @Autowired
    private CollegeWikiMapper collegeWikiMapper;
    
    @Override
    public List<CollegeWiki> queryByText(String keyStr) {
        return this.list(new QueryWrapper<CollegeWiki>().lambda()
//                .and(q -> q.like(StringUtils.isNotEmpty(keyStr), CollegeWiki::getWikiTitle, keyStr)
//                        .or().like(StringUtils.isNotEmpty(keyStr), CollegeWiki::getWikiContent, keyStr))
                .eq(CollegeWiki::getStatus, "0")
                .orderByDesc(CollegeWiki::getWikiType));
    }
}