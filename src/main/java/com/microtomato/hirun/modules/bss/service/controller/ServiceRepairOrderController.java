package com.microtomato.hirun.modules.bss.service.controller;


import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.customer.entity.dto.QueryCustCondDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.*;
import com.microtomato.hirun.modules.bss.service.service.IServiceRepairOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * (ServiceRepairOrder)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@RestController
@RequestMapping("/api/bss.service/service-repair-order")
public class ServiceRepairOrderController {

    /**
     * 服务对象
     */
    @Autowired
    private IServiceRepairOrderService serviceRepairOrderService;

    @PostMapping("/saveRepairOrder")
    @RestResult
    public void saveRepairOrder(@RequestBody RepairOrderInfoDTO records) {
        this.serviceRepairOrderService.saveRepairRecord(records);
    }

    @GetMapping("/queryRepairRecordInfo")
    @RestResult
    public RepairOrderRecordDTO queryRepairRecordInfo(Long orderId,Long customerId,String repairNo) {
        return serviceRepairOrderService.queryRepairRecordInfo(orderId,customerId,repairNo);
    }

    @PostMapping("/nextStep")
    @RestResult
    public void nextStep(@RequestBody RepairOrderInfoDTO records) {
        this.serviceRepairOrderService.nextStep(records);
    }

    @GetMapping("/queryRepairAllRecord")
    @RestResult
    public List<RepairOrderDTO> queryRepairAllRecord(QueryRepairCondDTO condDTO) {
        return serviceRepairOrderService.queryRepairAllRecord(condDTO);
    }
}