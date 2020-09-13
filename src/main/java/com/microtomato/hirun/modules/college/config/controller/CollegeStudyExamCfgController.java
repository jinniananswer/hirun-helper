package com.microtomato.hirun.modules.college.config.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.config.entity.dto.*;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyExamCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyExercisesCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyExamCfgService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * (CollegeStudyExamCfg)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-11 00:30:30
 */
@RestController
@RequestMapping("/api/CollegeStudyExamCfg")
public class CollegeStudyExamCfgController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeStudyExamCfgService collegeStudyExamCfgService;

    @Autowired
    private IStaticDataService staticDataServiceImpl;

    /**
     * 分页查询所有数据
     *
     * @param page                分页对象
     * @param collegeStudyExamCfg 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeStudyExamCfg> selectByPage(Page<CollegeStudyExamCfg> page, CollegeStudyExamCfg collegeStudyExamCfg) {
        return this.collegeStudyExamCfgService.page(page, new QueryWrapper<>(collegeStudyExamCfg));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeStudyExamCfg selectById(@PathVariable Serializable id) {
        return this.collegeStudyExamCfgService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeStudyExamCfg 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeStudyExamCfg collegeStudyExamCfg) {
        return this.collegeStudyExamCfgService.save(collegeStudyExamCfg);
    }

    /**
     * 修改数据
     *
     * @param collegeStudyExamCfg 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeStudyExamCfg collegeStudyExamCfg) {
        return this.collegeStudyExamCfgService.updateById(collegeStudyExamCfg);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeStudyExamCfgService.removeByIds(idList);
    }

    @PostMapping("fixedStudyExam")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public CollegeFixedExamResponseDTO fixedStudyExam(@RequestBody CollegeFixedExamRequestDTO collegeFixedExamRequestDTO){
        CollegeFixedExamResponseDTO result = new CollegeFixedExamResponseDTO();
        if (null != collegeFixedExamRequestDTO){
            List<CollegeFixedExamTaskDTO> studyChaptersList = collegeFixedExamRequestDTO.getStudyChaptersList();
            if (ArrayUtils.isNotEmpty(studyChaptersList)){
                List<CollegeStudyExamCfg> collegeStudyExamCfgList = new ArrayList<>();
                for (CollegeFixedExamTaskDTO collegeFixedExamTaskDTO : studyChaptersList){
                    String studyId = collegeFixedExamTaskDTO.getStudyId();
                    String chaptersId = collegeFixedExamTaskDTO.getChaptersId();
                    CollegeStudyExamCfg collegeStudyExamCfg = new CollegeStudyExamCfg();
                    BeanUtils.copyProperties(collegeFixedExamRequestDTO, collegeStudyExamCfg);
                    collegeStudyExamCfg.setChaptersId(chaptersId);
                    collegeStudyExamCfg.setStudyId(studyId);
                    collegeStudyExamCfg.setStatus("0");
                    collegeStudyExamCfgList.add(collegeStudyExamCfg);
                }
                if (ArrayUtils.isNotEmpty(collegeStudyExamCfgList) && collegeStudyExamCfgList.size() > 0){
                    this.collegeStudyExamCfgService.saveBatch(collegeStudyExamCfgList);
                }
            }
            String examId = collegeFixedExamRequestDTO.getExamId();
            String examName = "";
            if (StringUtils.isNotEmpty(examId)){
                examName = staticDataServiceImpl.getCodeName("EXAM_RANGE", examId);
            }
            if (StringUtils.isEmpty(examName)){
                examName = examId;
            }
            result.setExamName(examName);
            String exercisesType = collegeFixedExamRequestDTO.getExercisesType();
            String exercisesTypeName = "";
            if (StringUtils.isNotEmpty(exercisesType)){
                exercisesTypeName = staticDataServiceImpl.getCodeName("EXERCISES_TYPE", exercisesType);
            }
            if (StringUtils.isEmpty(exercisesTypeName)){
                exercisesTypeName = exercisesType;
            }
            result.setExercisesTypeName(exercisesTypeName);
        }
        return result;
    }
}