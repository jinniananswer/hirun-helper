package com.microtomato.hirun.modules.bss.service.controller;



import com.microtomato.hirun.modules.bss.service.service.IServiceRepairOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (ServiceRepairOrder)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@RestController
@RequestMapping("/api/ServiceRepairOrder")
public class ServiceRepairOrderController {

    /**
     * 服务对象
     */
    @Autowired
    private IServiceRepairOrderService serviceRepairOrderService;

}