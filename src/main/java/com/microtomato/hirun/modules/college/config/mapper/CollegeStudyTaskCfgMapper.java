package com.microtomato.hirun.modules.college.config.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeStudyTaskRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeStudyTaskResponseDTO;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeStudyTaskCfg;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * (CollegeStudyTaskCfg)表数据库访问层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-26 02:04:48
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface CollegeStudyTaskCfgMapper extends BaseMapper<CollegeStudyTaskCfg> {

    @Select("select * from college_study_task_cfg  ${ew.customSqlSegment}")
    IPage<CollegeStudyTaskResponseDTO> queryCollegeStudyByPage(Page<CollegeStudyTaskRequestDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}