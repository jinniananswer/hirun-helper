package com.microtomato.hirun.modules.organization.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeHoliday;
import com.microtomato.hirun.modules.organization.entity.po.HrPending;
import com.microtomato.hirun.modules.organization.service.IHrPendingDomainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.organization.service.IHrPendingService;

import java.sql.Wrapper;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2019-10-22
 */
@RestController
@Slf4j
@RequestMapping("api/organization/hr-pending")
public class HrPendingController {

    @Autowired
    private IHrPendingService hrPendingServiceImpl;

    @Autowired
    private IHrPendingDomainService hrPendingDomainService;

    @GetMapping("/queryHrPendingListByEmployeeId")
    @RestResult
    public IPage<HrPending> queryEmployeeTransList(Long employeeId, Integer page, Integer limit) {
        Page<HrPending> hrPendingPage = new Page<HrPending>(page, limit);
        QueryWrapper<HrPending> hrPendingWrapper=new QueryWrapper<>();
        IPage<HrPending> employeeHolidayIpage = hrPendingServiceImpl.page(hrPendingPage,hrPendingWrapper);
        return employeeHolidayIpage;
    }

    /**
     * 新增待办记录
     */
    @PostMapping("/addHrPending")
    @RestResult
    public boolean addHrPending(HrPending hrPending) {
        hrPendingDomainService.addHrPending(hrPending);
        return true;
    }

    /**
     * 修改待办记录
     */
    @PostMapping("/updateHrPending")
    @RestResult
    public boolean updateHrPending(EmployeeHoliday employeeHoliday) {
        return true;
    }

    /**
     * 删除待办
     */
    @PostMapping("/deleteHrPending")
    @RestResult
    public boolean deleteHrPending(EmployeeHoliday employeeHoliday) {
        return true;
    }

    }
