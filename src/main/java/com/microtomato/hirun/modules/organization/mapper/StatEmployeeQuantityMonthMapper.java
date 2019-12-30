package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQuantityStatDTO;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2019-12-17
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface StatEmployeeQuantityMonthMapper extends BaseMapper<StatEmployeeQuantityMonth> {

    /**
     * 按最部门统计员工数量信息
     * @param year
     * @param orgId
     * @return
     */
    @Select("select a.month,a.org_id,sum(employee_sum) as employee_sum,b.parent_org_id,b.name as org_name FROM stat_employee_quantity_month a,ins_org b  " +
            "where a.org_id = b.org_id and a.year=${year} and b.org_id in (${orgId}) " +
            "GROUP BY a.org_id,a.month,b.parent_org_id,b.name ")
    List<EmployeeQuantityStatDTO> countByOrgId(@Param("year")String year, @Param("orgId")String orgId);

}
