package com.microtomato.hirun.modules.bss.order.service;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustConsultDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderConsult;
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
     * 新增首期工程款信息
     * @param dto
     */
    void addDownPayment(OrderFeeDTO dto);

    /**
     * 新增订单大类费用信息
     * @param orderFee
     */
  //  void addOrderFeeTotal(OrderFee orderFee);

    /**
     * 新增订单小类费用信息
     * @param orderFee
     */
  //  void addOrderFeeDetail(OrderFee orderFee);

    /**
     * 更新订单大类费用信息
     * @param orderFee
     */
  //  void updateOrderFeeTotal(OrderFee orderFee,String tag);

    /**
     * 更新订单小类费用信息
     * @param orderFee
     */
   // void updateOrderFeeDetail(OrderFee orderFee,String tag);


    /**
     * 新增订单收费方式信息
     * @param dto,id
     */
    void addOrderPaymoney(OrderFeeDTO dto,long id);



    /**
     * 查询设计费用信息
     * @param orderId
     */
    OrderFeeDTO loadDesignFeeInfo(Long orderId);

    /**
     * 查询工程费用信息
     * @param orderId
     */
  //  OrderFeeDTO loadProjectFeeInfo(Long orderId,String period);

    /**
     * 修改大类及小类费用信息（包含设计费，主材等）
     * @param dto
     */
    void auditUpdateForTotal(OrderFeeDTO dto);


    /**
     * 修改工程费用信息
     * @param dto
     */
   // void auditUpdateForProject(OrderFeeDTO dto);

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
