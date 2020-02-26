package com.microtomato.hirun.modules.bss.order.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;

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
     * 查询已收取费用信息 初始化方法使用
     * @param orderId
     */
    OrderFee queryOrderCollectFee(Long orderId);

    /**
     * 审核费用信息
     * @param dto
     */
    void submitAudit(OrderFeeDTO dto);



}
