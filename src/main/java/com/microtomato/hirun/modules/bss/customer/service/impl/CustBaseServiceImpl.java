package com.microtomato.hirun.modules.bss.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.CustBase;
import com.microtomato.hirun.modules.bss.customer.mapper.CustBaseMapper;
import com.microtomato.hirun.modules.bss.customer.service.ICustBaseService;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.bss.order.service.IOrderBaseService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户信息 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-02-01
 */
@Slf4j
@Service
public class CustBaseServiceImpl extends ServiceImpl<CustBaseMapper, CustBase> implements ICustBaseService {

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IOrderBaseService orderBaseService;

    @Override
    public CustBase queryByCustId(Long custId) {
        return this.getById(custId);
    }

    /**
     * 根据客户ID或者订单ID查询客户信息，如果客户ID不为空，永远以客户ID为准进行查询
     * @param custId
     * @param orderId
     * @return
     */
    @Override
    public CustInfoDTO queryByCustIdOrOrderId(Long custId, Long orderId) {
        CustInfoDTO custInfoDTO = new CustInfoDTO();

        if (custId == null && orderId != null) {
            OrderBase orderBase = this.orderBaseService.queryByOrderId(orderId);
            if (orderBase != null) {
                custId = orderBase.getCustId();
            }
        }

        if (custId != null) {
            CustBase custBase = this.queryByCustId(custId);
            if (custBase != null) {
                BeanUtils.copyProperties(custBase, custInfoDTO);

                if (StringUtils.isNotBlank(custInfoDTO.getSex())) {
                    custInfoDTO.setSexName(this.staticDataService.getCodeName("SEX", custInfoDTO.getSex()));
                }
            }
        }

        return custInfoDTO;
    }
}