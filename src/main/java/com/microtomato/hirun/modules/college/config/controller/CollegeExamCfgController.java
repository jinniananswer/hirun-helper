package com.microtomato.hirun.modules.college.config.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeReleaseTaskExamRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeReleaseExamTaskDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeTopicInfoRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeExamCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeExamRelCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeExamCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeExamRelCfgService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * (CollegeExamCfg)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-18 01:12:19
 */
@RestController
@RequestMapping("/api/CollegeExamCfg")
public class CollegeExamCfgController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeExamCfgService collegeExamCfgService;

    @Autowired
    private ICollegeExamRelCfgService collegeExamRelCfgServiceImpl;

    /**
     * 分页查询所有数据
     *
     * @param page           分页对象
     * @param collegeExamCfg 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeExamCfg> selectByPage(Page<CollegeExamCfg> page, CollegeExamCfg collegeExamCfg) {
        return this.collegeExamCfgService.page(page, new QueryWrapper<>(collegeExamCfg));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeExamCfg selectById(@PathVariable Serializable id) {
        return this.collegeExamCfgService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeExamCfg 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeExamCfg collegeExamCfg) {
        return this.collegeExamCfgService.save(collegeExamCfg);
    }

    /**
     * 修改数据
     *
     * @param collegeExamCfg 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeExamCfg collegeExamCfg) {
        return this.collegeExamCfgService.updateById(collegeExamCfg);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeExamCfgService.removeByIds(idList);
    }

    @PostMapping("releaseTaskExam")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void releaseTaskExam(@RequestBody CollegeReleaseTaskExamRequestDTO collegeReleaseTaskExamRequestDTO){
        if (null != collegeReleaseTaskExamRequestDTO){
            List<CollegeExamRelCfg> collegeExamRelCfgList = new ArrayList<>();
            List<CollegeReleaseExamTaskDTO> studyChaptersList = collegeReleaseTaskExamRequestDTO.getTaskInfoList();
            if (ArrayUtils.isNotEmpty(studyChaptersList)){
                for (CollegeReleaseExamTaskDTO collegeReleaseExamTaskDTO : studyChaptersList){
                    String studyTaskId = collegeReleaseExamTaskDTO.getStudyTaskId();
                    CollegeExamCfg collegeExamCfg = new CollegeExamCfg();
                    BeanUtils.copyProperties(collegeReleaseTaskExamRequestDTO, collegeExamCfg);
                    collegeExamCfg.setStudyTaskId(studyTaskId);
                    collegeExamCfg.setStatus("0");
                    this.collegeExamCfgService.save(collegeExamCfg);
                    Long examTopicId = collegeExamCfg.getExamTopicId();
                    List<CollegeTopicInfoRequestDTO> studyTopicTypeInfoDetails = collegeReleaseTaskExamRequestDTO.getStudyTopicTypeInfoDetails();
                    if (ArrayUtils.isNotEmpty(studyTopicTypeInfoDetails)){
                        for (CollegeTopicInfoRequestDTO collegeTopicInfoRequestDTO : studyTopicTypeInfoDetails){
                            String exercisesNumber = collegeTopicInfoRequestDTO.getExercisesNumber();
                            String exercisesType = collegeTopicInfoRequestDTO.getExercisesType();
                            CollegeExamRelCfg collegeExamRelCfg = new CollegeExamRelCfg();
                            collegeExamRelCfg.setTopicNum(Integer.valueOf(exercisesNumber));
                            collegeExamRelCfg.setTopicType(exercisesType);
                            collegeExamRelCfg.setStudyExercisesId(String.valueOf(examTopicId));
                            collegeExamRelCfg.setStatus("0");
                            collegeExamRelCfgList.add(collegeExamRelCfg);
                        }
                    }
                }
                if (ArrayUtils.isNotEmpty(collegeExamRelCfgList)){
                    collegeExamRelCfgServiceImpl.saveBatch(collegeExamRelCfgList);
                }
            }
        }
    }
}