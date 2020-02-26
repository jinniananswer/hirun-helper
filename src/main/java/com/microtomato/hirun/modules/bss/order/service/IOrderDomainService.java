package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.bss.config.entity.dto.CollectFeeDTO;
import com.microtomato.hirun.modules.bss.config.entity.dto.PayComponentDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.*;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 订单领域服务接口类
 * @author: jinnian
 * @create: 2020-02-03 01:34
 **/
public interface IOrderDomainService {

    OrderDetailDTO getOrderDetail(Long orderId);

    List<OrderWorkerDTO> queryOrderWorkers(Long orderId);

    void createNewOrder(NewOrderDTO newOrder);

    void orderStatusTrans(Long orderId, String oper);

    void orderStatusTrans(OrderBase order, String oper);

    List<PendingTaskDTO> queryPendingTask();

    IPage<CustOrderInfoDTO> queryCustOrderInfos(CustOrderQueryDTO queryCondition, Page<CustOrderQueryDTO> page);

    List<PaymentDTO> queryPayment();

    PayComponentDTO initPayComponent();

    void collectFee(CollectFeeDTO feeData);
}
