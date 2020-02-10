package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderRoleCfg;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusCfg;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusTransCfg;
import com.microtomato.hirun.modules.bss.config.service.IOrderRoleCfgService;
import com.microtomato.hirun.modules.bss.config.service.IOrderStatusCfgService;
import com.microtomato.hirun.modules.bss.config.service.IOrderStatusTransCfgService;
import com.microtomato.hirun.modules.bss.order.entity.consts.OrderConst;
import com.microtomato.hirun.modules.bss.order.entity.dto.NewOrderDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.exception.OrderException;
import com.microtomato.hirun.modules.bss.order.service.IOrderBaseService;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import com.microtomato.hirun.modules.bss.order.service.IOrderOperLogService;
import com.microtomato.hirun.modules.bss.order.service.IOrderWorkerService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 订单领域服务实现类
 * @author: jinnian
 * @create: 2020-02-03 01:34
 **/
@Slf4j
@Service
public class OrderDomainServiceImpl implements IOrderDomainService {

    @Autowired
    private IOrderBaseService orderBaseService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IOrderWorkerService orderWorkerService;

    @Autowired
    private IOrderRoleCfgService orderRoleCfgService;

    @Autowired
    private IOrderStatusCfgService orderStatusCfgService;

    @Autowired
    private IOrderStatusTransCfgService orderStatusTransCfgService;

    @Autowired
    private IOrderOperLogService orderOperLogService;

    /**
     * 查询订单综合信息
     * @param orderId
     * @return
     */
    @Override
    public OrderInfoDTO getOrderInfo(Long orderId) {
        OrderInfoDTO orderInfo = new OrderInfoDTO();
        OrderBase orderBase = this.orderBaseService.queryByOrderId(orderId);

        BeanUtils.copyProperties(orderBase, orderInfo);

        if (StringUtils.isNotBlank(orderInfo.getStatus())) {
            orderInfo.setStatusName(this.staticDataService.getCodeName("ORDER_STATUS", orderInfo.getStatus()));
        }

        if (StringUtils.isNotBlank(orderInfo.getHouseLayout())) {
            orderInfo.setHouseLayoutName(this.staticDataService.getCodeName("HOUSE_LAYOUT", orderInfo.getHouseLayout()));
        }
        return orderInfo;
    }

    /**
     * 查询订单工作人员信息
     * @param orderId
     * @return
     */
    @Override
    public List<OrderWorkerDTO> queryOrderWorkers(Long orderId) {
        List<OrderWorkerDTO> orderWorkers = new ArrayList<>();

        List<OrderRoleCfg> configs = this.orderRoleCfgService.queryAllValid();
        if (ArrayUtils.isNotEmpty(configs)) {
            for (OrderRoleCfg config : configs) {
                OrderWorkerDTO orderWorker = new OrderWorkerDTO();
                orderWorker.setRoleId(config.getRoleId());
                orderWorker.setRoleName(config.getRoleName());
                orderWorkers.add(orderWorker);
            }
        }

        List<OrderWorkerDTO> existsWorkers = this.orderWorkerService.queryByOrderId(orderId);
        if (ArrayUtils.isNotEmpty(existsWorkers)) {
            for (OrderWorkerDTO worker : orderWorkers) {
                worker.setStatus("wait");
                worker.setName("暂无");
                for (OrderWorkerDTO existsWorker : existsWorkers) {
                    if (worker.getRoleId().equals(existsWorker.getRoleId())) {
                        worker.setName(existsWorker.getName());
                        //匹配上了，表示状态有效，前端则点亮
                        worker.setStatus("finish");
                    }
                }
            }
        }
        return orderWorkers;
    }

    /**
     * 创建新订单
     * @param newOrder
     */
    @Override
    public void createNewOrder(NewOrderDTO newOrder) {
        if (newOrder == null) {
            return;
        }
        OrderBase order = new OrderBase();
        BeanUtils.copyProperties(newOrder, order);
        if (StringUtils.isNotBlank(newOrder.getStatus())) {
            order.setStatus(OrderConst.ORDER_STATUS_ASKING);
        }

        OrderStatusCfg orderStatusCfg = this.orderStatusCfgService.getCfgByStatus(order.getStatus());

        if (orderStatusCfg != null) {
            order.setStage(orderStatusCfg.getOrderStage());
        }

        this.orderBaseService.save(order);
        this.orderOperLogService.createOrderOperLog(order.getOrderId(), OrderConst.LOG_TYPE_CREATE, order.getStage(), order.getStatus(), OrderConst.OPER_LOG_CONTENT_CREATE);
    }

    /**
     * 订单状态切换
     * @param orderId
     */
    @Override
    public void orderStatusTrans(Long orderId, String oper) {
        OrderBase order = this.orderBaseService.queryByOrderId(orderId);
        this.orderStatusTrans(order, oper);
    }

    @Override
    public void orderStatusTrans(OrderBase order, String oper) {
        Integer stage = order.getStage();
        String status = order.getStatus();
        OrderStatusCfg statusCfg = this.orderStatusCfgService.getCfgByStatus(status);
        OrderStatusTransCfg statusTransCfg = this.orderStatusTransCfgService.getByStatusIdOper(statusCfg.getId(), oper);

        if (statusTransCfg == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.STATUS_TRANS_CFG_NOT_FOUND);
        }

        Long newStatusId = statusTransCfg.getNextOrderStatusId();
        OrderStatusCfg newStatusCfg = this.orderStatusCfgService.getById(newStatusId);

        if (newStatusCfg == null) {
            throw new OrderException(OrderException.OrderExceptionEnum.STATUS_CFG_NOT_FOUND);
        }

        order.setPreviousStage(stage);
        order.setPreviousStatus(status);

        order.setStage(newStatusCfg.getOrderStage());
        order.setStatus(newStatusCfg.getOrderStatus());

        //保存订单信息
        this.orderBaseService.updateById(order);

        String stageName = this.staticDataService.getCodeName("ORDER_STAGE", stage + "");
        String statusName = this.staticDataService.getCodeName("ORDER_STATUS", status);

        String newStageName = this.staticDataService.getCodeName("ORDER_STAGE", newStatusCfg.getOrderStage() + "");
        String newStatusName = this.staticDataService.getCodeName("ORDER_STATUS", newStatusCfg.getOrderStatus());

        String logContent = "，由订单阶段：" + stageName + "，订单状态：" + statusName + "变为新订单阶段：" + newStageName+"，新订单状态：" + newStatusName;

        this.orderOperLogService.createOrderOperLog(order.getOrderId(), OrderConst.LOG_TYPE_STATUS_TRANS, newStatusCfg.getOrderStage(), newStatusCfg.getOrderStatus(), OrderConst.OPER_LOG_CONTENT_STATUS_CHANGE+logContent);
    }
}
