package com.microtomato.hirun.modules.bss.order.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.PayGatherDTO;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.PayTrendDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 订单支付流水表表(OrderPayNo)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-02-29 11:01:48
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrderPayNoMapper extends BaseMapper<OrderPayNo> {

    /**
     * 根据部门ID查询主营收款信息
     * @return
     */
    @Select("select e.org_id, d.money from ins_org e left join (select a.shop_id org_id, sum(b.total_money)/100 money from order_base a, order_pay_no b, ins_org c " +
            " ${ew.customSqlSegment}) d on (d.org_id = e.org_id ) ${aw.customSqlSegment}")
    List<PayGatherDTO> queryPayGather(@Param(Constants.WRAPPER) Wrapper wrapper, @Param("aw") Wrapper wrapper2);

    @Select("select y.org_id, y.name, y.month, d.money\n" +
            "from (select x.month, c.org_id, c.name, c.type from (${monthTable}) x, ins_org c where c.type = '4' and c.org_id in (${orgIds})) y\n" +
            "         left join\n" +
            "     (select a.shop_id, date_format(b.pay_date, '%Y-%m') pay_month, sum(total_money/100) money\n" +
            "      from order_base a,\n" +
            "           order_pay_no b\n" +
            "      where b.order_id = a.order_id\n" +
            "        and b.end_date > now()\n" +
            "        and b.start_date >= '${minDate}' " +
            "        and b.start_date < '${maxDate}' " +
            "      group by a.shop_id, date_format(b.pay_date, '%Y-%m')) d on (d.shop_id = y.org_id and d.pay_month = y.month) order by y.org_id, y.month asc")
    List<PayTrendDTO> queryPayTrend(@Param("orgIds")String orgIds, @Param("monthTable") String monthTable, @Param("minDate")String minDate, @Param("maxDate")String maxDate);
}