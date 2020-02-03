package com.microtomato.hirun.modules.bss.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorker;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2020-02-03
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderWorkerMapper extends BaseMapper<OrderWorker> {

    @Select("select a.order_id, a.role_id, b.name from order_worker a, ins_employee b where b.employee_id = a.employee_id and a.order_id = #{orderId} and now() between a.start_date and a.end_date")
    List<OrderWorkerDTO> queryByOrderId(Long orderId);
}
