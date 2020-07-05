package com.microtomato.hirun.modules.bss.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustomerInfoDetailDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.QueryCustCondDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 客户信息 服务类
 * </p>
 *
 * @author liuhui
 * @since 2020-04-20
 */
public interface ICustomerService extends IService<Customer> {
    /**
     * 查询家装顾问环节数据
     * @param customerId
     * @return
     */
    Customer queryCustomerInfo(Long customerId);

    /**
     * 查询customer数据
     * @param condDTO
     * @return
     */
    IPage<CustomerInfoDetailDTO> queryCustomerInfoDetail(QueryCustCondDTO condDTO);
}
