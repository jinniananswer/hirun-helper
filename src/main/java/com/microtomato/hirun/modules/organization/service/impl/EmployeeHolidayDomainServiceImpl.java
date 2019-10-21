package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.domain.EmployeeHolidayDO;
import com.microtomato.hirun.modules.organization.entity.po.*;
import com.microtomato.hirun.modules.organization.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * @program: hirun-helper
 * @description: 员工休假领域服务实现类
 **/
@Slf4j
@Service
public class EmployeeHolidayDomainServiceImpl implements IEmployeeHolidayDomainService {

    @Autowired
    private IEmployeeHolidayService employeeHolidayService;


    @Autowired
    private EmployeeHolidayDO employeeHolidayDO;

    @Override
    public IPage<EmployeeHoliday> selectEmployeeHolidayList(Long employeeId, Page<EmployeeHoliday> employeeHolidayPage) {
        IPage<EmployeeHoliday> iPage = employeeHolidayService.selectEmployeeHolidayList(employeeId, employeeHolidayPage);
        return iPage;
    }

    @Override
    public boolean addEmployeeHoliday(EmployeeHoliday employeeHoliday) {
        Integer newResult = employeeHolidayDO.newEntry(employeeHoliday);
        if (newResult != null || newResult!=0 ) {
            return true;
        }
        return false;
    }

    /**
     * 更新休假记录
     * @param employeeHoliday
     * @return
     */
    @Override
    public boolean updateEmployeeHoliday(EmployeeHoliday employeeHoliday) {
        Integer updateResult =employeeHolidayDO.updateEntry(employeeHoliday);
        if (updateResult != null || updateResult!=0 ) {
            return true;
        }
        return false;
    }


    @Override
    public boolean deleteEmployeeHoliday(EmployeeHoliday employeeHoliday) {
        employeeHoliday.setEndTime(employeeHoliday.getStartTime());
        Integer deleteResult=employeeHolidayDO.deleteEntry(employeeHoliday);
        if (deleteResult != null || deleteResult!=0 ) {
            return true;
        }
        return false;
    }
}
