package com.microtomato.hirun.modules.bss.order.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.order.entity.dto.*;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.FinanceOrderTaskDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.finance.FinanceOrderTaskQueryDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderBase;
import com.microtomato.hirun.modules.system.entity.dto.PendingTaskDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 订单主表 Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2020-02-03
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderBaseMapper extends BaseMapper<OrderBase> {

    @Select("select a.cust_id, a.order_id, a.status,a.decorate_address, b.cust_name, b.mobile_no, a.create_time from order_base a, cust_base b, (select distinct x.order_id from order_worker x, order_base y where y.order_id = x.order_id and x.employee_id = #{employeeId} and y.status in (${statuses})) c " +
            " where b.cust_id = a.cust_id and a.order_id = c.order_id and a.status in (${statuses}) " +
            " order by status, create_time desc"
    )
    List<PendingOrderDTO> queryAttentionStatusOrders(@Param("statuses")String statuses, @Param("employeeId") Long employeeId);

    @Select("select a.order_id, a.cust_id, a.houses_id, a.type, a.decorate_address, a.house_layout, a.floorage, a.indoor_area, a.stage, a.status, a.design_pay/100 design_pay, a.contract_pay/100 contract_pay, a.second_contract_pay/100 second_contract_pay, a.settlement_pay/100 settlement_pay, b.cust_name, b.mobile_no, b.sex from order_base a, cust_base b " +
            "${ew.customSqlSegment}")
    IPage<CustOrderInfoDTO> queryCustOrderInfo(Page<CustOrderQueryDTO> queryCondition, @Param(Constants.WRAPPER) Wrapper wrapper);


    @Select("select a.cust_id, a.order_id, a.status,a.decorate_address, b.cust_name, b.mobile_no, a.create_time, c.total_money/100 total_money, c.pay_no, c.audit_status,c.pay_date from order_base a, cust_base b, order_pay_no c " +
            " where b.cust_id = a.cust_id and a.order_id = c.order_id and c.order_id = a.order_id and c.audit_status in (${statuses}) and c.end_date > now() " +
            " order by status, create_time desc"
    )
    List<FinancePendingOrderDTO> queryFinancePendingOrders(@Param("employeeId") Long employeeId, @Param("statuses")String statuses);

    @Select("select a.cust_id, a.order_id, a.status,a.decorate_address,a.house_layout, a.houses_id, a.indoor_area,a.type, b.cust_no, b.cust_name, b.mobile_no, a.create_time from order_base a, cust_base b" +
            " ${ew.customSqlSegment}"
    )
    List<OrderTaskDTO> queryOrderTaskInConsole(@Param(Constants.WRAPPER)Wrapper wrapper);

    @Select("select a.cust_id, a.order_id, a.status,a.decorate_address,a.house_layout, a.houses_id, a.indoor_area,a.type, b.cust_no, b.cust_name, b.mobile_no, a.create_time, c.total_money/100 total_money, c.pay_no, c.audit_status,c.pay_date, c.finance_employee_id from order_base a, cust_base b, order_pay_no c " +
            " ${ew.customSqlSegment}"
    )
    IPage<FinanceOrderTaskDTO> queryFinanceOrderTaskInConsole(IPage<FinanceOrderTaskQueryDTO> condition, @Param(Constants.WRAPPER)Wrapper wrapper);

    @Select("SELECT a.order_id,a.contract_fee FROM order_base a " +
            " WHERE a.order_id IN (" +
            " SELECT a.order_id FROM order_base a, order_worker b" +
            " WHERE a.`order_id` = b.`order_id`" +
            " AND b.`employee_id` = #{employeeId})" +
            " AND a.`create_time` >= #{startDate}" +
            " AND a.`create_time` <= #{endDate}"
    )
    List<OrderBase> queryOrderBaseByEmployee(@Param("employeeId") Long employeeId, @Param("startDate")LocalDateTime startDate, @Param("endDate")LocalDateTime endDate);

    @Select("select a.status name, count(1) num from order_base a where a.status in (${statuses}) and a.type=#{type} and exists(select 1 from order_worker b where b.employee_id = #{employeeId}) group by a.status ")
    List<PendingTaskDTO> queryOrderStatusPendingTasks(@Param("statuses")String statuses, @Param("employeeId")Long employeeId, @Param("type")String type);

    @Select("select ifnull(d.num, 0) from (\n" +
            "SELECT date_format(date_add(now(), INTERVAL 0 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -1 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -2 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -3 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -4 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -5 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -6 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -7 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -8 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -9 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -10 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -11 MONTH), '%Y%m') AS month\n" +
            ") a left join\n" +
            "(select date_format(b.create_time, '%Y%m') as month, count(1) num from order_base b where exists (select 1 from order_worker c where c.order_id = b.order_id and c.employee_id = #{employeeId}) group by month) d\n" +
            "on d.month = a.month\n" +
            "order by a.month")
    List<Integer> countYearOrdersByEmployee(@Param("employeeId")Long employeeId);

    @Select("select ifnull(d.num, 0) from (\n" +
            "SELECT date_format(date_add(now(), INTERVAL 0 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -1 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -2 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -3 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -4 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -5 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -6 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -7 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -8 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -9 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -10 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -11 MONTH), '%Y%m') AS month\n" +
            ") a left join\n" +
            "(select date_format(b.create_time, '%Y%m') as month, count(1) num from order_base b where exists (select 1 from order_worker c where c.order_id = b.order_id and c.employee_id = #{employeeId}) and exists(select 1 from order_pay_no p where p.order_id = b.order_id and p.end_date > now()) group by month) d\n" +
            "on d.month = a.month\n" +
            "order by a.month")
    List<Integer> countYearOrdersHasMoneyByEmployee(@Param("employeeId")Long employeeId);

    @Select("select ifnull(d.num, 0) from (\n" +
            "SELECT date_format(date_add(now(), INTERVAL 0 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -1 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -2 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -3 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -4 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -5 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -6 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -7 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -8 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -9 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -10 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -11 MONTH), '%Y%m') AS month\n" +
            ") a left join\n" +
            "(select date_format(b.create_time, '%Y%m') as month, count(1) num from order_base b where shop_id in (${orgId}) group by month) d\n" +
            "on d.month = a.month\n" +
            "order by a.month")
    List<Integer> countYearOrdersByOrg(@Param("orgId")String orgId);

    @Select("select ifnull(d.num, 0) from (\n" +
            "SELECT date_format(date_add(now(), INTERVAL 0 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -1 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -2 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -3 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -4 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -5 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -6 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -7 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -8 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -9 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -10 MONTH), '%Y%m') AS month\n" +
            "UNION SELECT date_format(date_add(now(), INTERVAL -11 MONTH), '%Y%m') AS month\n" +
            ") a left join\n" +
            "(select date_format(b.create_time, '%Y%m') as month, count(1) num from order_base b where shop_id in (${orgId}) and exists(select 1 from order_pay_no p where p.order_id = b.order_id and p.end_date > now()) group by month) d\n" +
            "on d.month = a.month\n" +
            "order by a.month")
    List<Integer> countYearOrdersHasMoneyByOrg(@Param("orgId")String orgId);
}
