package com.microtomato.hirun.modules.bss.salary.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryRoyaltyDetailDTO;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryRoyaltyDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: hirun-helper
 * @description: 工资明细控制器
 * @author: jinnian
 * @create: 2020-06-13 01:18
 **/
@RestController
@RequestMapping("/api/bss.salary/salary-royalty-detail")
public class SalaryRoyaltyDetailController {

    @Autowired
    private ISalaryRoyaltyDetailService salaryRoyaltyDetailService;

    @GetMapping("/queryRoyaltyByOrderId")
    @RestResult
    public SalaryRoyaltyDetailDTO querySalary(Long orderId) {
        return this.salaryRoyaltyDetailService.queryByOrderId(orderId);
    }
}
