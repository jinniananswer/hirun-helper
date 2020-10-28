package com.microtomato.hirun.modules.college.wiki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.college.wiki.entity.po.CollegeWiki;

import java.util.List;

/**
 * (CollegeWiki)表服务接口
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-28 02:20:46
 */
public interface ICollegeWikiService extends IService<CollegeWiki> {
    List<CollegeWiki> queryByText(String keyStr);

}