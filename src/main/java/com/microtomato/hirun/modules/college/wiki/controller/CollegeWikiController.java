package com.microtomato.hirun.modules.college.wiki.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.wiki.entity.dto.WikiServiceDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.microtomato.hirun.modules.college.wiki.entity.po.CollegeWiki;
import com.microtomato.hirun.modules.college.wiki.service.ICollegeWikiService;
import com.microtomato.hirun.framework.annotation.RestResult;

import java.io.Serializable;
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
}