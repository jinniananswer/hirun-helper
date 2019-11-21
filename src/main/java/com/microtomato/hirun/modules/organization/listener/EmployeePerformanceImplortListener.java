package com.microtomato.hirun.modules.organization.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeePerformanceImportDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeePerformance;
import com.microtomato.hirun.modules.organization.service.IEmployeePerformanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuhui
 * @date 2019-11-20
 */
@Slf4j
public class EmployeePerformanceImplortListener extends AnalysisEventListener<EmployeePerformanceImportDTO> {


    private static final int BATCH_COUNT = 5;

    private List<EmployeePerformance> list = new ArrayList<>(BATCH_COUNT);
    private List<EmployeePerformanceImportDTO> failList = new ArrayList<>();
    private IEmployeePerformanceService performanceService;


    public EmployeePerformanceImplortListener(IEmployeePerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    /**
     * 每读取一行触发一次该函数
     *
     * @param data    excel 读过来的一行数据
     * @param context 上下文信息
     */
    @Override
    public void invoke(EmployeePerformanceImportDTO data, AnalysisContext context) {
        if (data.getEmployeeId() == null || data.getPerformance() == null || data.getYear() == null) {
            data.setResultInfo("员工编码、年份、绩效为必填！");
            failList.add(data);
            return;
        }

        if (!StringUtils.equals("1", data.getPerformance())
                && !StringUtils.equals("2", data.getPerformance())
                && !StringUtils.equals("3", data.getPerformance())
                && !StringUtils.equals("4", data.getPerformance())
                && !StringUtils.equals("5", data.getPerformance())) {
            data.setResultInfo("输入的绩效成绩不合法！");
            failList.add(data);
            return;
        }
        //判断是否同一个员工在同一年份已有绩效成绩
        List<EmployeePerformance> performanceList = performanceService.queryEmployeePerformance(data.getEmployeeId(), data.getYear());

        if (performanceList.size() > 0) {
            data.setResultInfo("该员工本年度已录入了绩效成绩，如有调整，请进行单独修改");
            failList.add(data);
            return;
        }

        EmployeePerformance employeePerformance = new EmployeePerformance();
        BeanUtils.copyProperties(data, employeePerformance);
        employeePerformance.setStatus("1");
        list.add(employeePerformance);

        if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }
    }

    /**
     * 最后触发
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        for (EmployeePerformance employeePerformance : list) {
            performanceService.save(employeePerformance);
        }
    }

}