package com.microtomato.hirun.modules.bss.order.service;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPaymoney;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单费用表 服务类
 * </p>
 *
 * @author sunxin
 * @since 2020-02-05
 */
public interface IOrderFeeService extends IService<OrderFee> {

    /**
     * 新增设计费信息
     * @param dto
     */
    void addDesignFee(OrderFeeDTO dto);

    /**
     * 新增订单费用信息
     * @param orderFee
     */
    void addOrderFee(OrderFee orderFee);


    /**
     * 新增订单收费方式信息
     * @param orderPaymoney
     */
    void addOrderPaymoney(OrderPaymoney orderPaymoney);



    /**
     * 查询设计费用信息
     * @param orderId
     */
    OrderFeeDTO loadDesignFeeInfo(Long orderId);

}
