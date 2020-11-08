package com.microtomato.hirun.modules.college.wiki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.wiki.entity.po.CollegeWikiReply;

import java.util.List;

/**
 * (CollegeWikiReply)表服务接口
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-28 02:20:48
 */
public interface ICollegeWikiReplyService extends IService<CollegeWikiReply> {

    List<CollegeWikiReply> queryByWikiId(Long wikiId);
}