package com.microtomato.hirun.modules.college.config.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeCourseChaptersTaskResponseDTO;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseChaptersCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyTaskCfgService;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTopicRel;
import com.microtomato.hirun.modules.college.task.service.ICollegeStudyTopicRelService;
import com.microtomato.hirun.modules.organization.service.ICourseService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import com.microtomato.hirun.modules.system.service.IUploadFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeCourseChaptersCfg)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-04 00:18:35
 */
@RestController
@RequestMapping("/api/CollegeCourseChaptersCfg")
public class CollegeCourseChaptersCfgController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeCourseChaptersCfgService collegeCourseChaptersCfgService;

    @Autowired
    private ICollegeStudyTopicRelService collegeStudyTopicRelServiceImpl;

    @Autowired
    private IStaticDataService staticDataServiceImpl;

    @Autowired
    private ICollegeStudyTaskCfgService collegeStudyTaskCfgServiceImpl;

    @Autowired
    private IUploadFileService uploadFileServiceImpl;

    @Autowired
    private ICourseService courseServiceImpl;

    /**
     * 分页查询所有数据
     *
     * @param page                     分页对象
     * @param collegeCourseChaptersCfg 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeCourseChaptersCfg> selectByPage(Page<CollegeCourseChaptersCfg> page, CollegeCourseChaptersCfg collegeCourseChaptersCfg) {
        return this.collegeCourseChaptersCfgService.page(page, new QueryWrapper<>(collegeCourseChaptersCfg));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeCourseChaptersCfg selectById(@PathVariable Serializable id) {
        return this.collegeCourseChaptersCfgService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeCourseChaptersCfg 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeCourseChaptersCfg collegeCourseChaptersCfg) {
        return this.collegeCourseChaptersCfgService.save(collegeCourseChaptersCfg);
    }

    /**
     * 修改数据
     *
     * @param collegeCourseChaptersCfg 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeCourseChaptersCfg collegeCourseChaptersCfg) {
        return this.collegeCourseChaptersCfgService.updateById(collegeCourseChaptersCfg);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeCourseChaptersCfgService.removeByIds(idList);
    }

    @PostMapping("addChapters")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public CollegeCourseChaptersTaskResponseDTO addChapters(@RequestBody CollegeCourseChaptersCfg collegeCourseChaptersCfg){
        collegeCourseChaptersCfg.setStatus("0");
        this.collegeCourseChaptersCfgService.save(collegeCourseChaptersCfg);
        CollegeStudyTopicRel collegeStudyTopicCfg = new CollegeStudyTopicRel();
        collegeStudyTopicCfg.setStatus("0");
        collegeStudyTopicCfg.setChaptersId(String.valueOf(collegeCourseChaptersCfg.getChaptersId()));
        collegeStudyTopicCfg.setStudyId(collegeCourseChaptersCfg.getStudyId());
        collegeStudyTopicCfg.setStudyTopicDesc(collegeCourseChaptersCfg.getChaptersName() + "课件与习题范围关系");
        collegeStudyTopicCfg.setStudyTopicName(collegeCourseChaptersCfg.getChaptersName() + "习题");
        collegeStudyTopicRelServiceImpl.save(collegeStudyTopicCfg);
        CollegeCourseChaptersTaskResponseDTO result = new CollegeCourseChaptersTaskResponseDTO();
        BeanUtils.copyProperties(collegeCourseChaptersCfg, result);
        String chaptersType = result.getChaptersType();
        String chaptersTypeName = chaptersType;
        if (StringUtils.isNotEmpty(chaptersType)){
            chaptersTypeName = staticDataServiceImpl.getCodeName("CHAPTERS_TYPE", chaptersType);
        }
        result.setChaptersTypeName(chaptersTypeName);
        String studyModel = result.getStudyModel();
        String studyModelName = studyModel;
        if (StringUtils.isNotEmpty(studyModel)){
            studyModelName = staticDataServiceImpl.getCodeName("STUDY_MODEL", studyModel);
        }
        result.setStudyModelName(studyModelName);
        String studyId = collegeCourseChaptersCfg.getStudyId();
        CollegeStudyTaskCfg collegeStudyTaskCfg = collegeStudyTaskCfgServiceImpl.getEffectiveByStudyId(studyId);
        if (null != collegeStudyTaskCfg){
            String studyType = collegeStudyTaskCfg.getStudyType();
            String studyName = "";
            if (StringUtils.equals("1", studyType)){
                studyName = uploadFileServiceImpl.getFileNameByFileId(studyId);
            }else {
                studyName = courseServiceImpl.getCourseNameByCourseId(Long.valueOf(studyId));
            }
            result.setStudyName(studyName);
        }
        return result;
    }

    @PostMapping("editChapters")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void editChapters(@RequestBody CollegeCourseChaptersCfg collegeCourseChaptersCfg){
        this.collegeCourseChaptersCfgService.updateById(collegeCourseChaptersCfg);
    }
}