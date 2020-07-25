package com.microtomato.hirun.modules.finance.controller;



import com.microtomato.hirun.modules.finance.service.IFinanceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 财务科目表(FinanceItem)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-19 22:26:54
 */
@RestController
@RequestMapping("/api/finance/finance-item")
public class FinanceItemController {

    /**
     * 服务对象
     */
    @Autowired
    private IFinanceItemService financeItemService;
}