package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.college.config.mapper.CollegeTopicLabelCfgMapper;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTopicLabelCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeTopicLabelCfgService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * (CollegeTopicLabelCfg)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-20 01:28:12
 */
@Service("collegeTopicLabelCfgService")
public class CollegeTopicLabelCfgServiceImpl extends ServiceImpl<CollegeTopicLabelCfgMapper, CollegeTopicLabelCfg> implements ICollegeTopicLabelCfgService {

    @Autowired
    private CollegeTopicLabelCfgMapper collegeTopicLabelCfgMapper;


    @Override
    public List<CollegeTopicLabelCfg> queryByLabelId(Long labelId) {
        return this.list(Wrappers.<CollegeTopicLabelCfg>lambdaQuery().eq(CollegeTopicLabelCfg::getLabelId, labelId).eq(CollegeTopicLabelCfg::getStatus, "0"));
    }

    @Override
    public List<CollegeTopicLabelCfg> queryByLabelIdList(List<Long> labelIdList) {
        return this.list(Wrappers.<CollegeTopicLabelCfg>lambdaQuery().in(CollegeTopicLabelCfg::getLabelId, labelIdList).eq(CollegeTopicLabelCfg::getStatus, "0"));
    }

    @Override
    public List<CollegeTopicLabelCfg> queryEffectiveLabel() {
        return this.list(Wrappers.<CollegeTopicLabelCfg>lambdaQuery().eq(CollegeTopicLabelCfg::getStatus, "0"));
    }
}