package com.microtomato.hirun.modules.bss.order.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.order.entity.dto.factory.FactoryOrderInfoDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.factory.QueryFactoryOrderDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFactoryOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * (OrderFactoryOrder)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-09-26 16:32:41
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderFactoryOrderMapper extends BaseMapper<OrderFactoryOrder> {

    @Select("select b.cust_id, b.cust_name, b.cust_no, a.order_id, a.decorate_address, a.house_layout, a.indoor_area, a.shop_id, a.type, a.status, c.produce_no, c.status factory_status\n" +
            "from cust_base b, order_base a \n" +
            "left join order_factory_order c on (c.order_id = a.order_id and end_date > now() ) \n" +
            "${ew.customSqlSegment}"
    )
    IPage<FactoryOrderInfoDTO> queryFactoryOrders(IPage<QueryFactoryOrderDTO> queryCondition, @Param(Constants.WRAPPER) Wrapper wrapper);
}