package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerSalaryDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerSalaryService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-03-01
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-worker-salary")
public class OrderWorkerSalaryController {

    @Autowired
    private IOrderWorkerSalaryService orderWorkerSalaryServiceImpl;

    @GetMapping("/queryWorkerSalary")
    @RestResult
    public OrderWorkerSalaryDTO queryWorkerSalary(Long orderId){
        List<OrderWorkerSalaryDTO> list=orderWorkerSalaryServiceImpl.queryOrderWorkerSalary(1,orderId);
        if(ArrayUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    @PostMapping("/saveWorkerSalary")
    @RestResult
    public void saveWorkerSalary(@RequestBody OrderWorkerSalaryDTO dto){
        orderWorkerSalaryServiceImpl.updateWorkerSalary(1,dto);
    }

    @PostMapping("/closeWorkerSalary")
    @RestResult
    public void closeWorkerSalary(@RequestBody OrderWorkerSalaryDTO dto){
        orderWorkerSalaryServiceImpl.closeWorkerSalary(1,dto);
    }

    @GetMapping("/queryAllWorkerSalary")
    @RestResult
    public Map<String, OrderWorkerSalaryDTO> queryAllWorkerSalary(Long orderId){
       return orderWorkerSalaryServiceImpl.queryAllWorkerSalary(orderId);
    }
}
