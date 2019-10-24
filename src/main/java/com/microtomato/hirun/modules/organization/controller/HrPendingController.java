package com.microtomato.hirun.modules.organization.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.po.HrPending;
import com.microtomato.hirun.modules.organization.service.IHrPendingDomainService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;



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
    private IHrPendingDomainService hrPendingDomainService;

    @GetMapping("/queryPendingByEmployeeId")
    @RestResult
    public IPage<HrPending> queryPendingByEmployeeId(Long employeeId, Integer page, Integer limit) {
        Page<HrPending> hrPendingPage = new Page<HrPending>(page, limit);
        IPage<HrPending> iPage = hrPendingDomainService.queryPendingByEmployeeId(employeeId,hrPendingPage);
        return iPage;
    }

    /**
     * 新增待办记录·
     */
    @PostMapping("/addHrPending")
    @RestResult
    public boolean addHrPending(HrPending hrPending) {
        Boolean result=hrPendingDomainService.addHrPending(hrPending);
        return result;
    }

    /**
     * 修改待办记录
     */
    @PostMapping("/updateHrPending")
    @RestResult
    public boolean updateHrPending(HrPending hrPending) {
        return true;
    }

    /**
     * 删除待办
     */
    @PostMapping("/deleteHrPending")
    @RestResult
    public boolean deleteHrPending(HrPending hrPending) {
        return hrPendingDomainService.deleteHrPending(hrPending);
    }

    }
