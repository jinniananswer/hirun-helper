package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQuantityStatDTO;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
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
     *
     * @param year
     * @param orgId
     * @return
     */
    @Select("select a.month,a.org_id,sum(employee_num) as employee_num,b.parent_org_id,b.name as org_name FROM stat_employee_quantity_month a,ins_org b  " +
            "where a.org_id = b.org_id and a.year=${year} and b.org_id in (${orgId}) " +
            "GROUP BY a.org_id,a.month,b.parent_org_id,b.name ")
    List<EmployeeQuantityStatDTO> countByOrgId(@Param("year") String year, @Param("orgId") String orgId);

    /**
     * 按部门性质和岗位统计员工当月在岗以及异动信息
     *
     * @param year
     * @param orgId
     * @return
     */
    @Select("select a.org_nature,a.job_role,SUM(a.less_month_num) as less_month_num,  " +
            "SUM(a.more_month_num) as more_month_num,SUM(a.employee_num) as employee_num,IFNULL(SUM(b.employee_entry_quantity),0) as employee_entry_quantity ,IFNULL(SUM(b.employee_destroy_quantity),0) as employee_destroy_quantity, " +
            "group_concat(b.entry_employee_id) as entry_employee_ids ,group_concat(b.destroy_employee_id) as destroy_employee_ids, " +
            "IFNULL(SUM(b.employee_holiday_quantity),0) as employee_holiday_quantity,group_concat(b.holiday_employee_id) as holiday_employee_ids, " +
            "IFNULL(SUM(b.employee_trans_in_quantity),0) as employee_trans_in_quantity,group_concat(b.trans_in_employee_id) as trans_in_employee_ids, " +
            "IFNULL(SUM(b.employee_trans_out_quantity),0) as employee_trans_out_quantity,group_concat(b.trans_out_employee_id) as trans_out_employee_ids," +
            "IFNULL(SUM(b.employee_borrow_in_quantity),0) as employee_borrow_in_quantity,group_concat(b.borrow_in_employee_id) as borrow_in_employee_ids, " +
            "IFNULL(SUM(b.employee_borrow_out_quantity),0) as employee_borrow_out_quantity,group_concat(b.borrow_out_employee_id) as borrow_out_employee_ids " +
            "from stat_employee_quantity_month a LEFT JOIN stat_employee_transition b " +
            "on (a.`year`=b.`year` and a.`month`=b.`month` and a.org_id=b.org_id " +
            " and a.job_role=b.job_role and a.org_nature=b.org_nature and a.job_role_nature=b.job_role_nature and a.job_grade=b.job_grade) " +
            " where a.org_nature in (${orgNature}) and a.org_id in (${orgId}) and a.year=${year} and a.month=${month} " +
            " GROUP BY a.org_nature,a.job_role ")
    List<Map<String, String>> countByOrgNatureAndJobRole(@Param("year") String year, @Param("month") String month, @Param("orgId") String orgId,@Param("orgNature") String orgNature);

    /**
     * 统计分公司四大业务线人员
     *
     * @param year
     * @param month
     * @return
     */
    @Select("select a.org_nature,a.job_role,a.city,CAST(sum(a.employee_num) AS char) as employee_num from stat_employee_quantity_month a  " +
            "where a.org_nature in (${orgNature})  and a.org_id in (${orgId}) and a.year=${year} and a.month=${month} and a.job_role in (${jobRole}) " +
            " GROUP BY a.org_nature ,a.job_role, a.city ")
    List<Map<String, String>> companyCountByOrgNatureAndJobRoleAndCity(@Param("year") String year, @Param("month") String month, @Param("orgId") String orgId,@Param("orgNature") String orgNature,@Param("jobRole") String jobRole);

    /**
     * 按照岗位，部门性质统计年度人员趋势
     *
     * @param year
     * @param orgNature
     * @param orgId
     * @return
     */
    @Select("select a.org_nature,a.`month`,a.job_role,SUM(a.employee_num) as employee_num,IFNULL(b.employee_entry_quantity,0) as employee_entry_quantity ,IFNULL(b.employee_destroy_quantity,0) as employee_destroy_quantity " +
            "from stat_employee_quantity_month a " +
            "LEFT JOIN stat_employee_transition b " +
            "on (a.job_role=b.job_role and a.org_nature=b.org_nature and a.`month`=b.`month` and a.job_role_nature=b.job_role_nature and a.job_grade=b.job_grade) " +
            "where a.org_id in (${orgId}) and a.year=${year} and a.org_nature in (${orgNature}) " +
            "group by a.org_nature,a.job_role,a.`month`")
    List<Map<String, String>> busiCountByOrgNatureAndJobRole(@Param("year") String year, @Param("orgNature") String orgNature, @Param("orgId") String orgId);

    /**
     * 业务线与所有员工人员异动趋势
     *
     * @param year
     * @param orgId
     * @return
     */
    @Select("select CAST(x.org_id AS char) as org_id,CAST(x.parent_org_id AS char) as parent_org_id,x.month,CAST(x.all_employee_entry_quantity AS char) as all_employee_entry_quantity," +
            "CAST(x.all_employee_destroy_quantity AS char) as all_employee_destroy_quantity," +
            "CAST(y.busi_employee_entry_quantity AS char) as busi_employee_entry_quantity, " +
            "CAST(y.busi_employee_destroy_quantity AS char) as busi_employee_destroy_quantity from\n " +
            "(select a.org_id ,a.parent_org_id,b.`month`,IFNULL(SUM(b.employee_entry_quantity),0) as all_employee_entry_quantity ,IFNULL(SUM(b.employee_destroy_quantity),0) as all_employee_destroy_quantity\n " +
            "from ins_org a , stat_employee_transition b where (a.org_id=b.org_id and b.`year`=${year})" +
            "GROUP BY a.org_id,b.`month`,a.parent_org_id) x " +
            "LEFT JOIN " +
            "(select c.org_id,c.parent_org_id,d.`month`,IFNULL(SUM(d.employee_entry_quantity),0) as busi_employee_entry_quantity ,IFNULL(SUM(d.employee_destroy_quantity),0) as busi_employee_destroy_quantity " +
            "from ins_org c , stat_employee_transition d " +
            "where (c.org_id=d.org_id and d.`year`=${year} and d.org_nature in ('2','3','4','5','6','7')) " +
            "GROUP BY c.org_id,d.`month`,c.parent_org_id) y " +
            " on (x.org_id=y.org_id and x.month=y.month) " +
            " where x.org_id in (${orgId}) ")
    List<Map<String, String>> busiAndAllCountTrend(@Param("year") String year, @Param("orgId") String orgId);

}
