package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.microtomato.hirun.modules.bss.order.service.IOrderFeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 费用审核
     * @param dto
     */
    @PostMapping("/submitAudit")
    @RestResult
    public void submitAudit(OrderFeeDTO dto) {
        orderFeeServiceImpl.submitAudit(dto);
    }

    /**po
     * 费用复核
     * @param orderPayNo
     */
    @PostMapping("/costReview")
    @RestResult
    public void costReview(OrderPayNo orderPayNo) {
        System.out.println("orderPayNo========="+orderPayNo);
        orderFeeServiceImpl.costReview(orderPayNo);
    }


    @PostMapping("/secondInstallmentCollect")
    @RestResult
    public void secondInstallmentCollect(SecondInstallmentCollectionDTO dto) {
        orderFeeServiceImpl.secondInstallmentCollect(dto);
    }

}
