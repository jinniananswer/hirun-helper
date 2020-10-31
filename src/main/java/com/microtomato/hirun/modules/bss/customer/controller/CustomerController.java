package com.microtomato.hirun.modules.bss.customer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bi.middleproduct.entity.po.MidprodOpen;
import com.microtomato.hirun.modules.bss.customer.entity.dto.*;
import com.microtomato.hirun.modules.bss.customer.entity.po.Party;
import com.microtomato.hirun.modules.bss.customer.service.ICustomerDomainService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.customer.service.ICustomerService;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 客户信息 前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-04-20
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.customer/customer")
public class CustomerController {

    @Autowired
    private ICustomerDomainService customerDomainService;

    @GetMapping("/getCustomerDetailInfo")
    @RestResult
    public CustomerInfoDetailDTO getCustomerDetailInfo(Long customerId, String openId, Long partyId) {
        return customerDomainService.queryCustomerInfoDetail(customerId,openId,partyId);
    }


    @GetMapping("/getActionInfo")
    @RestResult
    public List<CustomerActionInfoDTO> getActionInfo(Long customerId, String openId, Long partyId) {
        return customerDomainService.getActionInfo(customerId,openId,partyId);
    }

    @GetMapping("/getXQLTYInfo")
    @RestResult
    public List<XQLTYInfoDTO> getXQLTYInfo(String openId) {
        return customerDomainService.getXQLTYInfo(openId);
    }

    @GetMapping("/getXQLTEInfo")
    @RestResult
    public XQLTEInfoDTO getXQLTEInfo(String openId) {
        return customerDomainService.getXQLTEInfo(openId);
    }

    @GetMapping("/getMidProdInfo")
    @RestResult
    public List<MidprodOpen> getMidProdInfo(String openId) {
        return customerDomainService.getMidPordInfo(openId);
    }

    @GetMapping("/queryCustomerInfo")
    @RestResult
    public IPage<CustomerInfoDetailDTO> queryCustomerInfo(QueryCustCondDTO custQueryCond){
        log.debug(custQueryCond.toString());
        return customerDomainService.queryCustomerInfo(custQueryCond);
    }

    @GetMapping("/getLtzdsInfo")
    @RestResult
    public List<LTZDSInfoDTO> getLtzdsInfo(String openId) {
        return customerDomainService.getLtzdsInfo(openId);
    }

}
