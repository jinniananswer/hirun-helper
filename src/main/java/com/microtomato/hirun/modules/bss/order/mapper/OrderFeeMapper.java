package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderFee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
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

}
