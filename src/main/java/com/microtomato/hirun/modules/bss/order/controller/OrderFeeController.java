package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.config.entity.dto.PayComponentDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.LastInstallmentCollectionDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.SecondInstallmentCollectionDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;
import com.microtomato.hirun.modules.bss.order.service.IInstallmentCollectDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderFeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 订单费用表 前端控制器
 * </p>
 *
 * @author sunxin
 * @since 2020-02-05
 */
@RestController
@Slf4j
@RequestMapping("/api/bss/order/order-fee")
public class OrderFeeController {

    @Autowired
    private IOrderFeeService orderFeeServiceImpl;

    @Autowired
    private IInstallmentCollectDomainService collectDomainService;

    /**
     *
     * @param orderId
     * @return
     */
    @GetMapping("/queryOrderConsult")
    @RestResult
    public OrderFee queryOrderCollectFee(Long orderId){
        return orderFeeServiceImpl.queryOrderCollectFee(orderId);
    }

    /**
     * 主营系统审核加载付费数据
     * @param orderId
     */
    @GetMapping("/initCostAudit")
    @RestResult
    public PayComponentDTO initCostAudit(Long orderId) {
        return this.orderFeeServiceImpl.initCostAudit(orderId);
    }

    /**
     * 费用审核
     * @param dto
     */
    @PostMapping("/submitAudit")
    @RestResult
    public void submitAudit(@RequestBody OrderFeeDTO dto) {
        orderFeeServiceImpl.submitAudit(dto);
    }

    /**
     * 费用复核
     * @param orderPayNo
     */
    @PostMapping("/costReview")
    @RestResult
    public void costReview(@RequestBody OrderPayNo orderPayNo) {
        orderFeeServiceImpl.costReview(orderPayNo);
    }

    @PostMapping("/secondInstallmentCollect")
    @RestResult
    public void secondInstallmentCollect(@RequestBody SecondInstallmentCollectionDTO dto) {
        orderFeeServiceImpl.secondInstallmentCollect(dto);
    }

    @PostMapping("/saveLastInstallmentCollect")
    @RestResult
    public void saveLastInstallmentCollect(@RequestBody LastInstallmentCollectionDTO lastInstallmentCollectionDTO) {
        collectDomainService.saveLastCollectionFee(lastInstallmentCollectionDTO);
    }

    @GetMapping("/queryLastInstallmentCollect")
    @RestResult
    public LastInstallmentCollectionDTO queryLastInstallmentCollect(Long orderId) {
        return collectDomainService.queryLastInstallmentCollect(orderId);
    }

}
