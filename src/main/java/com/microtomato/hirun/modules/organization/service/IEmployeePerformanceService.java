package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeePerformanceInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeePerformance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liuhui
 * @since 2019-11-20
 */
public interface IEmployeePerformanceService extends IService<EmployeePerformance> {
    /**
     * 查询员工绩效
     *
     * @param name
     * @param orgSet
     * @param page
     * @return
     */
    IPage<EmployeePerformanceInfoDTO> queryPerformanceList(String name, String orgSet,String year,String performance,String jobRoleNature, Page<EmployeePerformanceInfoDTO> page);

    /**
     *新增绩效
     * @param employeePerformance
     */
    void addEmployeePerformance(EmployeePerformance employeePerformance);

    /**
     *更新绩效
     * @param employeePerformance
     * @return
     */
    boolean updateEmployeePerformance(EmployeePerformance employeePerformance);

    /**
     *查询绩效
     * @param employeeId
     * @param year
     * @return
     */
    List<EmployeePerformance> queryEmployeePerformance(Long employeeId, String year);

    /**
     * 查询绩效
     * @param name
     * @param orgSet
     * @param year
     * @param performance
     * @return
     */
    List<EmployeePerformanceInfoDTO> queryPerformanceList(String name, String orgSet,String year,String performance,String jobRoleNature);

}
