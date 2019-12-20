package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeePerformanceInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeePerformance;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2019-11-20
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface EmployeePerformanceMapper extends BaseMapper<EmployeePerformance> {
    /**
     * 查询员工绩效信息
     *
     * @param page
     * @param wrapper
     * @param year
     * @return
     */
    @Select("select a.name employee_name,a.employee_id," +
            " b.job_role,b.org_id,b.job_role_nature, c.name org_name,d.year,d.performance,d.id,d.remark  from " +
            "  ins_employee_job_role b, ins_org c , ins_employee a left join ins_employee_performance d " +
            " on (a.employee_id=d.employee_id and d.year=#{year}) \n" +
            " ${ew.customSqlSegment}"
    )
    IPage<EmployeePerformanceInfoDTO> selectEmployeePerformancePage(Page<EmployeePerformanceInfoDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper,@Param("year")String year);

    /**
     * 针对导出做查询
     *
     * @param
     * @param wrapper
     * @return
     */
    @Select("select a.name employee_name,a.employee_id," +
            " b.job_role,b.org_id,b.job_role_nature, c.name org_name,d.year,d.performance,d.id,d.remark  from " +
            "  ins_employee_job_role b, ins_org c , ins_employee a left join ins_employee_performance d " +
            " on (a.employee_id=d.employee_id and d.year=#{year}) \n" +
            " ${ew.customSqlSegment}"
    )
    List<EmployeePerformanceInfoDTO> queryEmployeePerformance(@Param(Constants.WRAPPER) Wrapper wrapper, @Param("year")String year);
}
