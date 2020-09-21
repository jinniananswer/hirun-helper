package com.microtomato.hirun.modules.finance.controller;



import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.CascadeDTO;
import com.microtomato.hirun.modules.finance.entity.po.FinanceItem;
import com.microtomato.hirun.modules.finance.service.IFinanceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
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

    @GetMapping("/loadFinancenItem")
    @RestResult
    public List<CascadeDTO<FinanceItem>>  loadFinancenItem() {
        return this.financeItemService.loadFinancenItem();
    }
}