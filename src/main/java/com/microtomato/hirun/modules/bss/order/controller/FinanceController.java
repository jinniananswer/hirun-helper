package com.microtomato.hirun.modules.bss.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;

import com.microtomato.hirun.modules.bss.order.entity.dto.CollectFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.CollectionComponentDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.PayComponentDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.CustOrderInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.CustOrderQueryDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.FinancePendingTaskDTO;
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

    @GetMapping("/initCollectionComponent")
    @RestResult
    public CollectionComponentDTO initCollectionComponent() {
        return this.domainService.initCollectionComponent();
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

    @GetMapping("/queryFinancePendingTask")
    @RestResult
    public List<FinancePendingTaskDTO> queryFinancePendingTask() {
        return this.domainService.queryFinancePendingTask();
    }
}
