package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.FeeDTO;
import com.microtomato.hirun.modules.bss.order.service.IFeeDomainService;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 费用领域服务实现类
 * @author: jinnian
 * @create: 2020-03-02 21:11
 **/
public class FeeDomainServiceImpl implements IFeeDomainService {

    /**
     * 创建订单费用信息
     * @param fees 费用项数据
     * @param period 期数 1-首期 2-二期款 3-结算款
     */
    @Override
    public void createOrderFee(List<FeeDTO> fees, Long period) {
        if (ArrayUtils.isEmpty(fees)) {
            return;
        }

        for (FeeDTO fee : fees) {
            //创建费用项信息
        }
    }
}
