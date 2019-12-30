package com.microtomato.hirun.modules.organization.task;

import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQuantityStatDTO;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IStatEmployeeQuantityMonthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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


    /**
     * 每月第一天 00:30 开始执行。
     */
    @Scheduled(cron = "0 04 10 * * ?")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void scheduled() {
        List<EmployeeQuantityStatDTO> dtoList = employeeService.countEmployeeQuantityByOrgId();
        if (ArrayUtils.isEmpty(dtoList)) {
            return;
        }

        Map<String, StatEmployeeQuantityMonth> tempMap = new HashMap<>();
        //查询出来的结果变成横表结构
        for (EmployeeQuantityStatDTO dto : dtoList) {
            Long orgId = dto.getOrgId();
            String jobRole = dto.getJobRole();
            String jobRoleNature = dto.getJobRoleNature();
            String orgNature = dto.getOrgNature();
            if (!tempMap.containsKey(orgId + "" + jobRole + jobRoleNature + orgNature)) {
                StatEmployeeQuantityMonth statEmployeeQuantityMonth = new StatEmployeeQuantityMonth();
                BeanUtils.copyProperties(dto, statEmployeeQuantityMonth);
                if (StringUtils.isEmpty(dto.getInMonths())) {
                    statEmployeeQuantityMonth.setLessMonthQuantity(0);
                    statEmployeeQuantityMonth.setMoreMonthQuantity(0);
                    statEmployeeQuantityMonth.setEmployeeSum(0);
                } else {
                    if (StringUtils.equals(dto.getInMonths(), "m")) {
                        statEmployeeQuantityMonth.setMoreMonthQuantity(dto.getEmployeeSum());
                        statEmployeeQuantityMonth.setLessMonthQuantity(0);
                        statEmployeeQuantityMonth.setEmployeeSum(dto.getEmployeeSum());

                    } else {
                        statEmployeeQuantityMonth.setLessMonthQuantity(dto.getEmployeeSum());
                        statEmployeeQuantityMonth.setMoreMonthQuantity(0);
                        statEmployeeQuantityMonth.setEmployeeSum(dto.getEmployeeSum());
                    }
                }
                tempMap.put(orgId + "" + jobRole + jobRoleNature + orgNature, statEmployeeQuantityMonth);
            } else {
                StatEmployeeQuantityMonth tempPO = tempMap.get(dto.getOrgId() + dto.getJobRole() + dto.getJobRoleNature() + dto.getOrgNature());
                if (StringUtils.isEmpty(dto.getInMonths())) {

                } else {
                    if (StringUtils.equals(dto.getInMonths(), "m")) {
                        tempPO.setMoreMonthQuantity(dto.getEmployeeSum());
                        if (tempPO.getLessMonthQuantity() == null) {
                            tempPO.setEmployeeSum(dto.getEmployeeSum());
                        } else {
                            tempPO.setEmployeeSum(dto.getEmployeeSum() + tempPO.getLessMonthQuantity());
                        }
                    } else {
                        tempPO.setLessMonthQuantity(dto.getEmployeeSum());
                        if (tempPO.getMoreMonthQuantity() == null) {
                            tempPO.setEmployeeSum(dto.getEmployeeSum());
                        } else {
                            tempPO.setEmployeeSum(dto.getEmployeeSum() + tempPO.getMoreMonthQuantity());
                        }
                    }
                }

            }
        }

        List<StatEmployeeQuantityMonth> tempList=new ArrayList<>();
        Set<String> keys = tempMap.keySet();
        for(String key:keys){
            tempList.add(tempMap.get(key));
        }


        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        String month = String.valueOf(date.get(Calendar.MONTH)+1);

        List<StatEmployeeQuantityMonth> addList = new ArrayList<>();
        List<StatEmployeeQuantityMonth> updateList = new ArrayList<>();


        for (StatEmployeeQuantityMonth statEmployeeQuantityMonth : tempList) {
            StatEmployeeQuantityMonth employeeQuantityMonth = employeeQuantityMonthService.queryCountRecord(year, month, statEmployeeQuantityMonth.getOrgId(),statEmployeeQuantityMonth.getJobRole(),
                    statEmployeeQuantityMonth.getJobRoleNature(),statEmployeeQuantityMonth.getOrgNature() );
            //根据统计数据查询该部门在该年该月是否已存在过记录，如果没有，则新增，如果有，则取最新的合计数据做更新
            if (employeeQuantityMonth == null) {
                statEmployeeQuantityMonth.setYear(year);
                statEmployeeQuantityMonth.setMonth(month);
                addList.add(statEmployeeQuantityMonth);
            } else {
                employeeQuantityMonth.setLessMonthQuantity(statEmployeeQuantityMonth.getLessMonthQuantity());
                employeeQuantityMonth.setMoreMonthQuantity(statEmployeeQuantityMonth.getMoreMonthQuantity());
                employeeQuantityMonth.setEmployeeSum(statEmployeeQuantityMonth.getEmployeeSum());
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
