package com.microtomato.hirun.modules.bss.supply.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.supply.entity.dto.QuerySupplyOrderDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyOrderDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 供应订单表(SupplyOrder)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-15 11:26:08
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface SupplyOrderMapper extends BaseMapper<SupplyOrder> {

    @Select("select a.id,a.supply_order_type,a.total_fee,a.create_user_id,a.supply_status,b.supplier_id  " +
            "from supply_order a ,supply_order_detail b,supply_supplier c where a.id =b.supply_id and  b.supplier_id =c.id and c.verify_person = #{employeeId} and a.supply_status = #{status} group by b.supplier_id" )
    IPage<SupplyOrderDTO> querySupplyInfo(IPage<QuerySupplyOrderDTO> queryCondtion, @Param("employeeId") Long employeeId,@Param("status")String status);

}