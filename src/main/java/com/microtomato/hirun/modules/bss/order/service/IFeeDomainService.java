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

    void createOrderFee(List<FeeDTO> fees, Long period);
}
