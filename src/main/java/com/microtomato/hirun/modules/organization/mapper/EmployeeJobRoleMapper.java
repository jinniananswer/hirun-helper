package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeOrgGroupByDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2019-10-09
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface EmployeeJobRoleMapper extends BaseMapper<EmployeeJobRole> {

    @Select("SELECT org_id, count(1) AS count FROM ins_employee_job_role WHERE now() > start_date and now() < end_date and org_id IS NOT NULL GROUP BY org_id")
    public List<EmployeeOrgGroupByDTO> countGroupByOrgId();

}
