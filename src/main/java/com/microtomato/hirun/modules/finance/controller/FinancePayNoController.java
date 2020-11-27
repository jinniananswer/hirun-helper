package com.microtomato.hirun.modules.finance.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.finance.entity.dto.FinancePayNoDTO;
import com.microtomato.hirun.modules.finance.service.IFinancePayNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: hirun-helper
 * @description: 出纳付款控制器
 * @author: jinnian
 * @create: 2020-11-24 16:47
 **/
@RestController
@RequestMapping("/api/finance/finance-pay-no")
public class FinancePayNoController {

    @Autowired
    private IFinancePayNoService financePayNoService;

    @PostMapping("/createFinancePay")
    @RestResult
    public void createFinancePay(@RequestBody FinancePayNoDTO pay) {
        this.financePayNoService.createPayNo(pay);
    }

    @GetMapping("/getFinancePay")
    @RestResult
    public FinancePayNoDTO getFinancePay(String voucherNo) {
        return this.financePayNoService.getFinancePay(voucherNo);
    }
}
