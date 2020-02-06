package com.microtomato.hirun.modules.bss.customer.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustQueryCondDTO;
import com.microtomato.hirun.modules.bss.customer.service.ICustBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 客户信息 前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-02-01
 */
@RestController
@Slf4j
@RequestMapping("/api/customer/cust-base")
public class CustBaseController {

    @Autowired
    private ICustBaseService custBaseServiceImpl;

    @GetMapping("/getCustInfo")
    @RestResult
    public CustInfoDTO getCustInfo(Long custId, Long orderId) {
        return custBaseServiceImpl.queryByCustIdOrOrderId(custId, orderId);
    }

    @GetMapping("/queryCustomerInfo")
    @RestResult
    public List<CustInfoDTO> queryCustomerInfo(CustQueryCondDTO custQueryCond){
        log.debug(custQueryCond.toString());
        return custBaseServiceImpl.queryCustomerInfo(custQueryCond);
    }
}
