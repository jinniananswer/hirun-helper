package com.microtomato.hirun.modules.organization.mapper;

import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeTransition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2019-12-22
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface StatEmployeeTransitionMapper extends BaseMapper<StatEmployeeTransition> {
    /**
     * 按月统计异动详情
     *
     * @param year
     * @param orgId
     * @return
     */
    @Select("select a.org_id,a.job_role,  " +
            "IFNULL(SUM(a.employee_entry_quantity),0) as employee_entry_quantity ,IFNULL(SUM(a.employee_destroy_quantity),0) as employee_destroy_quantity, " +
            "group_concat(a.entry_employee_id) as entry_employee_ids ,group_concat(a.destroy_employee_id) as destroy_employee_ids, " +
            "IFNULL(SUM(a.employee_holiday_quantity),0) as employee_holiday_quantity,group_concat(a.holiday_employee_id) as holiday_employee_ids, " +
            "IFNULL(SUM(a.employee_trans_in_quantity),0) as employee_trans_in_quantity,group_concat(a.trans_in_employee_id) as trans_in_employee_ids, " +
            "IFNULL(SUM(a.employee_trans_out_quantity),0) as employee_trans_out_quantity,group_concat(a.trans_out_employee_id) as trans_out_employee_ids," +
            "IFNULL(SUM(a.employee_borrow_in_quantity),0) as employee_borrow_in_quantity,group_concat(a.borrow_in_employee_id) as borrow_in_employee_ids, " +
            "IFNULL(SUM(a.employee_borrow_out_quantity),0) as employee_borrow_out_quantity,group_concat(a.borrow_out_employee_id) as borrow_out_employee_ids " +
            "from stat_employee_transition a " +
            " where  a.org_id in (${orgId}) and a.year=${year} and a.month=${month} " +
            " GROUP BY a.org_id,a.job_role " +
            " order by a.org_id")
    List<Map<String, String>> employeeTransitionDetail(@Param("year") String year, @Param("month") String month, @Param("orgId") String orgId);

}
