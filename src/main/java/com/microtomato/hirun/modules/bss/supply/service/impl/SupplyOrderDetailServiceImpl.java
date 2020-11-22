package com.microtomato.hirun.modules.bss.supply.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrderDetail;
import com.microtomato.hirun.modules.bss.supply.mapper.SupplyOrderDetailMapper;
import com.microtomato.hirun.modules.bss.supply.service.ISupplyOrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 供应订单明细表(SupplyOrderDetail)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-15 11:26:08
 */
@Service
@Slf4j
@DataSource(DataSourceKey.INS)
public class SupplyOrderDetailServiceImpl extends ServiceImpl<SupplyOrderDetailMapper, SupplyOrderDetail> implements ISupplyOrderDetailService {

    @Autowired
    private SupplyOrderDetailMapper supplyOrderDetailMapper;
    

}