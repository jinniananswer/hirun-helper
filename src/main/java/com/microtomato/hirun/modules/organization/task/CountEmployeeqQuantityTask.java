package com.microtomato.hirun.modules.organization.task;

import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IStatEmployeeQuantityMonthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 每月统计在岗人数
 *
 * @author liuhui
 * @date 2019-11-27
 */
@Slf4j
@Component
public class CountEmployeeqQuantityTask {


    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IStatEmployeeQuantityMonthService employeeQuantityMonthService;


    /**
     * 每月第一天 00:30 开始执行。
     */
    @Scheduled(cron = "0 32 18 * * ?")
    public void scheduled() {
        List<StatEmployeeQuantityMonth> statEmployeeQuantityMonthList = employeeService.countEmployeeQuantityByOrgId();
        if (ArrayUtils.isEmpty(statEmployeeQuantityMonthList)) {
            return;
        }
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        String month = String.valueOf(date.get(Calendar.MONTH) + 1);

        List<StatEmployeeQuantityMonth> addList = new ArrayList<>();
        List<StatEmployeeQuantityMonth> updateList = new ArrayList<>();


        for (StatEmployeeQuantityMonth statEmployeeQuantityMonth : statEmployeeQuantityMonthList) {
            StatEmployeeQuantityMonth employeeQuantityMonth = employeeQuantityMonthService.queryCountRecord(year, month, statEmployeeQuantityMonth.getOrgId());
            //根据统计数据查询该部门在该年该月是否已存在过记录，如果没有，则新增，如果有，则取最新的合计数据做更新
            if (employeeQuantityMonth == null) {
                statEmployeeQuantityMonth.setYear(year);
                statEmployeeQuantityMonth.setMonth(month);
                addList.add(statEmployeeQuantityMonth);
            } else {
                employeeQuantityMonth.setEmployeeQuantity(statEmployeeQuantityMonth.getEmployeeQuantity());
                updateList.add(employeeQuantityMonth);
            }
        }
        if (ArrayUtils.isNotEmpty(addList)) {
            employeeQuantityMonthService.saveBatch(addList);
        }

        if (ArrayUtils.isNotEmpty(updateList)) {
            employeeQuantityMonthService.updateBatchById(updateList);
        }

    }
}
