package com.microtomato.hirun.modules.finance.controller;



import com.microtomato.hirun.modules.finance.service.IFinanceVoucherItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 财务领款单明细表(FinanceVoucherItem)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-25 21:25:21
 */
@RestController
@RequestMapping("/api/finance/finance-voucher-item")
public class FinanceVoucherItemController {

    /**
     * 服务对象
     */
    @Autowired
    private IFinanceVoucherItemService financeVoucherItemService;


}