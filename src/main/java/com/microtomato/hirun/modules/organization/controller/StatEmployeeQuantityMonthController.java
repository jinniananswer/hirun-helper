package com.microtomato.hirun.modules.organization.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.organization.service.IStatEmployeeQuantityMonthService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2019-12-17
 */
@RestController
@Slf4j
@RequestMapping("/api/organization/stat-employee-quantity-month")
public class StatEmployeeQuantityMonthController {

    @Autowired
    private IStatEmployeeQuantityMonthService statEmployeeQuantityMonthServiceImpl;

    @GetMapping("/queryEmployeeQuantityStat")
    @RestResult
    public IPage<StatEmployeeQuantityMonth> queryEmployeeQuantityStat(String year,String orgId,Integer page,Integer limit){
        Page<StatEmployeeQuantityMonth> statPage = new Page<>(page, limit);
        return statEmployeeQuantityMonthServiceImpl.queryEmployeeQuantityStat(year,orgId,statPage);
    }

}
