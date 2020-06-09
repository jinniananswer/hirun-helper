package com.microtomato.hirun.modules.bss.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.order.entity.dto.OrderWorkerActionDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderWorkerAction;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (OrderWorkerAction)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-06-07 15:16:56
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderWorkerActionMapper extends BaseMapper<OrderWorkerAction> {

    @Select("select a.order_id, a.employee_id, a.role_id, b.order_status, b.job_role, b.job_grade, b.org_id ,b.action " +
            "from order_worker a, order_worker_action b " +
            " where b.worker_id = a.id" +
            " and a.orderId =#{orderId} " +
            " and a.orderId =#{orderId} " +
            " and b.end_date > now()"
    )
    List<OrderWorkerActionDTO> queryByOrderId(Long orderId);
}