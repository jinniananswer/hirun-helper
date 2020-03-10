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
import com.microtomato.hirun.modules.bss.order.service.IFeeDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderBaseService;
import com.microtomato.hirun.modules.bss.order.service.IOrderFileService;
import com.microtomato.hirun.modules.bss.order.service.IOrderPlaneSketchService;
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

    @PostMapping("/submitPlaneSketch")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submit(@RequestBody OrderPlaneSketch orderPlaneSketch) {
        log.debug("OrderPlaneSketchController"+orderPlaneSketch.toString());
        orderPlaneSketchServiceImpl.save(orderPlaneSketch);
    }

    @PostMapping("/submitToConfirmFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToConfirmFlow(@RequestBody OrderPlaneSketch orderPlaneSketch) {
        log.debug("orderPlaneSketch"+orderPlaneSketch.getOrderId());
        orderPlaneSketchServiceImpl.submitToConfirmFlow(orderPlaneSketch.getOrderId());
    }

    @PostMapping("/submitToSignContractFlow")
    @Transactional(rollbackFor = Exception.class)
    @RestResult
    public void submitToSignContractFlow(@RequestBody OrderPlaneSketch orderPlaneSketch) {
        /**
        * 判斷文件是否上傳
        * */
        OrderFile orderFile = orderFileService.getOrderFile(orderPlaneSketch.getOrderId(), 456);
        if (orderFile == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.FILE_NOT_FOUND);
        }
        /**
         * 回写套内面积
         * */
        OrderBase order = this.orderBaseService.queryByOrderId(orderPlaneSketch.getOrderId());
        if (order == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.ORDER_FEE_NOT_FOUND);
        }
        order.setIndoorArea(orderPlaneSketch.getIndoorArea());
        this.orderBaseService.updateOrderBase(order);
         /**
         * 回写费用
         * */
        List<FeeDTO> FeeDTOs = new ArrayList();
        FeeDTO fee  = new FeeDTO();
        fee.setFeeItemId(1L);
        fee.setMoney(Double.valueOf(orderPlaneSketch.getContractDesignFee()));
        FeeDTOs.add(fee);
        ifeeDomainService.createOrderFee(orderPlaneSketch.getOrderId(),"1",null,FeeDTOs);
        /**
         * 状态扭转
         * */
        orderPlaneSketchServiceImpl.submitToSignContractFlow(orderPlaneSketch.getOrderId());
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
