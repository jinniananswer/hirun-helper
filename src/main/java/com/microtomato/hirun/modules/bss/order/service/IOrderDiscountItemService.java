package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.dto.OrderDiscountItemDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderDiscountItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单优惠项 服务类
 * </p>
 *
 * @author anwx
 * @since 2020-02-26
 */
public interface IOrderDiscountItemService extends IService<OrderDiscountItem> {

    public List<OrderDiscountItemDTO> list(Long orderId);

    public void save(List<OrderDiscountItemDTO> dtoList);

    List<OrderDiscountItemDTO> queryByOrderId(Long orderId);
}
