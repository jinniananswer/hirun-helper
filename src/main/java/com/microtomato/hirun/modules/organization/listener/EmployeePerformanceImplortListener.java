package com.microtomato.hirun.modules.organization.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.microtomato.hirun.framework.harbour.excel.ExcelEventListener;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.ValidationUtils;
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
public class EmployeePerformanceImplortListener extends ExcelEventListener<EmployeePerformanceImportDTO> {


    private static final int BATCH_COUNT = 5;

    private List<EmployeePerformance> list = new ArrayList<>(BATCH_COUNT);
    private IEmployeePerformanceService performanceService = SpringContextUtils.getBean(IEmployeePerformanceService.class);


    /**
     * 每读取一行触发一次该函数
     *
     * @param data    excel 读过来的一行数据
     * @param context 上下文信息
     */
    @Override
    public void invoke(EmployeePerformanceImportDTO data, AnalysisContext context) {

        String checkResult = ValidationUtils.jsr303Check(data);
        if (null != checkResult) {
            addErrData(context, data, checkResult);
            return;
        }

        //判断是否同一个员工在同一年份已有绩效成绩
        List<EmployeePerformance> performanceList = performanceService.queryEmployeePerformance(data.getEmployeeId(), data.getYear());

        if (performanceList.size() > 0) {
            data.setResultInfo("该员工本年度已录入了绩效成绩，如有调整，请进行单独修改");
            addErrData(context, data, "该员工本年度已录入了绩效成绩，如有调整，请进行单独修改(新的)");
            return;
        }

        EmployeePerformance employeePerformance = new EmployeePerformance();
        BeanUtils.copyProperties(data, employeePerformance);
        employeePerformance.setStatus("1");
        try {
            performanceService.save(employeePerformance);
        } catch (Exception e) {
            try {
                addErrData(context, data);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}