package com.microtomato.hirun.modules.bss.customer.service;

import com.microtomato.hirun.modules.bss.customer.entity.dto.CustConsultDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustQueryCondDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.CustBase;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 客户信息 服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-01
 */
public interface ICustBaseService extends IService<CustBase> {

    CustBase queryByCustId(Long custId);

    CustInfoDTO queryByCustIdOrOrderId(Long custId, Long orderId);

    List<CustInfoDTO> queryCustomerInfo(CustQueryCondDTO condDTO);

    void saveCustomerConsultInfo(CustConsultDTO dto);

    void submitMeasure(CustConsultDTO dto);

    void submitSneak(CustConsultDTO dto);

}
