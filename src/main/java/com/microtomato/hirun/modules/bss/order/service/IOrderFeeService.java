package com.microtomato.hirun.modules.bss.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.order.entity.dto.PayComponentDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.SecondInstallmentCollectionDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;

import java.time.LocalDateTime;
import java.util.List;

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
     * @param orderId
     */
    PayComponentDTO initCostAudit(Long orderId);

    /**
     * 审核费用信息
     * @param dto
     */
    void submitAudit(OrderFeeDTO dto);

    /**
     * 费用复审
     * @param orderPayNo
     */
    void costReview(OrderPayNo orderPayNo);

    /**
     * 根据订单查询订单费用
     * @param orderId
     * @return
     */
    List<OrderFee> queryByOrderId(Long orderId);

    /**
     * 根据订单ID、类型、期数查询订单费用
     * @param orderId
     * @param type
     * @param period
     * @return
     */
    OrderFee getByOrderIdTypePeriod(Long orderId, String type, Integer period);

    /**
     * 更新审核费用信息
     * @param orderId
     */
    void updateByOrderId(Long orderId, String type, Integer periods, String auditStatus, Long employeeId, String auditRemark, LocalDateTime auditTime);
}
