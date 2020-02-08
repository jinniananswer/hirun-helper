package com.microtomato.hirun.modules.bss.config.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusCfg;
import com.microtomato.hirun.modules.bss.config.mapper.OrderStatusCfgMapper;
import com.microtomato.hirun.modules.bss.config.service.IOrderStatusCfgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单阶段及状态配置表 服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-07
 */
@Slf4j
@Service
public class OrderStatusCfgServiceImpl extends ServiceImpl<OrderStatusCfgMapper, OrderStatusCfg> implements IOrderStatusCfgService {


    @Override
    @Cacheable(value = "orderstatus-cfg-all")
    public List<OrderStatusCfg> getAll() {
        return this.list();
    }

    @Override
    @Cacheable(value = "orderstatus-cfg-with-status")
    public OrderStatusCfg getCfgByStatus(String orderStatus) {
        IOrderStatusCfgService service = SpringContextUtils.getBean(OrderStatusCfgServiceImpl.class);
        List<OrderStatusCfg> datas = service.getAll();

        if (ArrayUtils.isEmpty(datas)) {
            return null;
        }

        for (OrderStatusCfg data : datas) {
            if (StringUtils.equals(data.getOrderStatus(), orderStatus)) {
                return data;
            }
        }
        return null;
    }
}
