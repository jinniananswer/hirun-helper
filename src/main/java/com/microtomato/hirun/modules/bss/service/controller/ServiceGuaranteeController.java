package com.microtomato.hirun.modules.bss.service.controller;



import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.salary.entity.dto.DesignRoyaltyDetailDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryRoyaltyDetailDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.GuaranteeDTO;
import com.microtomato.hirun.modules.bss.service.service.IServiceGuaranteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (ServiceGuarantee)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@RestController
@RequestMapping("/api/bss.service/ServiceGuarantee")
public class ServiceGuaranteeController {

    /**
     * 服务对象
     */
    @Autowired
    private IServiceGuaranteeService serviceGuaranteeService;

    @GetMapping("/queryCustomerGuaranteeInfo")
    @RestResult
    public GuaranteeDTO queryCustomerGuaranteeInfo(Long orderId) {
        return serviceGuaranteeService.queryCustomerGuaranteeInfo(orderId);
    }

    @PostMapping("/saveGuaranteeInfo")
    @RestResult
    public void saveGuaranteeInfo(@RequestBody GuaranteeDTO dto) {
        this.serviceGuaranteeService.saveGuaranteeInfo(dto);
    }
}