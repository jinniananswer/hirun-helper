package com.microtomato.hirun.modules.bss.order.service;

import com.microtomato.hirun.modules.bss.order.entity.dto.FeeDTO;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 费用领域服务接口类
 * @author: jinnian
 * @create: 2020-03-02 21:10
 **/
public interface IFeeDomainService {

    void createOrderFee(Long orderId, String type, Integer period, List<FeeDTO> fees);

    void changeOrderFee(Long orderId, String type, Integer period, List<FeeDTO> fees);

    Long getPayedMoney(Long orderId, String type, Integer period);

    Long getNeedPay(Long fee, Long orderId, String type, Integer period);
}
