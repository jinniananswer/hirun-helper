package com.microtomato.hirun.modules.bss.order.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderMaterialContractDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderMaterialContract;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (OrderMaterialContract)表数据库访问层
 *
 * @author
 * @version 1.0.0
 * @date 2020-09-24 00:05:42
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderMaterialContractMapper extends BaseMapper<OrderMaterialContract> {
    /**
     *
     * @param wrapper
     * @return
     */
    @Select("select a.cust_no,b.houses_id,c.id,c.material_type,c.contract_fee,c.discount_fee, " +
            " c.actual_fee,c.brand_code,c.remark,c.order_id " +
            " from cust_base a,order_base b,order_material_contract c " +
            " ${ew.customSqlSegment}"
    )
    List<OrderMaterialContractDTO> queryMaterialContracts(@Param(Constants.WRAPPER) Wrapper wrapper);
}