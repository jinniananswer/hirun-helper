package com.microtomato.hirun.modules.bss.order.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.FeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderPlaneSketchDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFile;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderMeasureHouse;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPlaneSketch;
import com.microtomato.hirun.modules.bss.order.exception.OrderException;
import com.microtomato.hirun.modules.bss.order.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：mmzs
 * @date ：Created in 2020/2/6 20:13
 * @description：订单平面图控制器
 * @modified By：
 * @version: 1$
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.order/order-planSketch")
public class OrderPlaneSketchController {

    @Autowired
    private IOrderFileService orderFileService;

    @Autowired
    private IFeeDomainService ifeeDomainService;

    @Autowired
    private IOrderBaseService orderBaseService;

    @Autowired
    private IOrderPlaneSketchService orderPlaneSketchServiceImpl;

    @Autowired
    private IOrderWorkerService orderWorkerService;

    @PostMapping("/submitPlaneSketch")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submit(@RequestBody OrderPlaneSketchDTO dto) {
        log.debug("OrderPlaneSketchController"+dto.toString());
        OrderPlaneSketch orderPlaneSketch=new OrderPlaneSketch();
        BeanUtils.copyProperties(dto,orderPlaneSketch);

        if(orderPlaneSketch.getId()==null){
            orderPlaneSketchServiceImpl.save(orderPlaneSketch);
        }else{
            orderPlaneSketchServiceImpl.updateById(orderPlaneSketch);
        }
        this.updateIndorrArea(orderPlaneSketch.getOrderId(),orderPlaneSketch.getIndoorArea());

        this.orderWorkerService.updateOrderWorker(dto.getOrderId(),34L,dto.getFinanceEmployeeId());
        this.orderWorkerService.updateOrderWorker(dto.getOrderId(),30L,dto.getDesigner());

    }

    @PostMapping("/submitToConfirmFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToConfirmFlow(@RequestBody OrderPlaneSketch orderPlaneSketch) {
        log.debug("orderPlaneSketch"+orderPlaneSketch.getOrderId());
        orderPlaneSketchServiceImpl.submitToConfirmFlow(orderPlaneSketch.getOrderId());

        OrderBase order = this.orderBaseService.queryByOrderId(orderPlaneSketch.getOrderId());
        if (order == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.ORDER_FEE_NOT_FOUND);
        }
        order.setIndoorArea(orderPlaneSketch.getIndoorArea());
        this.orderBaseService.updateById(order);
    }

    private void updateIndorrArea(Long orderId,String indoorArea){
        OrderBase order = this.orderBaseService.queryByOrderId(orderId);
        if (order == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.ORDER_FEE_NOT_FOUND);
        }
        order.setIndoorArea(indoorArea);
        this.orderBaseService.updateById(order);
    }

    @PostMapping("/submitToSignContractFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToSignContractFlow(@RequestBody OrderPlaneSketchDTO dto) {
        this.submit(dto);
        /**
        * 判斷文件是否上傳
        * */
        OrderFile orderFile = orderFileService.getOrderFile(dto.getOrderId(), 456);
        if (orderFile == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.FILE_NOT_FOUND);
        }
        /**
         * 回写套内面积
         * */
        OrderBase order = this.orderBaseService.queryByOrderId(dto.getOrderId());
        if (order == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.ORDER_FEE_NOT_FOUND);
        }
        order.setIndoorArea(dto.getIndoorArea());
        this.orderBaseService.updateById(order);
         /**
         * 回写费用
         * */
        List<FeeDTO> FeeDTOs = new ArrayList();
        FeeDTO fee  = new FeeDTO();
        fee.setFeeItemId(1L);
        fee.setMoney(Double.valueOf(dto.getContractDesignFee()));
        FeeDTOs.add(fee);
        ifeeDomainService.createOrderFee(dto.getOrderId(),"1",null,FeeDTOs);
        /**
         * 状态扭转
         * */
        orderPlaneSketchServiceImpl.submitToSignContractFlow(dto.getOrderId());
    }

    @GetMapping("/updateOrderWork")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void updateOrderWork(Long orderId,Long roleId,Long employeeId) {
        log.debug("orderId_employeeId"+employeeId+"|"+orderId);
        orderPlaneSketchServiceImpl.updateOrderWork(orderId,roleId,employeeId);
    }

    @GetMapping("/getPlaneSketch")
    @RestResult
    public OrderPlaneSketchDTO getPlaneSketch(Long orderId) {
        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId = userContext.getEmployeeId();
        OrderPlaneSketch orderPlaneSketch =  orderPlaneSketchServiceImpl.getPlaneSketch(orderId);
        OrderPlaneSketchDTO orderPlaneSketchDTO = new OrderPlaneSketchDTO();
        if (orderPlaneSketch != null) {
            BeanUtils.copyProperties(orderPlaneSketch,orderPlaneSketchDTO);
        }
        orderPlaneSketchDTO.setDesigner(employeeId);
        return orderPlaneSketchDTO;
    }

    @PostMapping("/submitToDelayTimeFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToDelayTimeFlow(@RequestBody OrderPlaneSketch orderPlaneSketch) {
        log.debug("orderPlaneSketch"+orderPlaneSketch.getOrderId());
        orderPlaneSketchServiceImpl.submitToDelayTimeFlow(orderPlaneSketch.getOrderId());
    }

    @PostMapping("/submitToBackToDesignerFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToBackToDesignerFlow(@RequestBody OrderPlaneSketch orderPlaneSketch) {
        orderPlaneSketchServiceImpl.submitToBackToDesignerFlow(orderPlaneSketch.getOrderId());
    }
}
