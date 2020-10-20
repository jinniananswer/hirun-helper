package com.microtomato.hirun.modules.bss.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.fee.*;
import com.microtomato.hirun.modules.bss.order.entity.po.OrderPayNo;
import com.microtomato.hirun.modules.bss.order.mapper.OrderPayNoMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderPayNoService;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.impl.OrgServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单支付流水表表(OrderPayNo)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-02-29 11:01:48
 */
@Service
@Slf4j
public class OrderPayNoServiceImpl extends ServiceImpl<OrderPayNoMapper, OrderPayNo> implements IOrderPayNoService {

    @Autowired
    private OrderPayNoMapper orderPayNoMapper;

    @Autowired
    private OrgServiceImpl orgService;

    /**
     * 根据订单ID和付款流水号查询订单支付流水信息
     * @param orderId
     * @param orderPayNo
     * @return
     */
    @Override
    public OrderPayNo getByOrderIdAndPayNo(Long orderId, Long orderPayNo) {
        return this.getOne(new QueryWrapper<OrderPayNo>().lambda().eq(OrderPayNo::getOrderId, orderId).eq(OrderPayNo::getPayNo, orderPayNo).gt(OrderPayNo::getEndDate, RequestTimeHolder.getRequestTime()));
    }

    /**
     * 根据订单ID查询订单支付信息
     * @param orderId
     * @return
     */
    @Override
    public List<OrderPayNo> queryByOrderId(Long orderId) {
        return this.list(new QueryWrapper<OrderPayNo>().lambda().eq(OrderPayNo::getOrderId, orderId).gt(OrderPayNo::getEndDate, RequestTimeHolder.getRequestTime()));
    }

    /**
     * 查询主营收款汇总数据
     * @param condition
     * @return
     */
    @Override
    public List<PayGatherDTO> queryPayGather(QueryPayGatherDTO condition) {
        String orgIds = condition.getOrgIds();
        LocalDate[] queryDate = condition.getQueryDate();

        if (StringUtils.isBlank(orgIds) || ArrayUtils.isEmpty(queryDate)) {
            return null;
        }
        String[] orgIdArray = null;
        if (StringUtils.isNotBlank(orgIds)) {
            orgIdArray = orgIds.split(",");
        }

        if (ArrayUtils.isEmpty(orgIdArray)) {
            return null;
        }

        List<Long> orgList = new ArrayList<>();
        for (String orgId : orgIdArray) {
            Org org = this.orgService.queryByOrgId(Long.parseLong(orgId));
            if (StringUtils.equals("4", org.getType())) {
                orgList.add(org.getOrgId());
            }
        }

        if (ArrayUtils.isEmpty(orgList)) {
            return null;
        }

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        QueryWrapper<QueryDesignFeeDTO> wrapper = new QueryWrapper<>();
        wrapper.apply(" b.order_id = a.order_id ");
        wrapper.apply(" c.org_id = a.shop_id ");
        wrapper.in("a.shop_id", orgList);
        wrapper.gt("b.end_date", now);
        wrapper.gt("b.pay_date", queryDate[0]);
        wrapper.le("b.pay_date", queryDate[1]);
        wrapper.groupBy("a.shop_id");

        QueryWrapper<QueryDesignFeeDTO> wrapper2 = new QueryWrapper<>();
        wrapper2.in("e.org_id", orgList);


        List<PayGatherDTO> datas = this.orderPayNoMapper.queryPayGather(wrapper, wrapper2);
        if (ArrayUtils.isNotEmpty(datas)) {
            for (PayGatherDTO data : datas) {
                Org org = this.orgService.queryByOrgId(Long.parseLong(data.getOrgId()));
                data.setOrgName(org.getName());
                String date = TimeUtils.formatLocalDateToString(queryDate[0], TimeUtils.DATE_FMT_3) + "~" + TimeUtils.formatLocalDateToString(queryDate[1], TimeUtils.DATE_FMT_3);
                data.setQueryDate(date);
                if (data.getMoney() == null) {
                    data.setMoney(0d);
                }
            }
        }
        return datas;
    }

    /**
     * 根据店名和年月查询订单实际收入趋势图
     * @param condition
     * @return
     */
    @Override
    public LineChartDTO queryPayTrend(QueryPayTrendDTO condition) {
        String orgIds = condition.getOrgIds();
        List<String> months = condition.getQueryMonth();

        if (StringUtils.isBlank(orgIds) || ArrayUtils.isEmpty(months)) {
            return null;
        }
        String minDateStr = months.get(0);
        String maxDateStr = months.get(1);

        LocalDate minDate = TimeUtils.stringToDate(minDateStr + "-01", TimeUtils.DATE_FMT_3);
        LocalDate maxDate = TimeUtils.stringToDate(maxDateStr + "-01", TimeUtils.DATE_FMT_3);
        long interval = minDate.until(maxDate, ChronoUnit.MONTHS);
        if (interval < 0) {
            return null;
        }

        String[] orgIdArray = null;
        if (StringUtils.isNotBlank(orgIds)) {
            orgIdArray = orgIds.split(",");
        }
        List<String> orgList = new ArrayList<>();
        for (String orgId : orgIdArray) {
            Org org = this.orgService.queryByOrgId(Long.parseLong(orgId));
            if (StringUtils.equals("4", org.getType())) {
                orgList.add(org.getOrgId() + "");
            }
        }

        List<String> monthNum = new ArrayList<>();
        for (int i = 0; i <= interval; i++) {
            String yearMonth = TimeUtils.addMonths(minDateStr + "-01", TimeUtils.DATE_FMT_3, i).substring(0, 7);
            monthNum.add(yearMonth);
        }

        StringBuilder monthSql = new StringBuilder();
        int i = 0;
        for (String month : monthNum) {
            if (i != 0) {
                monthSql.append("union all ");
            }
            monthSql.append("select '" + month + "' month ");
            i++;
        }

        List<PayTrendDTO> payTrends = this.orderPayNoMapper.queryPayTrend(StringUtils.join(orgList, ","), monthSql.toString(), minDateStr, maxDateStr);
        if (ArrayUtils.isEmpty(payTrends)) {
            return null;
        }

        LineChartDTO lineChart = new LineChartDTO();
        lineChart.setTitle("各店实际收入趋势图");

        List<String> legend = new ArrayList<>();
        List<String> xAxis = new ArrayList<>();
        xAxis.addAll(monthNum);
        List<LineChartDataDTO> datas = new ArrayList<>();

        for (PayTrendDTO payTrend : payTrends) {
            if (!legend.contains(payTrend.getName())) {
                legend.add(payTrend.getName());
            }

            LineChartDataDTO lineChartData = null;

            boolean isFind = false;
            for (LineChartDataDTO data : datas) {
                if (data.getId().equals(payTrend.getOrgId())) {
                    isFind = true;
                    lineChartData = data;
                }
            }

            if (!isFind) {
                lineChartData = new LineChartDataDTO();
                lineChartData.setId(payTrend.getOrgId());
                lineChartData.setName(payTrend.getName());
                lineChartData.setStack("总收入");
                lineChartData.setType("line");
                List<Double> values = new ArrayList<>();
                lineChartData.setData(values);
                datas.add(lineChartData);
            }

            List<Double> values = lineChartData.getData();
            if (payTrend.getMoney() == null) {
                values.add(0d);
            } else {
                values.add(payTrend.getMoney());
            }
        }

        lineChart.setDatas(datas);
        lineChart.setXAxises(xAxis);
        lineChart.setLegends(legend);
        return lineChart;
    }
}