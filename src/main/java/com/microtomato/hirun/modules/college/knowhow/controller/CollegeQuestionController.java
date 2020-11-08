package com.microtomato.hirun.modules.college.knowhow.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.UserContextUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.college.knowhow.consts.KnowhowConsts;
import com.microtomato.hirun.modules.college.knowhow.entity.dto.*;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestionRela;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeReply;
import com.microtomato.hirun.modules.college.knowhow.service.*;
import com.microtomato.hirun.modules.college.teacher.entity.po.Teacher;
import com.microtomato.hirun.modules.college.teacher.service.ITeacherService;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.entity.po.StaticData;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestion;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * (CollegeQuestion)表控制层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-08-16 16:14:44
 */
@RestController
@RequestMapping("/api/CollegeQuestion")
public class CollegeQuestionController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeQuestionService collegeQuestionService;

    @Autowired
    private ICollegeQuestionRelaService collegeQuestionRelaService;

    @Autowired
    private ICollegeKnowhowDomainService collegeKnowhowDomainService;

    @Autowired
    private ICollegeReplyService collegeReplyService;

    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private IStaticDataService staticDataServiceImpl;

    @Autowired
    private ICollegeQuestionReplyerCfgService collegeQuestionReplyerCfgService;

    @Autowired
    private IEmployeeService employeeService;

    /**
     * 点赞
     */
    @PostMapping("thumbsUp")
    @RestResult
    public void thumbsUp(@RequestBody QuestionServiceDTO request) {
        collegeQuestionService.thumbsUpById(request.getQuestionId(), request.getCancelTag());
    }

    /**
     * 初始化知乎首页
     */
    @GetMapping("initKnowhowPage")
    @RestResult
    public IPage<CollegeQuestion> initKnowhowPage(Page<CollegeQuestion> page) {
        Page<CollegeQuestion> results = this.collegeQuestionService.page(page, new QueryWrapper<CollegeQuestion>().lambda()
                .eq(CollegeQuestion::getStatus, KnowhowConsts.QUESTION_STATUS_DEPLOYED)
                .orderByDesc(CollegeQuestion::getCreateTime));

        return results;
    }

    /**
     * 根据条件查询
     */
    @GetMapping("queryByCond")
    @RestResult
    public IPage<CollegeQuestion> queryByCond(Page<CollegeQuestion> page, String questionText, String sortType) {
        Page<CollegeQuestion> results = this.collegeQuestionService.page(page, new QueryWrapper<CollegeQuestion>().lambda()
                .eq(CollegeQuestion::getStatus, KnowhowConsts.QUESTION_STATUS_DEPLOYED)
                .and(qw -> qw.like(CollegeQuestion::getQuestionTitle, questionText).or().like(CollegeQuestion::getQuestionContent, questionText))
                .orderByDesc(StringUtils.equals("0", sortType), CollegeQuestion::getCreateTime)
                .orderByDesc(StringUtils.equals("1", sortType), CollegeQuestion::getClicks));

        return results;
    }

    /**
     * 查询老师列表
     */
    @GetMapping("queryTeacher")
    @RestResult
    public List<Teacher> queryTeacher() {
        return teacherService.queryTeacher();
    }

    /**
     * 根据条件查询
     */
    @GetMapping("querySelfQuestion")
    @RestResult
    public IPage<CollegeQuestion> querySelfQuestion(Page<CollegeQuestionRela> page, String questionText, String sortType, String relationType, String optionTag, String questionType) {
        UserContext userContext = WebContextUtils.getUserContext();
        return this.collegeKnowhowDomainService.querySelfQuestion(questionText, sortType, userContext.getEmployeeId(), relationType, optionTag,  page, questionType);
    }

    /**
     * 根据条件查询
     */
    @GetMapping("queryAllQuestion")
    @RestResult
    public IPage<CollegeQuestion> queryAllQuestion(Page<CollegeQuestion> page) {
        return this.collegeKnowhowDomainService.queryAllQuestion(page);
    }

    /**
     * 我的问题，需要我回答的问题，需要我审批的问题
     * @param relationType
     * @return
     */
    @GetMapping("queryByEmployeeIdAndRelaType")
    @RestResult
    public IPage<CollegeQuestion> queryByEmployeeIdAndRelaType(String relationType, Page<CollegeQuestionRela> page) {
        UserContext userContext = WebContextUtils.getUserContext();
        return this.collegeKnowhowDomainService.queryByEmployeeIdAndRelaType(userContext.getEmployeeId(), relationType,  page);
    }

    /**
     * 查询问题回复内容
     * @param questionId
     * @return
     */
    @GetMapping("queryReplyByQuestionId")
    @RestResult
    public ReplyServiceDTO queryReplyByQuestionId(String questionId) {
        ReplyServiceDTO reployService = new ReplyServiceDTO();
        CollegeReply reply = this.collegeReplyService.queryReplyByQuestionId(Long.valueOf(questionId));
        if (null == reply || null == reply.getReplyId()) {
            return new ReplyServiceDTO();
        }
        BeanUtils.copyProperties(reply, reployService);

        reployService.setReplyer(teacherService.getById(reply.getRespondent()).getName());
        collegeQuestionService.updateClicksById(questionId);
        return reployService;
    }

    /**
     * 提问
     * @param request
     */
    @PostMapping("addQuestion")
    @RestResult
    public void addQuestion(@RequestBody QuestionServiceDTO request) {
        UserContext userContext = WebContextUtils.getUserContext();
        request.setEmployeeId(userContext.getEmployeeId());
        // 暂时写死审批人与回答人
        request.setApprovedId(userContext.getEmployeeId());
        request.setRespondent(request.getRespondent());
        this.collegeKnowhowDomainService.addQuestion(request);
    }

    /**
     * 问题删除，逻辑删除，状态改为已失效
     * @param request
     */
    @PostMapping("deleteQuestionByIds")
    @RestResult
    public void deleteQuestionByIds(@RequestBody List<QuestionServiceDTO> request) {
        List<Long> questionIds = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(request)) {
            for (QuestionServiceDTO req : request) {
                questionIds.add(req.getQuestionId());
            }

            this.collegeQuestionService.updateByIds(questionIds);
        }
    }

    /**
     * 问题审核
     * @param request
     */
    @PostMapping("verifyQuestion")
    @RestResult
    public void verifyQuestion(@RequestBody List<QuestionServiceDTO> request) {
        List<Long> questionIds = new ArrayList<>();
        String approvedTag = "";
        if (ArrayUtils.isNotEmpty(request)) {
            approvedTag = request.get(0).getApprovedTag();
            for (QuestionServiceDTO req : request) {
                questionIds.add(req.getQuestionId());
            }

            this.collegeQuestionService.updateApprovedTagByIds(questionIds, approvedTag);
        }
    }

    /**
     * 问题回复
     */
    @PostMapping("replyQuestion")
    @RestResult
    public void replyQuestion(@RequestBody QuestionServiceDTO request) {
        UserContext userContext = WebContextUtils.getUserContext();
        if (null != request && null != request.getQuestionId()) {
            this.collegeReplyService.insertReply(request.getQuestionId(), request.getReplyContent(), userContext.getEmployeeId());
            this.collegeQuestionService.updateReplyQuestion(request.getQuestionId());
        }
    }

    /**
     * 问题发布
     * @param request
     */
    @PostMapping("publishQuestion")
    @RestResult
    public void publishQuestion(@RequestBody List<QuestionServiceDTO> request) {
        List<Long> questionIds = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(request)) {
            for (QuestionServiceDTO req : request) {
                questionIds.add(req.getQuestionId());
            }

            this.collegeQuestionService.updatePublishQuestionByIds(questionIds);
        }
    }

    @GetMapping("queryQuestionByName")
    @RestResult
    public List<QuestionInfoDTO> queryQuestionByName(){
        return this.collegeQuestionService.queryQuestionByName(null);
    }

    @GetMapping("queryQuestionByText")
    @RestResult
    public List<QuestionInfoDTO> queryQuestionByText(@RequestParam("name") String name){
        return this.collegeQuestionService.queryQuestionByName(name);
    }

    @GetMapping("queryLoginQuestion")
    @RestResult
    public List<QuestionInfoDTO> queryLoginQuestion(){
        return this.collegeQuestionService.queryLoginQuestion(null);
    }

    @GetMapping("queryLoginQuestionByText")
    @RestResult
    public List<QuestionInfoDTO> queryLoginQuestionByText(@RequestParam("name") String name){
        return this.collegeQuestionService.queryLoginQuestion(name);
    }

    @GetMapping("queryQuestionTypeOptions")
    @RestResult
    public List<CollegeQuestionOptionsDTO> queryQuestionTypeOptions(){
        List<CollegeQuestionOptionsDTO> result = new ArrayList<>();
        List<StaticData> questionTypeList = staticDataServiceImpl.getStaticDatas("QUESTION_TYPE");
        if (ArrayUtils.isNotEmpty(questionTypeList)){
            for (StaticData staticData : questionTypeList) {
                CollegeQuestionOptionsDTO collegeQuestionOptionsDTO = new CollegeQuestionOptionsDTO();
                collegeQuestionOptionsDTO.setName(staticData.getCodeName());
                collegeQuestionOptionsDTO.setValue(staticData.getCodeValue());
                result.add(collegeQuestionOptionsDTO);
            }
        }
        return result;
    }

    @GetMapping("queryQuestionTeacherOptions")
    @RestResult
    public List<CollegeQuestionOptionsDTO> queryQuestionTeacherOptions(@RequestParam("questionType") String questionType){
        questionType = questionType.substring(questionType.indexOf("[") + 1, questionType.indexOf("]"));
        List<CollegeQuestionOptionsDTO> result = new ArrayList<>();
        List<Long> teacherIds = collegeQuestionReplyerCfgService.queryTeacherByType(questionType);

        List<EmployeeInfoDTO> teachers = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(teacherIds)) {
            teacherIds.forEach(x -> {
                EmployeeInfoDTO employee = employeeService.queryEmployeeInfoByEmployeeId(x);
                if (Objects.nonNull(employee) && null != employee.getEmployeeId()) {
                    teachers.add(employee);
                }
            });
        }
        if (ArrayUtils.isNotEmpty(teachers)){
            for (EmployeeInfoDTO teacher : teachers) {
                CollegeQuestionOptionsDTO collegeQuestionOptionsDTO = new CollegeQuestionOptionsDTO();
                collegeQuestionOptionsDTO.setName(teacher.getName());
                collegeQuestionOptionsDTO.setValue(String.valueOf(teacher.getEmployeeId()));
                result.add(collegeQuestionOptionsDTO);
            }
        }
        return result;
    }

    @GetMapping("queryWikiOptions")
    @RestResult
    public List<CollegeQuestionOptionsDTO> queryWikiOptions(){
        List<CollegeQuestionOptionsDTO> result = new ArrayList<>();
        List<StaticData> questionTypeList = staticDataServiceImpl.getStaticDatas("WIKI_TYPE");
        if (ArrayUtils.isNotEmpty(questionTypeList)){
            for (StaticData staticData : questionTypeList) {
                CollegeQuestionOptionsDTO collegeQuestionOptionsDTO = new CollegeQuestionOptionsDTO();
                collegeQuestionOptionsDTO.setName(staticData.getCodeName());
                collegeQuestionOptionsDTO.setValue(staticData.getCodeValue());
                result.add(collegeQuestionOptionsDTO);
            }
        }
        return result;
    }

    @PostMapping("addQuestionByType")
    @RestResult
    public void addQuestionByType(@RequestParam("questionType") String questionType, @RequestParam("teacherId") String teacherId, @RequestParam("title") String title, @RequestParam("desc") String desc){
        UserContext userContext = WebContextUtils.getUserContext();
        if (userContext == null) {
            userContext = UserContextUtils.getUserContext();
        }
        Long employeeId = userContext.getEmployeeId();
        teacherId = teacherId.substring(teacherId.indexOf("[") + 1, teacherId.indexOf("]"));
        questionType = questionType.substring(questionType.indexOf("[") + 1, questionType.indexOf("]"));

        QuestionServiceDTO request = new QuestionServiceDTO();
        request.setEmployeeId(userContext.getEmployeeId());
        // 暂时写死审批人与回答人
        request.setApprovedId(userContext.getEmployeeId());
        request.setRespondent(Long.valueOf(teacherId));
        request.setQuestionContent(desc);
        request.setQuestionTitle(title);
        request.setQuestionType(questionType);
        this.collegeKnowhowDomainService.addQuestion(request);
    }

    @GetMapping("queryQuestionByQuestionType")
    @RestResult
    public List<QuestionInfoDTO> queryQuestionByQuestionType(@RequestParam("questionType") String questionType){
        return this.collegeQuestionService.queryQuestionByQuestionType(questionType);
    }

    @GetMapping("getQuestionById")
    @RestResult
    public CollegeQuestionServiceDTO getQuestionById(@RequestParam("questionId") Long questionId) {
        return this.collegeQuestionService.getQuestionById(questionId);
    }

    @PostMapping("replyThumbsUp")
    @RestResult
    public void replyThumbsUp(@RequestParam("replyId") Long replyId, @RequestParam("cancelTag") String cancelTag) {
        this.collegeReplyService.thumbsUpById(replyId, cancelTag);
    }

    @PostMapping("addClick")
    @RestResult
    public void addClick(@RequestParam("questionId") Long questionId) {
        CollegeQuestion question = collegeQuestionService.getById(questionId);
        if (null == question || null == question.getQuestionId()) {
            return;
        }

        question.setClicks(question.getClicks() + 1);
        collegeQuestionService.updateById(question);
    }

    @PostMapping("addThumbsUp")
    @RestResult
    public void addThumbsUp(@RequestParam("questionId") Long questionId, @RequestParam("cancelTag") String cancelTag) {
        CollegeQuestion question = collegeQuestionService.getById(questionId);
        if (null == question || null == question.getQuestionId()) {
            return;
        }

        if (StringUtils.equals("0", cancelTag)) {
            question.setThumbsUp(question.getThumbsUp() + 1);
        } else {
            question.setThumbsUp(question.getThumbsUp() - 1);
        }
        collegeQuestionService.updateById(question);
    }
}