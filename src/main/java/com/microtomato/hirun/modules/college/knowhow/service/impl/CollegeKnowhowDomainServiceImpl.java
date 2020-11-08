package com.microtomato.hirun.modules.college.knowhow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.mybatis.sequence.impl.CollegeQuestionSeq;
import com.microtomato.hirun.framework.mybatis.service.IDualService;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.college.knowhow.consts.KnowhowConsts;
import com.microtomato.hirun.modules.college.knowhow.entity.dto.QuestionServiceDTO;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestion;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestionRela;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeKnowhowDomainService;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeQuestionRelaService;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeQuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 1.知乎首页
 * 默认展示已审核的有效问题，可根据提出时间、点击量、是否已回答排序
 * 1.1.我的问题
 * 展示我提出的问题
 * 1.2.我来回答
 * 展示需要我回答的问题
 * 1.3.我来审批
 * 展示需要我审批的问题
 * 2.提问
 * 新增一条问题记录
 * 新增三条问题关系记录（记录提出人、回答人、审批人）
 * 3.回答
 * 新增一条回复记录，修改问题记录状态
 * 4.审批
 * 修改问题记录状态
 * @author huanghua
 * @date 2020-08-16
 */
@Service
public class CollegeKnowhowDomainServiceImpl implements ICollegeKnowhowDomainService {

    @Autowired
    private ICollegeQuestionService collegeQuestionService;

    @Autowired
    private ICollegeQuestionRelaService collegeQuestionRelaService;

    @Autowired
    private IDualService dualService;

    /**
     * 提问
     */
    @Override
    public void addQuestion(QuestionServiceDTO request) {
        CollegeQuestion question = CollegeQuestion.builder().build();
        BeanUtils.copyProperties(request, question);
        question.setCreateTime(TimeUtils.getCurrentLocalDateTime());
        question.setStatus(KnowhowConsts.QUESTION_STATUS_UNAPPROVED);
        Long questionId = dualService.nextval(CollegeQuestionSeq.class);

        question.setQuestionId(questionId);
        // 1.新增一条问题记录
        collegeQuestionService.save(question);
        // 2.新增三条问题关系记录
        addQuestionRelas(questionId, request.getEmployeeId(), request.getRespondent(), request.getApprovedId());

    }

    private void addQuestionRelas(Long questionId, Long employeeId, Long respondent, Long approvedId) {
        // 1.新增提出人关系
        collegeQuestionRelaService.save(CollegeQuestionRela.builder()
                .employeeId(employeeId)
                .questionId(questionId)
                .status(KnowhowConsts.NORMAL_STATUS_VALID)
                .relationType("0").build());

        // 2.新增回答人关系
        collegeQuestionRelaService.save(CollegeQuestionRela.builder()
                .employeeId(respondent)
                .questionId(questionId)
                .status(KnowhowConsts.NORMAL_STATUS_VALID)
                .relationType("1").build());

        // 1.新增审批人关系
        collegeQuestionRelaService.save(CollegeQuestionRela.builder()
                .employeeId(approvedId)
                .questionId(questionId)
                .status(KnowhowConsts.NORMAL_STATUS_VALID)
                .relationType("2").build());
    }

    /**
     * 我的问题、我来回答、我来审批
     */
    @Override
    public IPage<CollegeQuestion> queryByEmployeeIdAndRelaType(Long employeeId, String relationType, Page<CollegeQuestionRela> page) {
        List<CollegeQuestionRela> questionRelas = collegeQuestionRelaService.queryByEmployeeIdAndRelaType(employeeId, relationType);

        List<CollegeQuestion> questions = this.queryQuestionByRelas(questionRelas);
        IPage<CollegeQuestion> pages = new Page<>(page.getSize(), page.getCurrent());
        pages.setRecords(questions);
        pages.setTotal(questions.size());
        return pages;
    }

    @Override
    public IPage<CollegeQuestion> querySelfQuestion(String questionText, String sortType, Long employeeId, String relationType, String optionTag, Page<CollegeQuestionRela> page, String questionType) {
        IPage<CollegeQuestion> pages = new Page<>(page.getSize(), page.getCurrent());
        List<CollegeQuestionRela> questionRelas = collegeQuestionRelaService.queryByEmployeeIdAndRelaType(employeeId, relationType);
        if (ArrayUtils.isEmpty(questionRelas)) {
            return pages;
        }

        List<CollegeQuestion> questions = this.queryQuestionByRelas(questionRelas);
        if (StringUtils.isNotBlank(questionType)) {
            questions = questions.stream().filter(question -> StringUtils.equals(questionType, question.getQuestionType())).collect(Collectors.toList());
        }

        if (StringUtils.isNotEmpty(questionText)) {
            questions = questions.stream().filter(question -> question.getQuestionContent().contains(questionText) || question.getQuestionTitle().contains(questionText)).collect(Collectors.toList());
        }

        if (KnowhowConsts.OPTION_APPROVE.equals(optionTag)) {
            questions = questions.stream().filter(question -> KnowhowConsts.QUESTION_STATUS_UNAPPROVED.equals(question.getStatus())).collect(Collectors.toList());
        } else if (KnowhowConsts.OPTION_REPLY.equals(optionTag)) {
            questions = questions.stream().filter(question -> KnowhowConsts.QUESTION_STATUS_UNREPLY.equals(question.getStatus())).collect(Collectors.toList());
        } else if (KnowhowConsts.OPTION_PUBLISH.equals(optionTag)) {
            questions = questions.stream().filter(question -> KnowhowConsts.QUESTION_STATUS_REPLY.equals(question.getStatus())).collect(Collectors.toList());
        } else if (KnowhowConsts.OPTION_SQUARE.equals(optionTag)) {
            questions = questions.stream().filter(question -> KnowhowConsts.QUESTION_STATUS_DEPLOYED.equals(question.getStatus())).collect(Collectors.toList());
        }

        if (KnowhowConsts.QUESTION_SORT_BY_CREATTIME.equals(sortType)) {
            questions = questions.stream().sorted(Comparator.comparing(CollegeQuestion::getCreateTime).reversed()).collect(Collectors.toList());
        } else if (KnowhowConsts.QUESTION_SORT_BY_CLICKS.equals(sortType)) {
            questions = questions.stream().sorted(Comparator.comparing(CollegeQuestion::getClicks).reversed()).collect(Collectors.toList());
        }

        pages.setRecords(questions);
        pages.setTotal(questions.size());
        return pages;
    }

    @Override
    public IPage<CollegeQuestion> queryAllQuestion(Page<CollegeQuestion> page) {
        List<CollegeQuestion> questions = collegeQuestionService.list(new QueryWrapper<CollegeQuestion>().lambda());
        IPage<CollegeQuestion> pages = new Page<>(page.getSize(), page.getCurrent());
        pages.setRecords(questions);
        pages.setTotal(questions.size());
        return pages;
    }

    private List<CollegeQuestion> queryQuestionByRelas(List<CollegeQuestionRela> questionRelas) {
        List<CollegeQuestion> questions = new ArrayList<>();
        List<Long> questionIds = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(questionRelas)) {
            for (CollegeQuestionRela questionRela : questionRelas) {
                questionIds.add(questionRela.getQuestionId());
            }

            questions = collegeQuestionService.queryByQuestionIds(questionIds);
            return questions.stream().distinct().collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
