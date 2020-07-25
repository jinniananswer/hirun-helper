package com.microtomato.hirun.modules.finance.controller;



import com.microtomato.hirun.modules.finance.service.IFinanceVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 财务领款单表(FinanceVoucher)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-25 21:25:21
 */
@RestController
@RequestMapping("/api/finance/finance-voucher")
public class FinanceVoucherController {

    /**
     * 服务对象
     */
    @Autowired
    private IFinanceVoucherService financeVoucherService;

}