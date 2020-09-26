package com.microtomato.hirun.modules.bss.order.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.order.entity.dto.QueryInspectCondDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderInspectDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderInspect;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * (OrderInspect)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-08-11 18:07:44
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderInspectMapper extends BaseMapper<OrderInspect> {
    @Select("select a.cust_no,a.cust_name,a.mobile_no,d.shop_id, " +
            " c.house_id,c.house_building,c.house_room_no," +
            " b.check_status,b.receive_date,b.settle_status,b.check_settle_date,b.institution,b.apply_date,b.offer_date,b.receive_status, " +
            " b.receive_people,b.guarantee_start_date,b.guarantee_end_date,b.remark, " +
            " f.cust_service_employee_id,f.design_employee_id, d.order_id " +
            " from cust_base a ,order_inspect b , ins_project c,order_base d LEFT JOIN order_consult f on(d.order_id=f.order_id) " +
            " ${ew.customSqlSegment}"
    )
    IPage<OrderInspectDTO> queryOrderInspects(Page<QueryInspectCondDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}