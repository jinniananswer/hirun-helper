package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.DecorateContractDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.order.service.IOrderContractService;

/**
 * <p>
 * 订单合同 前端控制器
 * </p>
 *
 * @author anwx
 * @since 2020-02-23
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-contract")
public class OrderContractController {

    @Autowired
    private IOrderContractService orderContractServiceImpl;

    @GetMapping("/getDecorateContractInfo")
    @RestResult
    public DecorateContractDTO getDecorateContractInfo(Long orderId) {
        return orderContractServiceImpl.getDecorateContractInfo(orderId);
    }

    @PostMapping("/submitDecorateContract")
    @RestResult
    @Transactional(rollbackFor = Exception.class)
    public void submitDecorateContract(DecorateContractDTO decorateContractDTO) {
        orderContractServiceImpl.submitDecorateContract(decorateContractDTO);
        //保存优惠项
    }
}
