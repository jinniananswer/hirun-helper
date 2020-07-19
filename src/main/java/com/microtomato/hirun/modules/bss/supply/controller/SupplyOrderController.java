package com.microtomato.hirun.modules.bss.supply.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.ProjectFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.QueryProjectFeeDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.QuerySupplyOrderDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyMaterialDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyOrderDTO;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应订单表(SupplyOrder)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-15 11:26:08
 */
@RestController
@RequestMapping("/api/bss.supply/supply-order")
public class SupplyOrderController {

    /**
     * 服务对象
     */
    @Autowired
    private ISupplyOrderService supplyOrderService;

    /**
     * 下单新增
     */
    @PostMapping("/materialOrderDeal")
    @RestResult
    public Map materialOrderDeal(@RequestBody SupplyOrderDTO supplyOrderInfo) {
       this.supplyOrderService.materialOrderDeal(supplyOrderInfo);
        return new HashMap();
    }


    @GetMapping("/querySupplyInfo")
    @RestResult
    public IPage<SupplyOrderDTO> querySupplyInfo(QuerySupplyOrderDTO querySupplyDTO) {
        UserContext userContext = WebContextUtils.getUserContext();
        if(querySupplyDTO.getEmployeeId()==null){
            querySupplyDTO.setEmployeeId(userContext.getEmployeeId());
        }
        return this.supplyOrderService.querySupplyInfo(querySupplyDTO);
    }

}