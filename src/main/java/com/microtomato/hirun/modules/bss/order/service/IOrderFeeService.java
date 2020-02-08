package com.microtomato.hirun.modules.bss.order.service;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * 新增费用信息
     * @param dto
     */
    void addOrderFee(OrderFeeDTO dto);

}
