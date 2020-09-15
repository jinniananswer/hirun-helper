package com.microtomato.hirun.modules.college.config.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.config.entity.dto.*;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeTaskJobCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseChaptersCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyTaskCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeTaskJobCfgService;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTopicCfg;
import com.microtomato.hirun.modules.college.task.service.ICollegeStudyTopicCfgService;
import com.microtomato.hirun.modules.system.entity.po.StaticData;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

    @Autowired
    private IStaticDataService staticDataServiceImpl;

    @Autowired
    private ICollegeTaskJobCfgService collegeTaskJobCfgServiceImpl;

    @Autowired
    private ICollegeStudyTopicCfgService collegeStudyTopicCfgServiceImpl;

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
    public CollegeStudyTaskResponseDTO addStudyTaskCfg(@RequestBody CollegeCourseChaptersTaskRequestDTO collegeCourseChaptersTaskRequestDTO){
        CollegeStudyTaskResponseDTO result = new CollegeStudyTaskResponseDTO();
        CollegeStudyTaskCfg collegeStudyTaskCfg = new CollegeStudyTaskCfg();
        BeanUtils.copyProperties(collegeCourseChaptersTaskRequestDTO, collegeStudyTaskCfg);
        BeanUtils.copyProperties(collegeCourseChaptersTaskRequestDTO, result);
        collegeStudyTaskCfg.setStatus("0");
        String studyType = result.getStudyType();
        List<CollegeStudyTopicCfg> collegeStudyTopicCfgList = new ArrayList<>();
        if (StringUtils.equals("1", studyType)){
            String studyName = collegeStudyTaskCfg.getStudyName();
            studyName = studyName.substring(0, studyName.lastIndexOf("."));
            collegeStudyTaskCfg.setStudyName(studyName);
            result.setStudyName(studyName);

            //如果是课件，新增一个课件与习题的关系
            CollegeStudyTopicCfg collegeStudyTopicCfg = new CollegeStudyTopicCfg();
            collegeStudyTopicCfg.setStatus("0");
            collegeStudyTopicCfg.setStudyId(collegeStudyTaskCfg.getStudyId());
            collegeStudyTopicCfg.setStudyTopicDesc(studyName + "课件与习题范围关系");
            collegeStudyTopicCfg.setStudyTopicName(studyName + "习题");
            collegeStudyTopicCfgList.add(collegeStudyTopicCfg);
        }
        this.collegeStudyTaskCfgService.save(collegeStudyTaskCfg);
        result.setStudyTaskId(collegeStudyTaskCfg.getStudyTaskId());
        //设置任务章节
        List<CollegeCourseChaptersCfg> courseChaptersList = collegeCourseChaptersTaskRequestDTO.getCourseChaptersList();
        if (ArrayUtils.isNotEmpty(courseChaptersList)){
            List<CollegeCourseChaptersTaskResponseDTO> collegeCourseChaptersTaskResponseDTOList = new ArrayList<>();
            for (CollegeCourseChaptersCfg courseChaptersCfg : courseChaptersList){
                courseChaptersCfg.setStatus("0");
                CollegeCourseChaptersTaskResponseDTO collegeCourseChaptersTaskResponseDTO = new CollegeCourseChaptersTaskResponseDTO();
                BeanUtils.copyProperties(courseChaptersCfg, collegeCourseChaptersTaskResponseDTO);
                String chaptersType = collegeCourseChaptersTaskResponseDTO.getChaptersType();
                String chaptersTypeName = chaptersType;
                if (StringUtils.isNotEmpty(chaptersType)){
                    chaptersTypeName = staticDataServiceImpl.getCodeName("CHAPTERS_TYPE", chaptersType);
                }
                collegeCourseChaptersTaskResponseDTO.setChaptersTypeName(chaptersTypeName);
                String studyModel = collegeCourseChaptersTaskResponseDTO.getStudyModel();
                String studyModelName = studyModel;
                if (StringUtils.isNotEmpty(studyModel)){
                    studyModelName = staticDataServiceImpl.getCodeName("CHAPTER_STUDY_MODEL", studyModel);
                }
                collegeCourseChaptersTaskResponseDTO.setStudyModelName(studyModelName);
                collegeCourseChaptersTaskResponseDTOList.add(collegeCourseChaptersTaskResponseDTO);
            }
            collegeCourseChaptersCfgServiceImpl.saveBatch(courseChaptersList);
            List<CollegeCourseChaptersCfg> collegeCourseChaptersCfgs = collegeCourseChaptersCfgServiceImpl.queryByStudyId(collegeStudyTaskCfg.getStudyId());
            for (CollegeCourseChaptersCfg courseChaptersCfg : collegeCourseChaptersCfgs){
                //如果有章节，新增章节与习题的关系
                CollegeStudyTopicCfg collegeStudyTopicCfg = new CollegeStudyTopicCfg();
                collegeStudyTopicCfg.setStatus("0");
                collegeStudyTopicCfg.setStudyId(collegeStudyTaskCfg.getStudyId());
                collegeStudyTopicCfg.setChaptersId(String.valueOf(courseChaptersCfg.getChaptersId()));
                collegeStudyTopicCfg.setStudyTopicDesc(courseChaptersCfg.getChaptersName() + "课件与习题范围关系");
                collegeStudyTopicCfg.setStudyTopicName(courseChaptersCfg.getChaptersName() + "习题");
                collegeStudyTopicCfgList.add(collegeStudyTopicCfg);
            }
            result.setCollegeCourseChaptersList(collegeCourseChaptersTaskResponseDTOList);
        }
        if (ArrayUtils.isNotEmpty(collegeStudyTopicCfgList)){
            collegeStudyTopicCfgServiceImpl.saveBatch(collegeStudyTopicCfgList);
        }
        //设置任务与岗位关系
        List<String> jobRoleInfos = collegeCourseChaptersTaskRequestDTO.getJobRoleInfos();
        List<CollegeTaskJobCfg> collegeTaskJobCfgList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(jobRoleInfos)){
            String jobType = collegeCourseChaptersTaskRequestDTO.getJobType();
            CollegeStudyTaskCfg effectiveByStudyId = this.collegeStudyTaskCfgService.getEffectiveByStudyId(collegeCourseChaptersTaskRequestDTO.getStudyId());
            String taskId = String.valueOf(effectiveByStudyId.getStudyTaskId());
            for (String jobRole : jobRoleInfos){
                CollegeTaskJobCfg collegeTaskJobCfg = new CollegeTaskJobCfg();
                collegeTaskJobCfg.setTaskId(taskId);
                collegeTaskJobCfg.setJobRole(jobRole);
                collegeTaskJobCfg.setJobType(jobType);
                collegeTaskJobCfg.setStatus("0");
                collegeTaskJobCfgList.add(collegeTaskJobCfg);
            }
            if (ArrayUtils.isNotEmpty(collegeTaskJobCfgList)){
                collegeTaskJobCfgServiceImpl.saveBatch(collegeTaskJobCfgList);
            }
        }
        String studyTypeName = "";
        if (StringUtils.isNotEmpty(studyType)){
            studyTypeName = staticDataServiceImpl.getCodeName("TASK_COURSEWARE_TYPE", studyType);
        }
        if (StringUtils.isEmpty(studyTypeName)){
            studyTypeName = studyType;
        }
        result.setStudyTypeName(studyTypeName);
        String taskType = result.getTaskType();
        String taskTypeName = staticDataServiceImpl.getCodeName("STUDY_TASK_TYPE", taskType);
        if (StringUtils.isEmpty(taskTypeName)){
            taskTypeName = taskType;
        }
        result.setTaskTypeName(taskTypeName);
        return result;
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

    @GetMapping("queryJobRoleTransferInfo")
    @RestResult
    List<JobRoleTransferResponseDTO> queryJobRoleTransferInfo(){
        List<JobRoleTransferResponseDTO> result = new ArrayList<>();
        List<StaticData> staticDatas = staticDataServiceImpl.getStaticDatas("JOB_ROLE");
        if (ArrayUtils.isNotEmpty(staticDatas)){
            for (StaticData staticData : staticDatas){
                JobRoleTransferResponseDTO jobRoleTransferResponseDTO = new JobRoleTransferResponseDTO();
                jobRoleTransferResponseDTO.setLabel(staticData.getCodeName());
                jobRoleTransferResponseDTO.setKey(staticData.getCodeValue());
                result.add(jobRoleTransferResponseDTO);
            }
        }
        return result;
    }

    @GetMapping("getCollegeStudyTaskByStudyTaskId")
    @RestResult
    CollegeStudyTaskResponseDTO getCollegeStudyTaskByStudyTaskId(String studyTaskId){
        return this.collegeStudyTaskCfgService.getCollegeStudyTaskByStudyTaskId(studyTaskId);
    }
}