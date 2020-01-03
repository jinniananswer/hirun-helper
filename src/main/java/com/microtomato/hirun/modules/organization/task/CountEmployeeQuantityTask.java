package com.microtomato.hirun.modules.organization.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQuantityStatDTO;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;
import com.microtomato.hirun.modules.organization.mapper.StatEmployeeQuantityMonthMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IStatEmployeeQuantityMonthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 每月统计在岗人数
 *
 * @author liuhui
 * @date 2019-11-27
 */
@Slf4j
@Component
public class CountEmployeeQuantityTask {


    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IStatEmployeeQuantityMonthService employeeQuantityMonthService;

    @Autowired
    private StatEmployeeQuantityMonthMapper mapper;


    /**
     * 每月第一天 00:30 开始执行。
     */
    @Scheduled(cron = "0 05 0 * * ?")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void scheduled() {
        List<EmployeeQuantityStatDTO> dtoList = employeeService.countEmployeeQuantityByOrgId();
        if (ArrayUtils.isEmpty(dtoList)) {
            return;
        }

        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        String month = String.valueOf(date.get(Calendar.MONTH) + 1);

        List<StatEmployeeQuantityMonth> addList = new ArrayList<>();
        //删除原有记录
        this.mapper.delete(Wrappers.<StatEmployeeQuantityMonth>lambdaQuery().eq(StatEmployeeQuantityMonth::getMonth, month)
                .eq(StatEmployeeQuantityMonth::getYear, year));
        //重新新增
        for (EmployeeQuantityStatDTO dto : dtoList) {

            StatEmployeeQuantityMonth statEmployeeQuantityMonth = new StatEmployeeQuantityMonth();
            BeanUtils.copyProperties(dto, statEmployeeQuantityMonth);
            statEmployeeQuantityMonth.setYear(year);
            statEmployeeQuantityMonth.setMonth(month);
            addList.add(statEmployeeQuantityMonth);

        }
        if (ArrayUtils.isNotEmpty(addList)) {
            employeeQuantityMonthService.saveBatch(addList);
        }


    }
}
