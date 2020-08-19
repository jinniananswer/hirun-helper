package com.microtomato.hirun.modules.college.knowhow.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.knowhow.entity.dto.AddQuestionServiceDTO;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestion;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeQuestionRela;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeKnowhowDomainService;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeQuestionRelaService;
import com.microtomato.hirun.modules.college.knowhow.service.ICollegeQuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 提问
     */
    @Override
    public void addQuestion(AddQuestionServiceDTO request) {
        Long questionId = 0L;
        CollegeQuestion question = CollegeQuestion.builder().build();
        BeanUtils.copyProperties(request, question);
        question.setQuestionId(questionId);
        // 1.新增一条问题记录
        collegeQuestionService.save(question);
        // 2.新增三条问题关系记录
        addQuestionRelas(questionId, request.getEmployeeId(), request.getRespondent(), request.getApprovedId());

    }

    private void addQuestionRelas(Long questionId, String employeeId, String respondent, String approvedId) {
        // 1.新增提出人关系
        collegeQuestionRelaService.save(CollegeQuestionRela.builder()
                .employeeId(employeeId)
                .questionId(questionId)
                .status("1")
                .relationType("0").build());

        // 2.新增回答人关系
        collegeQuestionRelaService.save(CollegeQuestionRela.builder()
                .employeeId(respondent)
                .questionId(questionId)
                .status("1")
                .relationType("1").build());

        // 1.新增审批人关系
        collegeQuestionRelaService.save(CollegeQuestionRela.builder()
                .employeeId(approvedId)
                .questionId(questionId)
                .status("1")
                .relationType("2").build());
    }

    /**
     * 我的问题、我来回答、我来审批
     */
    @Override
    public IPage<CollegeQuestion> queryByEmployeeIdAndRelaType(String employeeId, String relationType, Page<CollegeQuestionRela> page) {
        List<CollegeQuestionRela> questionRelas = collegeQuestionRelaService.queryByEmployeeIdAndRelaType(employeeId, relationType);
        List<CollegeQuestion> questions = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(questionRelas)) {
            for (CollegeQuestionRela questionRela : questionRelas) {
                CollegeQuestion question = collegeQuestionService.getById(questionRela.getQuestionId());
                if (null != question && null != question.getQuestionId()) {
                    questions.add(question);
                }
            }
        }

        IPage<CollegeQuestion> pages = new Page<>(page.getSize(), page.getCurrent());
        pages.setRecords(questions);
        return pages;
    }
}
