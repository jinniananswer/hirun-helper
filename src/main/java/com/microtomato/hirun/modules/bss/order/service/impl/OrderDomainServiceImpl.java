package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.modules.bss.order.entity.dto.OrderInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.service.IOrderBaseService;
import com.microtomato.hirun.modules.bss.order.service.IOrderDomainService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
