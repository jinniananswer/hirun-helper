package com.microtomato.hirun.modules.college.config.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeFixedExamRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeFixedExamTaskDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeFixedExercisesRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeFixedExercisesTaskDTO;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyExamCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyExercisesCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyExamCfgService;
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
    public void fixedStudyExam(@RequestBody CollegeFixedExamRequestDTO collegeFixedExamRequestDTO){
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
        }
    }
}