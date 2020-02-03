package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderRoleCfg;
import com.microtomato.hirun.modules.bss.config.service.IOrderRoleCfgService;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.service.IOrderBaseService;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
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
}
