package com.microtomato.hirun.modules.college.config.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeCourseTaskRequestDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeCourseTaskResponseDTO;
import com.microtomato.hirun.modules.college.config.entity.po.CollegeCourseTaskCfg;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * (CollegeCourseTaskCfg)表数据库访问层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-04 00:19:05
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface CollegeCourseTaskCfgMapper extends BaseMapper<CollegeCourseTaskCfg> {

    @Select("select * from college_course_task_cfg  ${ew.customSqlSegment}")
    IPage<CollegeCourseTaskResponseDTO> queryCollegeCourseByPage(Page<CollegeCourseTaskRequestDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}