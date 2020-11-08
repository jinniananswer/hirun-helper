package com.microtomato.hirun.modules.college.task.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyTaskCfgService;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeTaskExperienceRequestDTO;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeTaskExperienceScoreResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTaskScore;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTaskTutor;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeTaskExperience;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskScoreService;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskService;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskTutorService;
import com.microtomato.hirun.modules.college.task.service.ICollegeTaskExperienceService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.INotifyService;
import com.microtomato.hirun.modules.system.service.IUploadFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * (CollegeTaskExperience)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-27 03:36:43
 */
@RestController
@RequestMapping("/api/CollegeTaskExperience")
public class CollegeTaskExperienceController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeTaskExperienceService collegeTaskExperienceService;

    @Autowired
    private ICollegeEmployeeTaskScoreService collegeEmployeeTaskScoreServiceImpl;

    @Autowired
    private ICollegeEmployeeTaskService collegeEmployeeTaskServiceImpl;

    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private INotifyService notifyService;

    @Autowired
    private IEmployeeService employeeServiceImpl;

    @Autowired
    private ICollegeEmployeeTaskTutorService collegeEmployeeTaskTutorServiceImpl;

    @Autowired
    private ICollegeStudyTaskCfgService collegeStudyTaskCfgServiceImpl;

    /**
     * 分页查询所有数据
     *
     * @param page                  分页对象
     * @param collegeTaskExperience 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeTaskExperience> selectByPage(Page<CollegeTaskExperience> page, CollegeTaskExperience collegeTaskExperience) {
        return this.collegeTaskExperienceService.page(page, new QueryWrapper<>(collegeTaskExperience));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeTaskExperience selectById(@PathVariable Serializable id) {
        return this.collegeTaskExperienceService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeTaskExperience 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeTaskExperience collegeTaskExperience) {
        return this.collegeTaskExperienceService.save(collegeTaskExperience);
    }

    /**
     * 修改数据
     *
     * @param collegeTaskExperience 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeTaskExperience collegeTaskExperience) {
        return this.collegeTaskExperienceService.updateById(collegeTaskExperience);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeTaskExperienceService.removeByIds(idList);
    }

    @GetMapping("queryByTaskId")
    @RestResult
    public CollegeTaskExperienceScoreResponseDTO queryByTaskId(String taskId){
        CollegeTaskExperienceScoreResponseDTO collegeTaskExperienceScoreResponseDTO = this.collegeTaskExperienceService.queryByTaskId(taskId);
        CollegeEmployeeTaskScore collegeEmployeeTaskScore = collegeEmployeeTaskScoreServiceImpl.getByTaskId(taskId);
        if (null != collegeEmployeeTaskScore){
            collegeTaskExperienceScoreResponseDTO.setExperienceScore(collegeEmployeeTaskScore.getExperienceScore());
            collegeTaskExperienceScoreResponseDTO.setImgScore(collegeEmployeeTaskScore.getImgScore());
        }

        CollegeEmployeeTask collegeEmployeeTask = collegeEmployeeTaskServiceImpl.getById(Long.valueOf(taskId));
        if (null != collegeEmployeeTask){
            collegeTaskExperienceScoreResponseDTO.setTaskScore(collegeEmployeeTask.getScore());
        }
        return collegeTaskExperienceScoreResponseDTO;
    }

    @PostMapping("addExperience")
    @RestResult
    public void addExperience(@RequestBody CollegeTaskExperienceRequestDTO collegeTaskExperienceRequestDTO){
        LocalDateTime now = TimeUtils.getCurrentLocalDateTime();
        String experienceImgFileId = collegeTaskExperienceRequestDTO.getExperienceImgFileId();
        List<String> experienceImgList = new ArrayList<>();
        if (StringUtils.isNotEmpty(experienceImgFileId)){
            String[] split = experienceImgFileId.split(",");
            for (String fileId : split) {
                experienceImgList.add(fileId);
            }
        }
        String taskImgFileId = collegeTaskExperienceRequestDTO.getTaskImgFileId();
        List<String> tasImgList = new ArrayList<>();
        if (StringUtils.isNotEmpty(taskImgFileId)){
            String[] split = taskImgFileId.split(",");
            for (String fileId : split) {
                tasImgList.add(fileId);
            }
        }
        this.collegeTaskExperienceService.addExperience(collegeTaskExperienceRequestDTO.getTaskId(), collegeTaskExperienceRequestDTO.getExperience(), tasImgList, experienceImgList, now);
        //实践任务完成
        CollegeEmployeeTask collegeEmployeeTask = collegeEmployeeTaskServiceImpl.getById(collegeTaskExperienceRequestDTO.getTaskId());
        if (null != collegeEmployeeTask){
            collegeEmployeeTask.setTaskCompleteDate(now);
            String employeeId = collegeEmployeeTask.getEmployeeId();
            collegeEmployeeTaskServiceImpl.updateById(collegeEmployeeTask);
            CollegeStudyTaskCfg collegeStudyTaskCfg = collegeStudyTaskCfgServiceImpl.getEffectiveByStudyTaskId(Long.valueOf(collegeEmployeeTask.getStudyTaskId()));
            if (null != collegeStudyTaskCfg){
                String taskName = collegeStudyTaskCfg.getTaskName();
                //给导师发送消息
                CollegeEmployeeTaskTutor collegeEmployeeTaskTutor = collegeEmployeeTaskTutorServiceImpl.getEffectiveByTaskId(String.valueOf(collegeTaskExperienceRequestDTO.getTaskId()));
                if (null != collegeEmployeeTaskTutor){
                    String tutorId = collegeEmployeeTaskTutor.getTutorId();
                    String hrContent = employeeServiceImpl.getEmployeeNameEmployeeId(Long.valueOf(tutorId)) + " 您好！【" + employeeId + "-" + employeeServiceImpl.getEmployeeNameEmployeeId(Long.valueOf(employeeId)) +
                            taskName + "】已完成，请尽快对任务进行评分。";
                    notifyService.sendMessage(Long.valueOf(tutorId), hrContent, 1L);
                }
            }
        }

    }

    @PostMapping("uploadImg")
    @RestResult
    public String uploadImg(@RequestParam("file") MultipartFile multipartFile, @RequestParam("taskId") Long taskId) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("上传失败，请先选择文件。");
        }
        return uploadFileService.uploadOne(multipartFile);
    }
}