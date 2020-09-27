package com.microtomato.hirun.modules.bss.service.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.order.entity.dto.FinancePendingOrderDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.ComplainOrderDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.RepairOrderDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.ServicePendingOrderDTO;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceRepairOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (ServiceRepairOrder)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface ServiceRepairOrderMapper extends BaseMapper<ServiceRepairOrder> {

    @Select("select c.cust_name,b.decorate_address,'1' as type,v.repair_no as code,v.`status`,b.order_id,b.cust_id " +
            " from order_base b,cust_base c," +
            " (select a.customer_id,a.repair_no,a.`status` from service_repair_order a" +
            "  where a.`status` in (${statuses}) " +
            " group by a.customer_id,a.repair_no,a.`status` order by a.accept_time desc) v " +
            " where b.cust_id=c.cust_id and v.customer_id=c.cust_id "
    )
    List<ServicePendingOrderDTO> queryRepairPendingOrders(@Param("employeeId") Long employeeId, @Param("statuses")String statuses);

    /**
     * 保修记录查询
     * @param wrapper
     * @return
     */
    @Select("select a.cust_name,a.cust_no,a.mobile_no,b.houses_id as house_id,b.order_id, " +
            " c.*  " +
            " from cust_base a,order_base b,service_repair_order c " +
            " ${ew.customSqlSegment}"
    )
    List<RepairOrderDTO> queryRepairAllRecord(@Param(Constants.WRAPPER) Wrapper wrapper);

}