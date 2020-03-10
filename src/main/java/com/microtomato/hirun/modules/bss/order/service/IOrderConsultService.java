package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.customer.entity.dto.CustConsultDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderConsult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-15
 */
public interface IOrderConsultService extends IService<OrderConsult> {
    /**
     * 查询客户咨询信息
      * @param orderId
     * @return
     */
    OrderConsult queryOrderConsult(Long orderId);

    void saveCustomerConsultInfo(CustConsultDTO dto);

    void submitMeasure(CustConsultDTO dto);

    void submitSneak(CustConsultDTO dto);

    void transOrder(CustConsultDTO dto);

    CustConsultDTO queryOrderConsultForTrans(Long orderId);
}
