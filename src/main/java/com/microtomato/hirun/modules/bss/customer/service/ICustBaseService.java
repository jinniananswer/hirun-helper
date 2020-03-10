package com.microtomato.hirun.modules.bss.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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

    IPage<CustInfoDTO> queryCustomerInfo(CustQueryCondDTO condDTO);

    List<CustInfoDTO> queryCustomerInfoByMobile(String mobileNo);

    List<CustInfoDTO> queryCustomer4TransOrder();
}
