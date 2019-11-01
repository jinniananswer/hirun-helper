package com.microtomato.hirun.modules.organization.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeHoliday;
import com.microtomato.hirun.modules.organization.service.IEmployeeHolidayDomainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2019-10-20
 */
@RestController
@Slf4j
@RequestMapping("api/organization/employee-holiday")
public class EmployeeHolidayController {


    @Autowired
    IEmployeeHolidayDomainService employeeHolidayDomainService;

    /**
     * 查询休假记录
     */
    @GetMapping("/queryEmployeeHoliday")
    @RestResult
    public IPage<EmployeeHoliday> queryEmployeeHoliday(Long employeeId, Integer page, Integer limit) {
        Page<EmployeeHoliday> employeeHolidayPage = new Page<EmployeeHoliday>(page, limit);
        IPage<EmployeeHoliday> employeeHolidayIpage = employeeHolidayDomainService.selectEmployeeHolidayList(employeeId, employeeHolidayPage);
        return employeeHolidayIpage;
    }

    /**
     * 新增休假记录
     */
    @PostMapping("/addEmployeeHoliday")
    @RestResult
    public boolean addEmployeeHoliday(EmployeeHoliday employeeHoliday) {
        //判断前台复选框是否选择休假期间购买保险，0否，1是
        if (StringUtils.equals(employeeHoliday.getIsSurrenderInsurance(), "on")) {
            employeeHoliday.setIsSurrenderInsurance("1");
        } else {
            employeeHoliday.setIsSurrenderInsurance("0");
        }
        boolean addRestult = employeeHolidayDomainService.addEmployeeHoliday(employeeHoliday);
        return addRestult;
    }

    /**
     * 修改休假记录
     */
    @PostMapping("/updateEmployeeHoliday")
    @RestResult
    public boolean updateEmployeeHoliday(EmployeeHoliday employeeHoliday) {
        //判断前台复选框是否选择休假期间购买保险，，0否，1是
        if (StringUtils.equals(employeeHoliday.getIsSurrenderInsurance(), "on")) {
            employeeHoliday.setIsSurrenderInsurance("1");
        } else {
            employeeHoliday.setIsSurrenderInsurance("0");
        }

        boolean updateRestult = employeeHolidayDomainService.updateEmployeeHoliday(employeeHoliday);
        return updateRestult;
    }

    /**
     * 删除休假记录
     */
    @PostMapping("/deleteEmployeeHoliday")
    @RestResult
    public boolean deleteEmployeeHoliday(EmployeeHoliday employeeHoliday) {
        return employeeHolidayDomainService.deleteEmployeeHoliday(employeeHoliday);
    }
}
