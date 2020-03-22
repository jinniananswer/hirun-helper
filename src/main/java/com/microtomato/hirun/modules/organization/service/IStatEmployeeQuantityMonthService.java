package com.microtomato.hirun.modules.organization.service;

import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQuantityStatDTO;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liuhui
 * @since 2019-12-17
 */
public interface IStatEmployeeQuantityMonthService extends IService<StatEmployeeQuantityMonth> {
    /**
     * 查询部门在某年某月的在职人员记录
     *
     * @param year
     * @param month
     * @param orgId
     * @return
     */
    StatEmployeeQuantityMonth queryCountRecord(String year, String month, Long orgId,String jobRole,String jobRoleNature,String orgNature,String jobGrade);

    /**
     * 查询部门在职人员数记录
     * @param year
     * @param orgId
     * @return
     */
    List<EmployeeQuantityStatDTO> queryEmployeeQuantityStat(String year, String orgId);

    /**
     * 查询员工异动信息汇总
     * @param time
     * @param orgId
     * @return
     */
    List<Map<String,String>> queryEmployeeTrendsStat(String time,String orgId,String orgNature) throws Exception;

    /**
     *家装分公司四大业务类人员汇总情况
     * @param time
     * @return
     */
    List<Map<String,String>> queryEmployeeCompanyStat(String time,String orgNature,String jobRole);

    /**
     *按照部门性质，岗位出四大业务类人员人数趋势
     * @param time
     * @return
     */
    List<Map<String,String>> busiCountByOrgNatureAndJobRole(String time,String orgId,String orgNature);

    /**
     *四大业务类人员与部门所有员工异动人数趋势
     * @param time
     * @return
     */
    List<Map<String,String>> busiAndAllCountTrend(String time,String orgId);

    /**
     * 重新加载在岗人数数据
     */
    void reloadCount();
}
