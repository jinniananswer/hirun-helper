package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeCourseChaptersCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseChaptersCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (CollegeCourseChaptersCfg)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-04 00:18:35
 */
@Service("collegeCourseChaptersCfgService")
@DataSource(DataSourceKey.SYS)
public class CollegeCourseChaptersCfgServiceImpl extends ServiceImpl<CollegeCourseChaptersCfgMapper, CollegeCourseChaptersCfg> implements ICollegeCourseChaptersCfgService {

    @Autowired
    private CollegeCourseChaptersCfgMapper collegeCourseChaptersCfgMapper;


    @Override
    public List<CollegeCourseChaptersCfg> queryByStudyId(String studyId) {
        return this.list(Wrappers.<CollegeCourseChaptersCfg>lambdaQuery().eq(CollegeCourseChaptersCfg::getStudyId, studyId)
                .eq(CollegeCourseChaptersCfg::getStatus, '0')
                .orderByAsc(CollegeCourseChaptersCfg::getChaptersId));
    }

    @Override
    public List<CollegeCourseChaptersCfg> queryByStudyIdList(List<String> studyIdList) {
        return this.list(Wrappers.<CollegeCourseChaptersCfg>lambdaQuery().in(CollegeCourseChaptersCfg::getStudyId, studyIdList)
                .eq(CollegeCourseChaptersCfg::getStatus, '0'));
    }

    @Override
    public String getChapterNameByChaptersId(Long chaptersId) {
        CollegeCourseChaptersCfg collegeCourseChaptersCfg = this.getOne(Wrappers.<CollegeCourseChaptersCfg>lambdaQuery()
                .eq(CollegeCourseChaptersCfg::getChaptersId, chaptersId), false);
        if (null != collegeCourseChaptersCfg){
            return collegeCourseChaptersCfg.getChaptersName();
        }
        return "";
    }
}