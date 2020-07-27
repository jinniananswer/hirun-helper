package com.microtomato.hirun.modules.bss.service.controller;



import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.service.entity.dto.ComplainOrderInfoDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.ComplainOrderRecordDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.RepairOrderInfoDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.RepairOrderRecordDTO;
import com.microtomato.hirun.modules.bss.service.service.IServiceComplainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (ServiceComplain)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@RestController
@RequestMapping("/api/bss.service/service-complain")
public class ServiceComplainController {

    /**
     * 服务对象
     */
    @Autowired
    private IServiceComplainService serviceComplainService;

    @PostMapping("/saveComplainOrder")
    @RestResult
    public void saveComplainOrder(@RequestBody ComplainOrderInfoDTO records) {
        this.serviceComplainService.saveComplainOrder(records);
    }

    @GetMapping("/queryComplainRecordInfo")
    @RestResult
    public ComplainOrderRecordDTO queryComplainRecordInfo(Long orderId, Long customerId, String complainNo) {
        return serviceComplainService.queryComplainRecordInfo(orderId,customerId,complainNo);
    }

}