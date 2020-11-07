package com.microtomato.hirun.modules.college.knowhow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.knowhow.mapper.CollegeQuestionReplyerCfgMapper;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestionReplyerCfg;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeQuestionReplyerCfgService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * (CollegeQuestionReplyerCfg)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-11-07 13:31:08
 */
@Service("collegeQuestionReplyerCfgService")
public class CollegeQuestionReplyerCfgServiceImpl extends ServiceImpl<CollegeQuestionReplyerCfgMapper, CollegeQuestionReplyerCfg> implements ICollegeQuestionReplyerCfgService {

    @Autowired
    private CollegeQuestionReplyerCfgMapper collegeQuestionReplyerCfgMapper;


    @Override
    public List<Long> queryTeacherByType(String questionType) {
        List<Long> teacherIds = new ArrayList<>();
        List<CollegeQuestionReplyerCfg> list = this.list(new QueryWrapper<CollegeQuestionReplyerCfg>().lambda()
                .eq(CollegeQuestionReplyerCfg::getQuestionType, questionType)
                .eq(CollegeQuestionReplyerCfg::getStatus, "0"));
        if (ArrayUtils.isEmpty(list)) {
            return teacherIds;
        }

        list.stream().forEach(x -> {
            teacherIds.add(x.getTeacherId());
        });
        return teacherIds;
    }
}