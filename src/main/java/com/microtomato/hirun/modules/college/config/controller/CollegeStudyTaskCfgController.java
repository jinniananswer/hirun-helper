package com.microtomato.hirun.modules.college.config.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.config.entity.dto.*;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseChaptersCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyTaskCfgService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * (CollegeStudyTaskCfg)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-26 02:04:48
 */
@RestController
@RequestMapping("/api/CollegeStudyTaskCfg")
public class CollegeStudyTaskCfgController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeStudyTaskCfgService collegeStudyTaskCfgService;

    @Autowired
    private ICollegeCourseChaptersCfgService collegeCourseChaptersCfgServiceImpl;

    /**
     * 分页查询所有数据
     *
     * @param page                分页对象
     * @param collegeStudyTaskCfg 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeStudyTaskCfg> selectByPage(Page<CollegeStudyTaskCfg> page, CollegeStudyTaskCfg collegeStudyTaskCfg) {
        return this.collegeStudyTaskCfgService.page(page, new QueryWrapper<>(collegeStudyTaskCfg));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeStudyTaskCfg selectById(@PathVariable Serializable id) {
        return this.collegeStudyTaskCfgService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeStudyTaskCfg 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeStudyTaskCfg collegeStudyTaskCfg) {
        return this.collegeStudyTaskCfgService.save(collegeStudyTaskCfg);
    }

    /**
     * 修改数据
     *
     * @param collegeStudyTaskCfg 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeStudyTaskCfg collegeStudyTaskCfg) {
        return this.collegeStudyTaskCfgService.updateById(collegeStudyTaskCfg);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeStudyTaskCfgService.removeByIds(idList);
    }

    @GetMapping("queryCollegeStufyByPage")
    @RestResult
    IPage<CollegeStudyTaskResponseDTO> queryCollegeStufyByPage(CollegeStudyTaskRequestDTO collegeStudyTaskRequestDTO){
        Page<CollegeStudyTaskRequestDTO> page = new Page<>(collegeStudyTaskRequestDTO.getPage(), collegeStudyTaskRequestDTO.getLimit());
        return this.collegeStudyTaskCfgService.queryCollegeStudyByPage(collegeStudyTaskRequestDTO, page);
    }

    @PostMapping("addStudyTaskCfg")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void addStudyTaskCfg(@RequestBody CollegeCourseChaptersTaskRequestDTO collegeCourseChaptersTaskRequestDTO){
        CollegeStudyTaskCfg collegeStudyTaskCfg = new CollegeStudyTaskCfg();
        BeanUtils.copyProperties(collegeCourseChaptersTaskRequestDTO, collegeStudyTaskCfg);
        collegeStudyTaskCfg.setStatus("0");
        this.collegeStudyTaskCfgService.save(collegeStudyTaskCfg);
        List<CollegeCourseChaptersCfg> courseChaptersList = collegeCourseChaptersTaskRequestDTO.getCourseChaptersList();
        if (ArrayUtils.isNotEmpty(courseChaptersList)){
            for (CollegeCourseChaptersCfg courseChaptersCfg : courseChaptersList){
                courseChaptersCfg.setStatus("0");
            }
            collegeCourseChaptersCfgServiceImpl.saveBatch(courseChaptersList);
        }
    }

    @PostMapping("deleteStudyTaskBatch")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void deleteStudyTaskBatch(@RequestBody List<CollegeStudyTaskResponseDTO> collegeStudyTaskResponseDTOList){
        //1.根据学习任务ID集合查询出所有的任务配置
        List<Long> studyTaskIdList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(collegeStudyTaskResponseDTOList)){
            for (CollegeStudyTaskResponseDTO collegeStudyTaskResponseDTO : collegeStudyTaskResponseDTOList){
                studyTaskIdList.add(collegeStudyTaskResponseDTO.getStudyTaskId());
            }
            List<CollegeStudyTaskCfg> collegeStudyTaskCfgs = this.collegeStudyTaskCfgService.queryByStudyTaskIdList(studyTaskIdList);
            if (ArrayUtils.isNotEmpty(collegeStudyTaskCfgs)){
                //2.获取所有的学习标识集合
                List<String> studyIdList = new ArrayList<>();
                for (CollegeStudyTaskCfg collegeStudyTaskCfg : collegeStudyTaskCfgs){
                    collegeStudyTaskCfg.setStatus("1");
                    studyIdList.add(collegeStudyTaskCfg.getStudyId());
                }

                //3.删除学习任务配置信息，及将修改状态
                this.collegeStudyTaskCfgService.updateBatchById(collegeStudyTaskCfgs);

                //4.根据所有学习标识集合，查询章节配置信息
                if(ArrayUtils.isNotEmpty(studyIdList)){
                    List<CollegeCourseChaptersCfg> collegeCourseChaptersCfgs = this.collegeCourseChaptersCfgServiceImpl.queryByStudyIdList(studyIdList);
                    if (ArrayUtils.isNotEmpty(collegeCourseChaptersCfgs)){
                        for (CollegeCourseChaptersCfg collegeCourseChaptersCfg : collegeCourseChaptersCfgs){
                            collegeCourseChaptersCfg.setStatus("1");
                        }
                        //5.删除查询出的章节配置信息
                        this.collegeCourseChaptersCfgServiceImpl.updateBatchById(collegeCourseChaptersCfgs);
                    }
                }
            }
        }
    }

    @PostMapping("deleteStudyTaskByRow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void deleteStudyTaskByRow(@RequestBody CollegeStudyTaskResponseDTO collegeStudyTaskResponseDTO){
        //1.根据学习任务标识查询学习任务配置
        CollegeStudyTaskCfg collegeStudyTaskCfg = this.collegeStudyTaskCfgService.getByStudyTaskId(collegeStudyTaskResponseDTO.getStudyTaskId());
        if (null != collegeStudyTaskCfg){
            //2.修改学习任务配置状态
            collegeStudyTaskCfg.setStatus("1");
            this.collegeStudyTaskCfgService.updateById(collegeStudyTaskCfg);

            //3.根据学习内容标识查询章节配置信息
            List<CollegeCourseChaptersCfg> collegeCourseChaptersCfgList = this.collegeCourseChaptersCfgServiceImpl.queryByStudyId(collegeStudyTaskCfg.getStudyId());
            if (ArrayUtils.isNotEmpty(collegeCourseChaptersCfgList)){
                //4.修改章节配置状态
                for (CollegeCourseChaptersCfg collegeCourseChaptersCfg : collegeCourseChaptersCfgList){
                    collegeCourseChaptersCfg.setStatus("1");
                }
                this.collegeCourseChaptersCfgServiceImpl.updateBatchById(collegeCourseChaptersCfgList);
            }
        }
    }

    @PostMapping("updateStudyTaskByDTO")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void updateStudyTaskByDTO(@RequestBody CollegeStudyTaskResponseDTO collegeStudyTaskResponseDTO){
        //1.根据学习任务标识查询学习任务配置
        CollegeStudyTaskCfg collegeStudyTaskCfg = this.collegeStudyTaskCfgService.getByStudyTaskId(collegeStudyTaskResponseDTO.getStudyTaskId());
        if (null != collegeStudyTaskCfg){
            collegeStudyTaskCfg.setExercisesNumber(collegeStudyTaskResponseDTO.getExercisesNumber());
            collegeStudyTaskCfg.setPassScore(collegeStudyTaskResponseDTO.getPassScore());
            collegeStudyTaskCfg.setStudyTime(collegeStudyTaskResponseDTO.getStudyTime());
            this.collegeStudyTaskCfgService.updateById(collegeStudyTaskCfg);

            List<CollegeCourseChaptersTaskResponseDTO> collegeCourseChaptersTaskResponseDTOList = collegeStudyTaskResponseDTO.getCollegeCourseChaptersList();
            if (ArrayUtils.isNotEmpty(collegeCourseChaptersTaskResponseDTOList)){
                List<CollegeCourseChaptersCfg> collegeCourseChaptersList = new ArrayList<>();
                for (CollegeCourseChaptersTaskResponseDTO collegeCourseChaptersTaskResponseDTO : collegeCourseChaptersTaskResponseDTOList){
                    CollegeCourseChaptersCfg collegeCourseChaptersCfg = new CollegeCourseChaptersCfg();
                    BeanUtils.copyProperties(collegeCourseChaptersTaskResponseDTO, collegeCourseChaptersCfg);
                    collegeCourseChaptersList.add(collegeCourseChaptersCfg);
                }
                this.collegeCourseChaptersCfgServiceImpl.updateBatchById(collegeCourseChaptersList);
            }
        }
    }

    @GetMapping("queryCollegeStudyExercisesByPage")
    @RestResult
    IPage<CollegeStudyExercisesResponseDTO> queryCollegeStudyExercisesByPage(CollegeStudyTaskRequestDTO collegeStudyTaskRequestDTO){
        Page<CollegeStudyTaskRequestDTO> page = new Page<>(collegeStudyTaskRequestDTO.getPage(), collegeStudyTaskRequestDTO.getLimit());
        return this.collegeStudyTaskCfgService.queryCollegeStudyExercisesByPage(collegeStudyTaskRequestDTO, page);
    }

    @GetMapping("queryCollegeStudyExamByPage")
    @RestResult
    IPage<CollegeStudyExamResponseDTO> queryCollegeStudyExamByPage(CollegeStudyTaskRequestDTO collegeStudyTaskRequestDTO){
        Page<CollegeStudyTaskRequestDTO> page = new Page<>(collegeStudyTaskRequestDTO.getPage(), collegeStudyTaskRequestDTO.getLimit());
        return this.collegeStudyTaskCfgService.queryCollegeStudyExamByPage(collegeStudyTaskRequestDTO, page);
    }
}