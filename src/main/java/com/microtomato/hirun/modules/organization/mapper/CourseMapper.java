package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.organization.entity.po.Course;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2019-12-18
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface CourseMapper extends BaseMapper<Course> {

    @Select("select c.name " +
        "from ins_train_course_rel b, ins_course c " +
        "where c.course_id = b.course_id " +
        "and b.status = '0' " +
        "and c.status = '0' " +
        "and b.train_id = #{trainId} "
    )
    List<String> queryCourseNameByTrainId(Long trainId);
}
