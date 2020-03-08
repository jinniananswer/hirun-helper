package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.config.service.IPayItemCfgService;
import com.microtomato.hirun.modules.bss.order.entity.dto.SecondInstallmentCollectionDTO;
import com.microtomato.hirun.modules.bss.order.service.*;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-second-installment")
public class OrderSecondInstallmentController {

    @Autowired
    private IOrderSecondInstallmentService orderSecondInstallmentServiceImpl;

    @PostMapping("/secondInstallmentCollect")
    @RestResult
    public void secondInstallmentCollect(@RequestBody SecondInstallmentCollectionDTO dto) {
        orderSecondInstallmentServiceImpl.secondInstallmentCollect(dto);
    }

    @GetMapping("/getSecondInstallment")
    @RestResult
    public SecondInstallmentCollectionDTO getSecondInstallment(Long orderId) {
        return orderSecondInstallmentServiceImpl.getSecondInstallment(orderId);
    }
}
