package com.microtomato.hirun.modules.bss.service.controller;


import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.service.entity.dto.RepairOrderInfoDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.RepairOrderRecordDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.ServicePendingTaskDTO;
import com.microtomato.hirun.modules.bss.service.service.IServiceCenterDomainService;
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
@RequestMapping("/api/bss.service/service-center-domain")
public class ServiceCenterDomainController {

    /**
     * 服务对象
     */
    @Autowired
    private IServiceCenterDomainService domainService;


    @GetMapping("/queryServicePendingTask")
    @RestResult
    public List<ServicePendingTaskDTO> queryServicePendingTask() {
        return domainService.queryServicePendingTask();
    }
}