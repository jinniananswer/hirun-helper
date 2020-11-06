package com.microtomato.hirun.modules.college.task.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeExamCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeExamRelCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeExamCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeExamRelCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyTaskCfgService;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeTaskExamDetailResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.dto.ExamTopicResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeStudyTopicRel;
import com.microtomato.hirun.modules.college.task.service.ICollegeEmployeeTaskService;
import com.microtomato.hirun.modules.college.task.service.ICollegeStudyTopicRelService;
import com.microtomato.hirun.modules.college.topic.entity.po.CollegeTopicLabelRel;
import com.microtomato.hirun.modules.college.topic.service.ICollegeTopicLabelRelService;
import com.microtomato.hirun.modules.organization.service.ICourseService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import com.microtomato.hirun.modules.system.service.IUploadFileService;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.college.task.entity.po.CollegeTaskScore;
import com.microtomato.hirun.modules.college.task.service.ICollegeTaskScoreService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * (CollegeTaskScore)表控制层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-22 04:16:25
 */
@RestController
@RequestMapping("/api/CollegeTaskScore")
public class CollegeTaskScoreController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeTaskScoreService collegeTaskScoreService;

    @Autowired
    private ICollegeStudyTaskCfgService collegeStudyTaskCfgServiceImpl;

    @Autowired
    private ICollegeEmployeeTaskService collegeEmployeeTaskServiceImpl;

    @Autowired
    private ICollegeExamCfgService collegeExamCfgServiceImpl;

    @Autowired
    private IUploadFileService uploadFileServiceImpl;

    @Autowired
    private ICourseService courseServiceImpl;

    @Autowired
    private ICollegeExamRelCfgService collegeExamRelCfgServiceImpl;

    @Autowired
    private IStaticDataService staticDataServiceImpl;

    @Autowired
    private ICollegeStudyTopicRelService collegeStudyTopicRelServiceImpl;

    @Autowired
    private ICollegeTopicLabelRelService collegeTopicLabelRelServiceImpl;
    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param collegeTaskScore 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeTaskScore> selectByPage(Page<CollegeTaskScore> page, CollegeTaskScore collegeTaskScore) {
        return this.collegeTaskScoreService.page(page, new QueryWrapper<>(collegeTaskScore));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeTaskScore selectById(@PathVariable Serializable id) {
        return this.collegeTaskScoreService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeTaskScore 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeTaskScore collegeTaskScore) {
        return this.collegeTaskScoreService.save(collegeTaskScore);
    }

    /**
     * 修改数据
     *
     * @param collegeTaskScore 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeTaskScore collegeTaskScore) {
        return this.collegeTaskScoreService.updateById(collegeTaskScore);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeTaskScoreService.removeByIds(idList);
    }

    @PostMapping("addScore")
    @RestResult
    public boolean addScore(@RequestParam("taskId") String taskId,@RequestParam("scoreType")  String scoreType,@RequestParam("score") String score) {
        LocalDateTime now = TimeUtils.getCurrentLocalDateTime();
        boolean save = this.collegeTaskScoreService.save(CollegeTaskScore.builder()
                .taskId(Long.parseLong(taskId))
                .scoreType(scoreType)
                .score(Integer.parseInt(score))
                .time(LocalDateTime.now()).build());
        if (save){
            CollegeEmployeeTask collegeEmployeeTask = collegeEmployeeTaskServiceImpl.getById(taskId);
            if (null != collegeEmployeeTask){
                if (StringUtils.equals("0", scoreType)){
                    Integer exercisesCompletedNumber = collegeEmployeeTask.getExercisesCompletedNumber();
                    collegeEmployeeTask.setExercisesCompletedNumber(null != exercisesCompletedNumber ? exercisesCompletedNumber + 1 : 1);
                }else if (StringUtils.equals("1", scoreType)){
                    Integer examScore = collegeEmployeeTask.getExamScore();
                    if (null != examScore){
                        //设置最高分数
                        if (examScore < Integer.valueOf(score)){
                            collegeEmployeeTask.setExamScore(Integer.valueOf(score));
                        }
                    }
                    CollegeStudyTaskCfg collegeStudyTaskCfg = collegeStudyTaskCfgServiceImpl.getAllByStudyTaskId(Long.valueOf(collegeEmployeeTask.getStudyTaskId()));
                    if (null != collegeStudyTaskCfg){
                        //考试通过视为完成
                        CollegeExamCfg collegeExamCfg = collegeExamCfgServiceImpl.getByStudyTaskIdAndExamType(String.valueOf(collegeStudyTaskCfg.getStudyTaskId()), "1");
                        if (null != collegeExamCfg){
                            Integer passScore = collegeExamCfg.getPassScore();
                            if (null != passScore){
                                if (Integer.valueOf(score) >= passScore){
                                    LocalDateTime taskCompleteDate = collegeEmployeeTask.getTaskCompleteDate();
                                    if (null == taskCompleteDate){
                                        collegeEmployeeTask.setTaskCompleteDate(now);
                                    }
                                } else {
                                    save = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        return save;
    }

    @GetMapping("getExamDetailByTaskId")
    @RestResult
    public CollegeTaskExamDetailResponseDTO getExamDetailByTaskId(@RequestParam("taskId") Long taskId, @RequestParam("examType") String examType){
        CollegeTaskExamDetailResponseDTO responseDTO = new CollegeTaskExamDetailResponseDTO();
        int examScoreNumByTaskId = this.collegeTaskScoreService.getExamScoreNumByTaskId(taskId, examType);
        responseDTO.setCurrentExamNum(examScoreNumByTaskId);
        CollegeEmployeeTask collegeEmployeeTask = collegeEmployeeTaskServiceImpl.getById(taskId);
        Integer examMaxNum = 0;
        if (null != collegeEmployeeTask){
            String studyTaskId = collegeEmployeeTask.getStudyTaskId();
            CollegeExamCfg collegeExamCfg = collegeExamCfgServiceImpl.getByStudyTaskIdAndExamType(studyTaskId, examType);
            if (null != collegeExamCfg){
                String examDesc = "本次";
                String examTypeName = "";
                if (StringUtils.equals("1", examType)){
                    examMaxNum = null != collegeExamCfg.getExamMaxNum() ? collegeExamCfg.getExamMaxNum() : 0;
                    examTypeName = "考试";
                }else {
                    examTypeName = "练习";
                }
                examDesc += examTypeName;
                Integer passScore = collegeExamCfg.getPassScore();
                CollegeStudyTaskCfg allByStudyTaskId = collegeStudyTaskCfgServiceImpl.getAllByStudyTaskId(Long.valueOf(studyTaskId));
                if (null != allByStudyTaskId){
                    String studyType = allByStudyTaskId.getStudyType();
                    String studyId = allByStudyTaskId.getStudyId();
                    String studyName = "";
                    if (StringUtils.equals("1", studyType)){
                        studyName = uploadFileServiceImpl.getFileNameByFileId(studyId);
                    }else {
                        studyName = courseServiceImpl.getCourseNameByCourseId(Long.valueOf(studyId));
                    }
                    examDesc += "为" + studyName + "学习任务";
                }
                examDesc += examTypeName;
                if (StringUtils.equals("1", examType)){
                    examDesc += "合格分数为：" + (null != passScore ? passScore : 0);
                }
                responseDTO.setExamDesc(examDesc);

                List<CollegeExamRelCfg> collegeExamRelCfgs = collegeExamRelCfgServiceImpl.queryByExamTopicId(collegeExamCfg.getExamTopicId());
                if (ArrayUtils.isNotEmpty(collegeExamRelCfgs)){
                    List<ExamTopicResponseDTO> examTopicList = new ArrayList<>();
                    for (CollegeExamRelCfg collegeExamRelCfg : collegeExamRelCfgs) {
                        ExamTopicResponseDTO examTopicResponseDTO = new ExamTopicResponseDTO();
                        examTopicResponseDTO.setTopicNum(collegeExamRelCfg.getTopicNum());
                        String topicType = collegeExamRelCfg.getTopicType();
                        String topicTypeName = staticDataServiceImpl.getCodeName("EXERCISES_TYPE", topicType);
                        if (StringUtils.isEmpty(topicTypeName)){
                            topicTypeName = topicType;
                        }
                        examTopicResponseDTO.setTopicType(topicTypeName);
                        Integer examScore = 0;
                        if (StringUtils.equals("1", topicType) || StringUtils.equals("3", topicType)){
                            examScore = 2;
                        }else {
                            examScore = 4;
                        }
                        examTopicResponseDTO.setTopicScore(examScore);
                        examTopicList.add(examTopicResponseDTO);
                    }
                    responseDTO.setExamTopicList(examTopicList);

                    String studyId = allByStudyTaskId.getStudyId();
                    List<Long> labelIdList = new ArrayList<>();
                    if (StringUtils.isNotEmpty(studyId)){
                        List<CollegeStudyTopicRel> collegeStudyTopicRelList = collegeStudyTopicRelServiceImpl.getEffectiveByStudyId(studyId);
                        if (ArrayUtils.isNotEmpty(collegeStudyTopicRelList)){
                            for (CollegeStudyTopicRel collegeStudyTopicRel : collegeStudyTopicRelList) {
                                Long labelId = collegeStudyTopicRel.getLabelId();
                                labelIdList.add(labelId);
                            }
                        }
                    }
                    if (ArrayUtils.isNotEmpty(labelIdList)){
                        List<CollegeTopicLabelRel> collegeTopicLabelRels = collegeTopicLabelRelServiceImpl.queryEffectiveByLabelIdList(labelIdList);
                        if (ArrayUtils.isNotEmpty(collegeTopicLabelRels)){
                            //responseDTO.setTopicFlag(true);
                        }
                    }

                }
            }
        }
        responseDTO.setMaxExamNum(examMaxNum);

        return responseDTO;
    }
}