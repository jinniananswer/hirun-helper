package com.microtomato.hirun.modules.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.NonCollectFeeQueryDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.FinanceOrderTaskDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.FinanceOrderTaskQueryDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.NormalPayNoDTO;
import com.microtomato.hirun.modules.finance.entity.dto.ReceiveReceiptDTO;
import com.microtomato.hirun.modules.finance.service.IFinanceFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: hirun-helper
 * @description:
 * @author: jinnian
 * @create: 2020-11-02 00:55
 **/
@RestController
@RequestMapping("/api/finance/finance-field")
public class FinanceFieldController {

    @Autowired
    private IFinanceFieldService financeFieldService;

    @GetMapping("/queryBusinessReceipt")
    @RestResult
    public IPage<FinanceOrderTaskDTO> queryBusinessReceipt(FinanceOrderTaskQueryDTO condition) {
        return this.financeFieldService.queryBusinessReceipt(condition);
    }

    @PostMapping("/queryNonBusinessReceipt")
    @RestResult
    public IPage<NormalPayNoDTO> queryNonBusinessReceipt(@RequestBody NonCollectFeeQueryDTO queryCondition) {
        return this.financeFieldService.queryNonBusinessReceipt(queryCondition);
    }

    @PostMapping("/submitBusinessReceiveReceipt")
    @RestResult
    public void submitBusinessReceiveReceipt(@RequestBody ReceiveReceiptDTO data) {
        this.financeFieldService.submitBusinessReceiveReceipt(data);
    }

    @PostMapping("/submitNonBusinessReceiveReceipt")
    @RestResult
    public void submitNonBusinessReceiveReceipt(@RequestBody ReceiveReceiptDTO data) {
        this.financeFieldService.submitNonBusinessReceiveReceipt(data);
    }
}
