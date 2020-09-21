package com.microtomato.hirun.modules.bss.customer.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustPreparationDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustVisitInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.PartyVisit;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.customer.service.IPartyVisitService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-02-04
 */
@RestController
@Slf4j
@RequestMapping("/api/customer/party-visit")
public class PartyVisitController {

    @Autowired
    private IPartyVisitService partyVisitServiceImpl;

    @GetMapping("/queryCustVisit")
    @RestResult
    public List<CustVisitInfoDTO> queryCustVisit(Long custId){
        return partyVisitServiceImpl.queryCustVisit(custId);
    }

    @PostMapping("/addCustomerVisit")
    @RestResult
    public void addCustomerVisit(@RequestBody PartyVisit customerVisit) {
        log.debug(customerVisit.toString());
        partyVisitServiceImpl.addCustomerVisit(customerVisit);
    }

}
