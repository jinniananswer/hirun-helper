package com.microtomato.hirun.modules.bss.config.controller;



import com.microtomato.hirun.modules.bss.config.service.IFinanceItemService;
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
@RequestMapping("/api/bss.config/finance-item")
public class FinanceItemController {

    /**
     * 服务对象
     */
    @Autowired
    private IFinanceItemService financeItemService;
}