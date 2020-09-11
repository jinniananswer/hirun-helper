package com.microtomato.hirun.modules.college.config.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeStudyExamResponseDTO;
import com.microtomato.hirun.modules.college.config.entity.dto.CollegeStudyExercisesResponseDTO;
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

    @Select("select a.study_id, a.study_name, a.study_type, b.chapters_id, b.chapters_name, \n" +
            "CASE WHEN ISNULL(b.exercises_number) then a.exercises_number ELSE b.exercises_number end as exercises_number,\n" +
            "CASE WHEN ISNULL(b.pass_score) then a.pass_score ELSE b.pass_score end as pass_score\n" +
            "from (select * from college_study_task_cfg  ${ew.customSqlSegment}) a \n" +
            "LEFT JOIN (select * from college_course_chapters_cfg where status = '0') b\n" +
            "ON a.study_id = b.study_id")
    IPage<CollegeStudyExercisesResponseDTO> queryCollegeStudyExercisesByPage(Page<CollegeStudyTaskRequestDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select a.study_id, a.study_name, a.study_type, b.chapters_id, b.chapters_name, \n" +
            "CASE WHEN ISNULL(b.exercises_number) then a.exercises_number ELSE b.exercises_number end as exercises_number,\n" +
            "CASE WHEN ISNULL(b.pass_score) then a.pass_score ELSE b.pass_score end as pass_score\n" +
            "from (select * from college_study_task_cfg  ${ew.customSqlSegment}) a \n" +
            "LEFT JOIN (select * from college_course_chapters_cfg where status = '0') b\n" +
            "ON a.study_id = b.study_id")
    IPage<CollegeStudyExamResponseDTO> queryCollegeStudyExamByPage(Page<CollegeStudyTaskRequestDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);


}