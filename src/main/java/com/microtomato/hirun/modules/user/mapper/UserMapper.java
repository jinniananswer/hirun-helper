package com.microtomato.hirun.modules.user.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2019-07-29
 */
@Mapper
@DS("ins")
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT c.org_id FROM ins_user a, ins_employee b, ins_employee_job_role c ${ew.customSqlSegment}")
    Long queryOrgIdByUserId(@Param(Constants.WRAPPER) Wrapper wrapper);
}
