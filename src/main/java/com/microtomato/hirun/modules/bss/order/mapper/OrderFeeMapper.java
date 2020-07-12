package com.microtomato.hirun.modules.bss.order.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.DesignFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.ProjectFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.QueryDesignFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.QueryProjectFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 订单费用表 Mapper 接口
 * </p>
 *
 * @author sunxin
 * @since 2020-02-05
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderFeeMapper extends BaseMapper<OrderFee> {

    /**
     * 根据orderId查询设计费信息记录
     * @param orderId
     * @return
     */
    @Select("select a.act_fee,a.id,a.order_id,a.fee_item_id,b.payment_type,a.create_time collectionDate,a.remark summary,a.fee_employee_id,b.fee detail_fee" +
            " from order_fee a,order_paymoney b\n" +
            " where a.id =b.fee_id\n" +
            "and a.parent_fee_item_id ='-1'\n "+
            " and a.order_id=#{orderId}\n")
    List<OrderFeeDTO> loadDesignFeeInfo(Long orderId);

    @Select("select b.cust_id, b.cust_name, b.cust_no, a.order_id, a.decorate_address, a.house_layout, a.indoor_area, a.shop_id, a.type, a.status, c.fee_no\n" +
            "from cust_base b, order_base a \n" +
            "left join order_fee c on (c.order_id = a.order_id and c.type = '1' and end_date > now() ) \n" +
            "${ew.customSqlSegment}"
    )
    IPage<DesignFeeDTO> queryDesignFee(IPage<QueryDesignFeeDTO> queryCondition, @Param(Constants.WRAPPER) Wrapper wrapper);


    @Select("select b.cust_id, b.cust_name, b.cust_no, a.order_id, a.decorate_address, a.house_layout, a.indoor_area, a.shop_id, a.type, a.status" +
            "from cust_base b, order_base a \n" +
            "${ew.customSqlSegment}"
    )
    IPage<ProjectFeeDTO> queryProjectFee(IPage<QueryProjectFeeDTO> queryCondtion, @Param(Constants.WRAPPER) Wrapper wrapper);
}
