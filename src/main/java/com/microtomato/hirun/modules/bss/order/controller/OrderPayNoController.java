package com.microtomato.hirun.modules.bss.order.controller;


import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.LineChartDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.PayGatherDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.QueryPayGatherDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.QueryPayTrendDTO;
import com.microtomato.hirun.modules.bss.order.service.IOrderPayNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单支付流水表表(OrderPayNo)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-02-29 11:59:42
 */
@RestController
@RequestMapping("/api/bss/order/order-pay-no")
public class OrderPayNoController {

    /**
     * 服务对象
     */
    @Autowired
    private IOrderPayNoService orderPayNoService;

    @PostMapping("/queryPayGather")
    @RestResult
    public List<PayGatherDTO> queryPayGather(@RequestBody QueryPayGatherDTO condition) {
        return this.orderPayNoService.queryPayGather(condition);
    }

    @PostMapping("/queryPayTrend")
    @RestResult
    public LineChartDTO queryPayTrend(@RequestBody QueryPayTrendDTO condition) {
        return this.orderPayNoService.queryPayTrend(condition);
    }

}