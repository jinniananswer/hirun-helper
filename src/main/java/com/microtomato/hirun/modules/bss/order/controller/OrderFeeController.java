package com.microtomato.hirun.modules.bss.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.LastInstallmentCollectionDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.PayComponentDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.*;
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
    public PayComponentDTO initCostAudit(Long orderId, String orderStatus) {
        return this.orderFeeServiceImpl.initCostAudit(orderId,orderStatus);
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

    @PostMapping("/applyFinanceAuditLast")
    @RestResult
    public void applyFinanceAuditLast(@RequestBody LastInstallmentCollectionDTO lastInstallmentCollectionDTO) {
        collectDomainService.applyFinanceAuditLast(lastInstallmentCollectionDTO);
    }

    @GetMapping("/getByOrderIdTypePeriod")
    @RestResult
    public OrderFee getByOrderIdTypePeriod(Long orderId, String type, Integer period) {
        return orderFeeServiceImpl.getByOrderIdTypePeriod(orderId, type, period);
    }

    @PostMapping("/queryDesignFees")
    @RestResult
    public IPage<DesignFeeDTO> queryDesignFees(@RequestBody QueryDesignFeeDTO designFeeDTO) {
        return this.orderFeeServiceImpl.queryDesignFees(designFeeDTO);
    }

    @GetMapping("/queryProjectFees")
    @RestResult
    public IPage<ProjectFeeDTO> queryProjectFees(QueryProjectFeeDTO projectFeeDTO) {
        return this.orderFeeServiceImpl.queryProjectFees(projectFeeDTO);
    }

    @GetMapping("/queryNoBalanceFees")
    @RestResult
    public IPage<NoBalanceFeeDTO> queryNoBalanceFees(QueryNoBalanceFeeDTO condtion) {
        return this.orderFeeServiceImpl.queryNoBalanceFees(condtion);
    }
}
