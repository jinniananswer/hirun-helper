package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.LineChartDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.PayGatherDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.QueryPayGatherDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.QueryPayTrendDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;

import java.util.List;

/**
 * 订单支付流水表表(OrderPayNo)表服务接口
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-02-29 11:01:48
 */
public interface IOrderPayNoService extends IService<OrderPayNo> {

    OrderPayNo getByOrderIdAndPayNo(Long orderId, Long orderPayNo);

    List<OrderPayNo> queryByOrderId(Long orderId);

    List<PayGatherDTO> queryPayGather(QueryPayGatherDTO condition);

    LineChartDTO queryPayTrend(QueryPayTrendDTO condition);
}