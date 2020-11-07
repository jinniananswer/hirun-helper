package com.microtomato.hirun.modules.college.knowhow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.knowhow.entity.po.CollegeReply;

import java.util.List;

/**
 * (CollegeReply)表服务接口
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-08-16 16:10:10
 */
public interface ICollegeReplyService extends IService<CollegeReply> {

    /**
     * 根据问题标识获取回复内容
     * @param questionId
     * @return
     */
    CollegeReply queryReplyByQuestionId(Long questionId);

    List<CollegeReply> queryReplysByQuestionId(Long questionId);

    /**
     * 回复内容入表
     * @param questionId
     * @param replyContent
     * @param respondent
     */
    void insertReply(Long questionId, String replyContent, Long respondent);

    /**
     * 点赞
     * @param replyId
     * @param cancelTag
     */
    void thumbsUpById(Long replyId, String cancelTag);
}