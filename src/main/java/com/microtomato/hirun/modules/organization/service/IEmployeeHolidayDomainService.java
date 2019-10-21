package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeDestroyInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeHoliday;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工休假领域服务接口
 **/
public interface IEmployeeHolidayDomainService {

    IPage<EmployeeHoliday> selectEmployeeHolidayList(Long employeeId,Page<EmployeeHoliday> employeeHolidayPage);

    /**
     *
     * @param employeeHoliday
     * @return
     */
    boolean addEmployeeHoliday(EmployeeHoliday employeeHoliday);

    /**
     *
     * @param employeeHoliday
     * @return
     */
    boolean updateEmployeeHoliday(EmployeeHoliday employeeHoliday);

    /**
     *
     */
    boolean deleteEmployeeHoliday(EmployeeHoliday employeeHoliday);
}
