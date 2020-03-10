package com.microtomato.hirun.modules.bss.config.service.impl;

import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.bss.config.entity.po.OrderStatusCfg;
import com.microtomato.hirun.modules.bss.config.mapper.OrderStatusCfgMapper;
import com.microtomato.hirun.modules.bss.config.service.IOrderStatusCfgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 订单阶段及状态配置表 服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-09
 */
@Slf4j
@Service
public class OrderStatusCfgServiceImpl extends ServiceImpl<OrderStatusCfgMapper, OrderStatusCfg> implements IOrderStatusCfgService {

    /**
     * 获取所有的状态配置
     * @return
     */
    @Override
    @Cacheable(value = "orderstatus-cfg-all")
    public List<OrderStatusCfg> getAll() {
        return this.list();
    }

    /**
     * 根据订单状态值获取订单状态配置
     * @param orderStatus
     * @return
     */
    @Override
    @Cacheable(value = "orderstatus-cfg-with-status")
    public OrderStatusCfg getCfgByTypeStatus(String orderType, String orderStatus) {
        IOrderStatusCfgService service = SpringContextUtils.getBean(OrderStatusCfgServiceImpl.class);
        List<OrderStatusCfg> datas = service.getAll();

        if (ArrayUtils.isEmpty(datas)) {
            return null;
        }

        for (OrderStatusCfg data : datas) {
            if (StringUtils.equals(data.getOrderStatus(), orderStatus) && StringUtils.equals(orderType, data.getType())) {
                return data;
            }
        }
        return null;
    }

    /**
     * 根据主键获取订单状态配置
     * @param id
     * @return
     */
    @Override
    @Cacheable(value = "orderstatus-cfg-with-id")
    public OrderStatusCfg getById(Long id) {
        IOrderStatusCfgService service = SpringContextUtils.getBean(OrderStatusCfgServiceImpl.class);
        List<OrderStatusCfg> datas = service.getAll();

        if (ArrayUtils.isEmpty(datas)) {
            return null;
        }

        for (OrderStatusCfg data : datas) {
            if (id.equals(data.getId())) {
                return data;
            }
        }
        return null;
    }
}
