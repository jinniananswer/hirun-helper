package com.microtomato.hirun.modules.bss.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustQueryCondDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustomerInfoDetailDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.QueryCustCondDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.Party;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 客户信息 服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-04-20
 */
public interface IPartyService extends IService<Party> {
    /**
     *查询客户列表
     * @param condDTO
     * @return
     */
    IPage<CustomerInfoDetailDTO> queryCustomerInfo(QueryCustCondDTO condDTO);

    /**
     * 查询客户代表环节录入的信息
     * @param partyId
     * @return
     */
    CustomerInfoDetailDTO queryPartyInfoDetail(Long partyId);
}
