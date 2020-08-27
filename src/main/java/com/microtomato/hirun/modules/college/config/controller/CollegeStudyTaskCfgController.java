package com.microtomato.hirun.modules.college.config.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeCourseChaptersTaskRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeStudyTaskRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeStudyTaskResponseDTO;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseChaptersCfg;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import com.microtomato.hirun.modules.college.config.service.ICollegeCourseChaptersCfgService;
import com.microtomato.hirun.modules.college.config.service.ICollegeStudyTaskCfgService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * (CollegeStudyTaskCfg)表控制层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-26 02:04:48
 */
@RestController
@RequestMapping("/api/CollegeStudyTaskCfg")
public class CollegeStudyTaskCfgController {

    /**
     * 服务对象
     */
    @Autowired
    private ICollegeStudyTaskCfgService collegeStudyTaskCfgService;

    @Autowired
    private ICollegeCourseChaptersCfgService collegeCourseChaptersCfgServiceImpl;

    /**
     * 分页查询所有数据
     *
     * @param page                分页对象
     * @param collegeStudyTaskCfg 查询实体
     * @return 所有数据
     */
    @GetMapping("selectByPage")
    @RestResult
    public IPage<CollegeStudyTaskCfg> selectByPage(Page<CollegeStudyTaskCfg> page, CollegeStudyTaskCfg collegeStudyTaskCfg) {
        return this.collegeStudyTaskCfgService.page(page, new QueryWrapper<>(collegeStudyTaskCfg));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectById/{id}")
    @RestResult
    public CollegeStudyTaskCfg selectById(@PathVariable Serializable id) {
        return this.collegeStudyTaskCfgService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param collegeStudyTaskCfg 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody CollegeStudyTaskCfg collegeStudyTaskCfg) {
        return this.collegeStudyTaskCfgService.save(collegeStudyTaskCfg);
    }

    /**
     * 修改数据
     *
     * @param collegeStudyTaskCfg 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public boolean update(@RequestBody CollegeStudyTaskCfg collegeStudyTaskCfg) {
        return this.collegeStudyTaskCfgService.updateById(collegeStudyTaskCfg);
    }

    /**
     * 删除数据
     *
     * @param idList 主键集合
     * @return 删除结果
     */
    @GetMapping("deleteByIds")
    public boolean deleteByIds(@RequestParam("idList") List<Long> idList) {
        return this.collegeStudyTaskCfgService.removeByIds(idList);
    }

    @GetMapping("queryCollegeStufyByPage")
    @RestResult
    IPage<CollegeStudyTaskResponseDTO> queryCollegeStufyByPage(CollegeStudyTaskRequestDTO collegeStudyTaskRequestDTO){
        Page<CollegeStudyTaskRequestDTO> page = new Page<>(collegeStudyTaskRequestDTO.getPage(), collegeStudyTaskRequestDTO.getLimit());
        return this.collegeStudyTaskCfgService.queryCollegeStufyByPage(collegeStudyTaskRequestDTO, page);
    }

    @PostMapping("addStudyTaskCfg")
    public void addStudyTaskCfg(@RequestBody CollegeCourseChaptersTaskRequestDTO collegeCourseChaptersTaskRequestDTO){
        CollegeStudyTaskCfg collegeStudyTaskCfg = new CollegeStudyTaskCfg();
        BeanUtils.copyProperties(collegeCourseChaptersTaskRequestDTO, collegeStudyTaskCfg);
        collegeStudyTaskCfg.setStatus("0");
        this.collegeStudyTaskCfgService.save(collegeStudyTaskCfg);
        List<CollegeCourseChaptersCfg> courseChaptersList = collegeCourseChaptersTaskRequestDTO.getCourseChaptersList();
        if (ArrayUtils.isNotEmpty(courseChaptersList)){
            for (CollegeCourseChaptersCfg courseChaptersCfg : courseChaptersList){
                courseChaptersCfg.setStatus("0");
            }
            collegeCourseChaptersCfgServiceImpl.saveBatch(courseChaptersList);
        }
    }
}