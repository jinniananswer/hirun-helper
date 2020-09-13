package com.microtomato.hirun.modules.college.task.mapper;

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
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployeeTaskDetailRequestDTO;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployeeTaskDetailResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployeeTaskQueryRequestDTO;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeEmployeeTaskQueryResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeEmployeeTask;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * (CollegeEmployeeTask)表数据库访问层
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-08-04 00:05:49
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface CollegeEmployeeTaskMapper extends BaseMapper<CollegeEmployeeTask> {
    @Select("select employee_id from college_employee_task  ${ew.customSqlSegment}")
    IPage<CollegeEmployeeTaskQueryResponseDTO> queryEmployeeTask(Page<CollegeEmployeeTaskQueryRequestDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select * from college_employee_task  ${ew.customSqlSegment}")
    IPage<CollegeEmployeeTaskDetailResponseDTO> queryEmployeeTaskDetailByPage(Page<CollegeEmployeeTaskDetailRequestDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);

}