package com.microtomato.hirun.modules.bss.order.service.impl;

import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.order.mapper.OrderBaseMapper;
import com.microtomato.hirun.modules.bss.order.service.IOrderStatisticService;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.StatisticBarDTO;
import com.microtomato.hirun.modules.organization.entity.dto.StatisticBarValueDTO;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IEmployeeStatisticService;
import com.microtomato.hirun.modules.system.entity.po.RoleConsoleConfig;
import com.microtomato.hirun.modules.system.service.IRoleConsoleConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 订单统计相关的服务
 * @author: jinnian
 * @create: 2020-07-28 11:10
 **/
@Slf4j
@Service
public class OrderStatisticServiceImpl implements IOrderStatisticService {

    @Autowired
    private OrderBaseMapper orderBaseMapper;

    @Autowired
    private IRoleConsoleConfigService roleConsoleConfigService;

    @Autowired
    private IEmployeeStatisticService employeeStatisticService;

    /**
     * 首页控制台图表
     * @return
     */
    @Override
    public StatisticBarDTO consoleBar() {
        Long roleId = WebContextUtils.getUserContext().getMainRoleId();
        if (roleId == null) {
            return null;
        }

        RoleConsoleConfig consoleConfig = this.roleConsoleConfigService.getRoleConsole(roleId);
        if (consoleConfig == null) {
            return this.employeeStatisticService.countInAndDestroyOneYear();
        }

        String chartType = consoleConfig.getChartType();
        if (StringUtils.equals("1", chartType)) {
            //按员工统计近一年订单数据
            return this.countYearOrdersByEmployee();
        } else if (StringUtils.equals("2", chartType)) {
            //按店面统计近一年订单数据
            return this.countYearOrdersByShop();
        } else if (StringUtils.equals("3", chartType)) {
            //按分公司统计近一年订单数据
            return this.countYearOrdersByCompany();
        }

        return null;
    }

    /**
     * 近一年按员工统计订单数量
     * @return
     */
    public StatisticBarDTO countYearOrdersByEmployee() {
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();
        List<Integer> orders = this.orderBaseMapper.countYearOrdersByEmployee(employeeId);
        List<Integer> moneyOrders = this.orderBaseMapper.countYearOrdersHasMoneyByEmployee(employeeId);
        return this.buildStatisticOrderCountBar(orders, moneyOrders);
    }

    /**
     * 近一年按店面统计订单数量
     * @return
     */
    public StatisticBarDTO countYearOrdersByShop() {
        Long orgId = WebContextUtils.getUserContext().getOrgId();
        OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);

        Org shop = orgDO.getBelongShop();

        if (shop == null) {
            return null;
        }
        List<Integer> orders = this.orderBaseMapper.countYearOrdersByOrg(shop.getOrgId() + "");
        List<Integer> moneyOrders = this.orderBaseMapper.countYearOrdersHasMoneyByOrg(shop.getOrgId() + "");
        return this.buildStatisticOrderCountBar(orders, moneyOrders);
    }

    /**
     * 近一年按公司统计订单数量，如果是事业部或者集团，则统计的是整个家装分公司的数据
     * @return
     */
    public StatisticBarDTO countYearOrdersByCompany() {
        Long orgId = WebContextUtils.getUserContext().getOrgId();
        OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);

        Org company = orgDO.getBelongCompany();

        if (company == null) {
            return null;
        }

        String line = orgDO.getOrgLine(company.getOrgId());
        List<Integer> orders = this.orderBaseMapper.countYearOrdersByOrg(line);
        List<Integer> moneyOrders = this.orderBaseMapper.countYearOrdersHasMoneyByOrg(line);
        return this.buildStatisticOrderCountBar(orders, moneyOrders);
    }

    /**
     * 构建订单统计图表
     * @param orders
     * @param moneyOrders
     * @return
     */
    private StatisticBarDTO buildStatisticOrderCountBar(List<Integer> orders, List<Integer> moneyOrders) {
        StatisticBarDTO bar = new StatisticBarDTO();
        bar.setTitle("订单数与已交费订单");
        bar.setSubtitle("最近12个月");
        List<String> legend = new ArrayList<>();
        legend.add("总订单数");
        legend.add("已交费订单");
        bar.setLegend(legend);

        List<String> months = new ArrayList<>();
        for (int i=11;i>=0;i--) {
            String month = TimeUtils.addMonths(TimeUtils.now(TimeUtils.DATE_FMT_3), TimeUtils.DATE_FMT_3, -i);
            month = month.substring(0,4) + month.substring(5,7);
            months.add(month);
        }
        bar.setXAxis(months);

        StatisticBarValueDTO total = new StatisticBarValueDTO();
        StatisticBarValueDTO payed = new StatisticBarValueDTO();

        total.setName("总订单数");
        total.setData(orders);

        payed.setName("已交费订单");
        payed.setData(moneyOrders);

        List<StatisticBarValueDTO> values = new ArrayList<>();
        values.add(total);
        values.add(payed);

        bar.setYAxis(values);

        return bar;
    }
}
