package com.microtomato.hirun.modules.college.wiki.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.UserContextUtils;
import com.microtomato.hirun.modules.college.wiki.entity.dto.WikiDetailServiceDTO;
import com.microtomato.hirun.modules.college.wiki.entity.dto.WikiServiceDTO;
import com.microtomato.hirun.modules.college.wiki.entity.po.CollegeWikiReply;
import com.microtomato.hirun.modules.college.wiki.service.ICollegeWikiReplyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.college.wiki.entity.po.CollegeWiki;
import com.microtomato.hirun.modules.college.wiki.service.ICollegeWikiService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * (CollegeWiki)表控制层
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-28 02:20:47
 */
@RestController
@RequestMapping("/api/CollegeWiki")
public class CollegeWikiController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeWikiService collegeWikiService;

    @Autowired
    private ICollegeWikiReplyService collegeWikiReplyService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param collegeWiki 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeWiki> selectByPage(Page<CollegeWiki> page, CollegeWiki collegeWiki) {
        return this.collegeWikiService.page(page, new QueryWrapper<>(collegeWiki));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeWiki selectById(@PathVariable Serializable id) {
        return this.collegeWikiService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeWiki 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeWiki collegeWiki) {
        return this.collegeWikiService.save(collegeWiki);
    }

    /**
     * 修改数据
     *
     * @param collegeWiki 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeWiki collegeWiki) {
        return this.collegeWikiService.updateById(collegeWiki);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeWikiService.removeByIds(idList);
    }

    @GetMapping("queryByText")
    @RestResult
    public List<CollegeWiki> queryByText(@RequestParam("keyStr") String keyStr) {
        keyStr = StringUtils.equals("1", keyStr) ? "" : keyStr;
        List<CollegeWiki> collegeWikis = collegeWikiService.queryByText(keyStr);

        return collegeWikis;
    }

    @PostMapping("addWiki")
    @RestResult
    public void addWiki(@RequestParam("wikiTitle") String wikiTitle, @RequestParam("wikiContent") String wikiContent, @RequestParam("wikiType") String wikiType) {
        UserContext context = UserContextUtils.getUserContext();
        CollegeWiki wiki = CollegeWiki.builder()
                .wikiTitle(wikiTitle)
                .wikiContent(wikiContent)
                .wikiType(wikiType)
                .status("0")
                .clicks(0L)
                .thumbsUp(0L)
                .author(context.getEmployeeId())
                .wikiType(wikiType.substring(wikiType.indexOf("[") + 1, wikiType.indexOf("]"))).build();
        this.collegeWikiService.save(wiki);
    }

    @GetMapping("queryWikiByType")
    @RestResult
    public List<WikiServiceDTO> queryWikiByType(@RequestParam("wikiType") String wikiType) {
        return this.collegeWikiService.queryWikiByType(wikiType);
    }

    @GetMapping("getDetailByWikiId")
    @RestResult
    public WikiDetailServiceDTO getDetailByWikiId(@RequestParam("wikiId") Long wikiId) {
        return this.collegeWikiService.getDetailByWikiId(wikiId);
    }

    @PostMapping("replyWiki")
    @RestResult
    public void replyWiki(@RequestParam("wikiId") Long wikiId, @RequestParam("replyContent") String replyContent) {
        UserContext context = UserContextUtils.getUserContext();
        CollegeWikiReply reply = CollegeWikiReply.builder()
                .wikiId(wikiId)
                .replyContent(replyContent)
                .status("0")
                .thumbsUp(0L)
                .replyTime(LocalDateTime.now())
                .respondent(context.getEmployeeId()).build();

        collegeWikiReplyService.save(reply);
    }

    @PostMapping("addClick")
    @RestResult
    public void addClick(@RequestParam("wikiId") Long wikiId) {
        CollegeWiki wiki = collegeWikiService.getById(wikiId);
        if (null == wiki || null == wiki.getWikiId()) {
            return;
        }

        wiki.setClicks(wiki.getClicks() + 1);
        collegeWikiService.updateById(wiki);
    }

    @PostMapping("thumbsUp")
    @RestResult
    public void thumbsUp(@RequestParam("wikiId") Long wikiId, @RequestParam("cancelTag") String cancelTag) {
        CollegeWiki wiki = collegeWikiService.getById(wikiId);
        if (null == wiki || null == wiki.getWikiId()) {
            return;
        }

        if (StringUtils.equals("0", cancelTag)) {
            wiki.setThumbsUp(wiki.getThumbsUp() + 1);
        } else {
            wiki.setThumbsUp(wiki.getThumbsUp() - 1);
        }
        collegeWikiService.updateById(wiki);
    }
}