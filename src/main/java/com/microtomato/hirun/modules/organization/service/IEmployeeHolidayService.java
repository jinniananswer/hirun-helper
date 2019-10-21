package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeHoliday;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2019-10-20
 */
public interface IEmployeeHolidayService extends IService<EmployeeHoliday> {
    IPage<EmployeeHoliday> selectEmployeeHolidayList(Long employeeId, Page<EmployeeHoliday> employeeHolidayPage);
}
