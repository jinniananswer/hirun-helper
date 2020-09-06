package com.microtomato.hirun.modules.bss.order.controller;



import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderInspectDTO;
import com.microtomato.hirun.modules.bss.order.service.IOrderInspectService;
import com.microtomato.hirun.modules.bss.service.entity.dto.ComplainOrderRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (OrderInspect)表控制层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-08-11 18:07:44
 */
@RestController
@RequestMapping("/api/bss.order/order-inspect")
public class OrderInspectController {

    /**
     * 服务对象
     */
    @Autowired
    private IOrderInspectService orderInspectService;

    @PostMapping("/save")
    @RestResult
    public void save(@RequestBody OrderInspectDTO orderInspectDTO) {
        this.orderInspectService.save(orderInspectDTO);
    }

    @GetMapping("/queryOrderInspect")
    @RestResult
    public OrderInspectDTO queryOrderInspect(Long orderId) {
        return this.orderInspectService.queryOrderInspect(orderId);
    }

    @PostMapping("/nextStep")
    @RestResult
    public void nextStep(@RequestBody OrderInspectDTO orderInspectDTO) {
         this.orderInspectService.nextStep(orderInspectDTO);
    }

    @PostMapping("/submitToNotReceive")
    @RestResult
    public void submitToNotReceive(@RequestBody OrderInspectDTO orderInspectDTO) {
        this.orderInspectService.submitToNotReceive(orderInspectDTO);
    }
}