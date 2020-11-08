package com.microtomato.hirun.modules.college.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeReleaseExamTaskDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeReleaseTaskExamRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeTopicInfoRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeExamCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeExamRelCfg;
import com.microtomato.hirun.modules.college.config.mapper.CollegeExamCfgMapper;
import com.microtomato.hirun.modules.college.config.service.ICollegeExamCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeExamRelCfgService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (CollegeExamCfg)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-24 04:21:53
 */
@Service("collegeExamCfgService")
@DataSource(DataSourceKey.SYS)
public class CollegeExamCfgServiceImpl extends ServiceImpl<CollegeExamCfgMapper, CollegeExamCfg> implements ICollegeExamCfgService {

    @Autowired
    private CollegeExamCfgMapper collegeExamCfgMapper;

    @Autowired
    private ICollegeExamRelCfgService collegeExamRelCfgServiceImpl;

    @Override
    public CollegeExamCfg getByStudyTaskId(Long studyTaskId) {
        return this.getOne(new QueryWrapper<CollegeExamCfg>().lambda()
                .eq(CollegeExamCfg::getStudyTaskId, studyTaskId)
                .eq(CollegeExamCfg::getStatus, "0"));
    }
    @Override
    public CollegeExamCfg getByStudyTaskIdAndExamType(String studyTaskId, String examTyp) {
        return this.getOne(Wrappers.<CollegeExamCfg>lambdaQuery().eq(CollegeExamCfg::getStudyTaskId, studyTaskId).eq(CollegeExamCfg::getExamType, examTyp).eq(CollegeExamCfg::getStatus, "0"));
    }

    @Override
    public boolean updateByIds(CollegeExamCfg collegeExamCfg) {
        return this.update(collegeExamCfg, Wrappers.<CollegeExamCfg>lambdaUpdate()
                .eq(CollegeExamCfg::getStudyTaskId, collegeExamCfg.getStudyTaskId())
                .eq(CollegeExamCfg::getExamTopicId, collegeExamCfg.getExamTopicId())
                .eq(CollegeExamCfg::getExamType, collegeExamCfg.getExamType()));
    }

    @Override
    public void releaseTaskExam(CollegeReleaseTaskExamRequestDTO collegeReleaseTaskExamRequestDTO) {
        if (null != collegeReleaseTaskExamRequestDTO){
            List<CollegeReleaseExamTaskDTO> studyChaptersList = collegeReleaseTaskExamRequestDTO.getTaskInfoList();
            if (ArrayUtils.isNotEmpty(studyChaptersList)){
                for (CollegeReleaseExamTaskDTO collegeReleaseExamTaskDTO : studyChaptersList){
                    String studyTaskId = collegeReleaseExamTaskDTO.getStudyTaskId();
                    CollegeExamCfg collegeExamCfg = this.getByStudyTaskIdAndExamType(studyTaskId, collegeReleaseTaskExamRequestDTO.getExamType());
                    if (null != collegeExamCfg){
                        collegeExamCfg.setExamMaxNum(collegeReleaseTaskExamRequestDTO.getExamMaxNum());
                        collegeExamCfg.setPassScore(collegeReleaseTaskExamRequestDTO.getPassScore());
                        collegeExamCfg.setExamTime(collegeReleaseTaskExamRequestDTO.getExamTime());
                        collegeExamCfg.setMinNum(collegeReleaseTaskExamRequestDTO.getMinNum());
                        if (null == collegeExamCfg.getPassScore()){
                            collegeExamCfg.setPassScore(80);
                        }
                        this.updateByIds(collegeExamCfg);
                    }else {
                        collegeExamCfg = new CollegeExamCfg();
                        BeanUtils.copyProperties(collegeReleaseTaskExamRequestDTO, collegeExamCfg);
                        collegeExamCfg.setStudyTaskId(studyTaskId);
                        collegeExamCfg.setStatus("0");
                        if (null == collegeExamCfg.getPassScore()){
                            collegeExamCfg.setPassScore(80);
                        }
                        this.save(collegeExamCfg);
                    }
                    Long examTopicId = collegeExamCfg.getExamTopicId();
                    List<CollegeTopicInfoRequestDTO> studyTopicTypeInfoDetails = collegeReleaseTaskExamRequestDTO.getStudyTopicTypeInfoDetails();
                    if (ArrayUtils.isNotEmpty(studyTopicTypeInfoDetails)){
                        for (CollegeTopicInfoRequestDTO collegeTopicInfoRequestDTO : studyTopicTypeInfoDetails){
                            String exercisesNumber = collegeTopicInfoRequestDTO.getExercisesNumber();
                            String exercisesType = collegeTopicInfoRequestDTO.getExercisesType();
                            CollegeExamRelCfg collegeExamRelCfg = collegeExamRelCfgServiceImpl.getEffectiveByExamTopicIdAndTopicType(examTopicId, exercisesType);
                            if (null != collegeExamRelCfg){
                                collegeExamRelCfg.setTopicNum(Integer.valueOf(exercisesNumber));
                                collegeExamRelCfg.setTopicType(exercisesType);
                                collegeExamRelCfgServiceImpl.updateById(collegeExamRelCfg);
                            }else {
                                collegeExamRelCfg = new CollegeExamRelCfg();
                                collegeExamRelCfg.setTopicNum(Integer.valueOf(exercisesNumber));
                                collegeExamRelCfg.setTopicType(exercisesType);
                                collegeExamRelCfg.setExamTopicId(examTopicId);
                                collegeExamRelCfg.setStatus("0");
                                collegeExamRelCfgServiceImpl.save(collegeExamRelCfg);
                            }
                        }
                    }
                }
            }
        }
    }
}