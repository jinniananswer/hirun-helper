package com.microtomato.hirun.modules.bss.customer.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.modules.bi.middleproduct.entity.po.MidprodOpen;
import com.microtomato.hirun.modules.bss.customer.entity.dto.*;
import com.microtomato.hirun.modules.bss.customer.entity.po.Party;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户信息 服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-04-20
 */
public interface ICustomerDomainService {
    /**
     * 查询客户详细信息
     * @param customerId
     * @param openId
     * @param partyId
     * @return
     */
    CustomerInfoDetailDTO queryCustomerInfoDetail(Long customerId, String openId, Long partyId);

    /**
     * 查询动作完成情况
     * @param customerId
     * @param openId
     * @param partyId
     * @return
     */
    List<CustomerActionInfoDTO> getActionInfo(Long customerId, String openId, Long partyId);

    /**
     *
     * @param openId
     * @return
     */
    List<XQLTYInfoDTO> getXQLTYInfo(String openId);

    /**
     *
     * @param openId
     * @return
     */
    XQLTEInfoDTO getXQLTEInfo(String openId);

    /**
     *查询客户列表
     * @param condDTO
     * @return
     */
    IPage<CustomerInfoDetailDTO> queryCustomerInfo(QueryCustCondDTO condDTO);

    /**
     *
     * @param openId
     * @return
     */
    List<LTZDSInfoDTO> getLtzdsInfo(String openId);


    List<MidprodOpen> getMidPordInfo(String openId);
}
