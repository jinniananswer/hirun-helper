package com.microtomato.hirun.modules.bss.supply.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.supply.entity.dto.QuerySupplyOrderDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyMaterialDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyOrderDetailDTO;
import com.microtomato.hirun.modules.bss.supply.entity.po.SupplyOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    @Select("select a.supply_order_type, a.order_id, b.id, b.supply_id, b.supplier_id, b.material_id, b.fee/ 100 fee, b.num material_num, b.total_money/100 total_money, b.audit_status, b.audit_comment, b.audit_employee_id, b.create_employee_id, b.remark, c.name material_name, c.cost_price/100 cost_price, c.sale_price/100 sale_price, c.material_code, c.id material_id, c.material_type, c.material_unit, d.name supplier_name  " +
            "from supply_order a ,supply_order_detail b,supply_material c, supply_supplier d " +
            "${ew.customSqlSegment} ")
    IPage<SupplyOrderDetailDTO> querySupplyInfo(IPage<QuerySupplyOrderDTO> queryCondtion, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select a.material_id,a.fee,a.storehouse_id,a.num as materialNum " +
            "from supply_order_detail a where a.supply_id =#{supplyId} and a.supplier_id = #{supplierId}" )
    List<SupplyMaterialDTO> querySupplyDetailInfo(@Param("supplyId") Long supplyId, @Param("supplierId")Long supplierId);
}