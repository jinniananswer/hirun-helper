package com.microtomato.hirun.modules.college.config.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeFixedExamResponseDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeFixedExercisesRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeFixedExercisesTaskDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeStudyTaskResponseDTO;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyExercisesCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyExercisesCfgService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import io.netty.util.internal.ObjectUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * (CollegeStudyExercisesCfg)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-04 00:41:33
 */
@RestController
@RequestMapping("/api/CollegeStudyExercisesCfg")
public class CollegeStudyExercisesCfgController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeStudyExercisesCfgService collegeStudyExercisesCfgService;

    @Autowired
    private IStaticDataService staticDataServiceImpl;

    /**
     * 分页查询所有数据
     *
     * @param page                     分页对象
     * @param collegeStudyExercisesCfg 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeStudyExercisesCfg> selectByPage(Page<CollegeStudyExercisesCfg> page, CollegeStudyExercisesCfg collegeStudyExercisesCfg) {
        return this.collegeStudyExercisesCfgService.page(page, new QueryWrapper<>(collegeStudyExercisesCfg));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeStudyExercisesCfg selectById(@PathVariable Serializable id) {
        return this.collegeStudyExercisesCfgService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeStudyExercisesCfg 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeStudyExercisesCfg collegeStudyExercisesCfg) {
        return this.collegeStudyExercisesCfgService.save(collegeStudyExercisesCfg);
    }

    /**
     * 修改数据
     *
     * @param collegeStudyExercisesCfg 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeStudyExercisesCfg collegeStudyExercisesCfg) {
        return this.collegeStudyExercisesCfgService.updateById(collegeStudyExercisesCfg);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeStudyExercisesCfgService.removeByIds(idList);
    }

    @PostMapping("fixedStudyExercises")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public CollegeFixedExamResponseDTO fixedStudyExercises(@RequestBody CollegeFixedExercisesRequestDTO collegeFixedExercisesRequestDTO){
        CollegeFixedExamResponseDTO result = new CollegeFixedExamResponseDTO();
        if (null != collegeFixedExercisesRequestDTO){
            List<CollegeFixedExercisesTaskDTO> studyChaptersList = collegeFixedExercisesRequestDTO.getStudyChaptersList();
            if (ArrayUtils.isNotEmpty(studyChaptersList)){
                List<CollegeStudyExercisesCfg> collegeStudyExercisesCfgList = new ArrayList<>();
                for (CollegeFixedExercisesTaskDTO collegeFixedExercisesTaskDTO : studyChaptersList){
                    String studyId = collegeFixedExercisesTaskDTO.getStudyId();
                    String chaptersId = collegeFixedExercisesTaskDTO.getChaptersId();
                    CollegeStudyExercisesCfg collegeStudyExercisesCfg = new CollegeStudyExercisesCfg();
                    BeanUtils.copyProperties(collegeFixedExercisesRequestDTO, collegeStudyExercisesCfg);
                    collegeStudyExercisesCfg.setChaptersId(chaptersId);
                    collegeStudyExercisesCfg.setStudyId(studyId);
                    collegeStudyExercisesCfg.setStatus("0");
                    collegeStudyExercisesCfgList.add(collegeStudyExercisesCfg);
                }
                if (ArrayUtils.isNotEmpty(collegeStudyExercisesCfgList) && collegeStudyExercisesCfgList.size() > 0){
                    this.collegeStudyExercisesCfgService.saveBatch(collegeStudyExercisesCfgList);
                }
                String examId = collegeFixedExercisesRequestDTO.getExamId();
                String examName = "";
                if (StringUtils.isNotEmpty(examId)){
                    examName = staticDataServiceImpl.getCodeName("EXAM_RANGE", examId);
                }
                if (StringUtils.isEmpty(examName)){
                    examName = examId;
                }
                result.setExamName(examName);
                String exercisesType = collegeFixedExercisesRequestDTO.getExercisesType();
                String exercisesTypeName = "";
                if (StringUtils.isNotEmpty(exercisesType)){
                    exercisesTypeName = staticDataServiceImpl.getCodeName("EXERCISES_TYPE", exercisesType);
                }
                if (StringUtils.isEmpty(exercisesTypeName)){
                    exercisesTypeName = exercisesType;
                }
                result.setExercisesTypeName(exercisesTypeName);
            }
        }
        return result;
    }
}