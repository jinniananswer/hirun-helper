package com.microtomato.hirun.modules.bss.customer.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustPreparationDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.customer.service.ICustPreparationService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-02-01
 */
@RestController
@Slf4j
@RequestMapping("/api/customer/cust-preparation")
public class CustPreparationController {

    @Autowired
    private ICustPreparationService custPreparationServiceImpl;

    @PostMapping("/addCustomerPreparation")
    @RestResult
    public void addCustomerPreparation(CustPreparationDTO custPreparation) {
        log.debug(custPreparation.toString());
        custPreparationServiceImpl.addCustomerPreparation(custPreparation);
    }

    @GetMapping("/loadPreparationHistory")
    @RestResult
    public List<CustPreparationDTO> loadPreparationHistory(String mobileNo){
        log.debug(mobileNo);
        return custPreparationServiceImpl.loadPreparationHistory(mobileNo);
    }

    @PostMapping("/customerRuling")
    @RestResult
    public void customerRuling(CustPreparationDTO custPreparation) {
        log.debug(custPreparation.toString());
        custPreparationServiceImpl.customerRuling(custPreparation);
    }

    @GetMapping("/queryFailPreparation")
    @RestResult
    public List<CustPreparationDTO> queryFailPreparation(String mobileNo,Long custId,Long houseId){
        log.debug(mobileNo);
        return custPreparationServiceImpl.queryCustPreparaton(mobileNo,custId,"3",houseId,"1");
    }

    @GetMapping("/getCustomerNoAndSec")
    @RestResult
    public Map<String,String> getCustomerNoAndSec(){
        return custPreparationServiceImpl.getCustomerNoAndSec();
    }
}
