package com.microtomato.hirun.modules.bss.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;

import com.microtomato.hirun.modules.bss.config.entity.po.CollectionItemCfg;
import com.microtomato.hirun.modules.bss.order.entity.dto.*;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.CustPayDataDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.FinanceOrderTaskDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.FinanceOrderTaskQueryDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.NormalPayNoDTO;
import com.microtomato.hirun.modules.bss.order.service.IFinanceDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: hirun-helper
 * @description: 财务相关业务控制器
 * @author: jinnian
 * @create: 2020-03-01 00:56
 **/
@RestController
@Slf4j
@RequestMapping("/api/bss.order/finance")
public class FinanceController {

    @Autowired
    private IFinanceDomainService domainService;

    @GetMapping("/queryCustOrderInfo")
    @RestResult
    public IPage<CustOrderInfoDTO> queryCustOrderInfo(CustOrderQueryDTO queryCond) {
        Page<CustOrderQueryDTO> page = new Page<>(queryCond.getPage(), queryCond.getLimit());
        return this.domainService.queryCustOrderInfos(queryCond, page);
    }

    @PostMapping("/queryPayInfoByCond")
    @RestResult
    public IPage<NormalPayNoDTO> queryPayInfoByCond(@RequestBody  NonCollectFeeQueryDTO queryCondition) {
        return this.domainService.queryPayInfoByCond(queryCondition);
    }

    @GetMapping("/initCollectionComponent")
    @RestResult
    public CollectionComponentDTO initCollectionComponent( Long payNo) {
        return this.domainService.initCollectionComponent(payNo);
    }

    @GetMapping("/initCollectionItems")
    @RestResult
    public List<CascadeDTO<CollectionItemCfg>> initCollectionItems() {
        return this.domainService.initCollectionItem();
    }

    @GetMapping("/initPayComponent")
    @RestResult
    public PayComponentDTO initPayComponent(Long orderId, Long payNo) {
        return this.domainService.initPayComponent(orderId, payNo);
    }

    @PostMapping("/collectFee")
    @RestResult
    public Map collectFee(@RequestBody  CollectFeeDTO collectFee) {
        this.domainService.collectFee(collectFee);
        return new HashMap();
    }

    @PostMapping("/nonCollectFeeUpdate")
    @RestResult
    public Map nonCollectFeeUpdate(@RequestBody NonCollectFeeDTO nonCollectFee) {
        this.domainService.nonCollectFeeUpdate(nonCollectFee);
        return new HashMap();
    }

    @PostMapping("/nonCollectFeeForAudit")
    @RestResult
    public Map nonCollectFeeForAudit(@RequestBody NonCollectFeeDTO nonCollectFee) {
        this.domainService.nonCollectFeeForAudit(nonCollectFee);
        return new HashMap();
    }

    @PostMapping("/nonCollectFee")
    @RestResult
    public Map nonCollectFee(@RequestBody NonCollectFeeDTO nonCollectFee) {
        this.domainService.nonCollectFee(nonCollectFee);
        return new HashMap();
    }

    @GetMapping("/queryFinancePendingTask")
    @RestResult
    public List<FinancePendingTaskDTO> queryFinancePendingTask() {
        return this.domainService.queryFinancePendingTask();
    }

    @GetMapping("/queryFinanceOrderTasks")
    @RestResult
    public IPage<FinanceOrderTaskDTO> queryFinanceOrderTasks(FinanceOrderTaskQueryDTO condition) {
        return this.domainService.queryFinanceOrderTasks(condition);
    }

    @GetMapping("/getCustPayData")
    @RestResult
    public CustPayDataDTO getCustPayData(Long orderId, Long payNo) {
        return this.domainService.getCustPayData(orderId, payNo);
    }

    @GetMapping("/submitNonBusinessReceipt")
    @RestResult
    public void submitNonBusinessReceipt(Long payNo, Long financeEmployeeId) {
        this.domainService.submitNonBusinessReceipt(payNo, financeEmployeeId);
    }

    @GetMapping("/submitBusinessReceipt")
    @RestResult
    public void submitBusinessReceipt(Long orderId, Long payNo, Long financeEmployeeId) {
        this.domainService.submitBusinessReceipt(orderId, payNo, financeEmployeeId);
    }
}
