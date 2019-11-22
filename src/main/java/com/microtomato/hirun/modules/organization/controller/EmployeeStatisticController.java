package com.microtomato.hirun.modules.organization.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.data.PieChart;
import com.microtomato.hirun.framework.data.PieData;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeePieStatisticDTO;
import com.microtomato.hirun.modules.organization.service.IEmployeeStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工图表统计控制器
 * @author: jinnian
 * @create: 2019-11-21 16:45
 **/
@RestController
@Slf4j
@RequestMapping("api/organization/employee-statistic")
public class EmployeeStatisticController {

    @Autowired
    private IEmployeeStatisticService employeeStatisticService;

    @PostMapping("/countBySex")
    @RestResult
    public PieChart countBySex() {
        List<EmployeePieStatisticDTO> sexStatistics = this.employeeStatisticService.countBySex();

        if (ArrayUtils.isEmpty(sexStatistics)) {
            return null;
        }

        PieChart pie = new PieChart();
        pie.setTitle("员工数量分布（按性别）");
        pie.setItemTitle("性别");

        List<PieData> datas = this.getPieData(sexStatistics);
        pie.setDatas(datas);

        return pie;
    }

    @PostMapping("/countByAge")
    @RestResult
    public PieChart countByAge() {
        List<EmployeePieStatisticDTO> ageStatistics = this.employeeStatisticService.countByAge();

        if (ArrayUtils.isEmpty(ageStatistics)) {
            return null;
        }

        PieChart pie = new PieChart();
        pie.setTitle("员工数量分布（按年龄段）");
        pie.setItemTitle("年龄段");

        List<PieData> datas = this.getPieData(ageStatistics);
        pie.setDatas(datas);

        return pie;
    }

    @PostMapping("/countByJobRoleNature")
    @RestResult
    public PieChart countByJobRoleNature() {
        List<EmployeePieStatisticDTO> jobRoleNatureStatistics = this.employeeStatisticService.countByJobRoleNature();

        if (ArrayUtils.isEmpty(jobRoleNatureStatistics)) {
            return null;
        }

        PieChart pie = new PieChart();
        pie.setTitle("员工数量分布（按岗位性质）");
        pie.setItemTitle("岗位性质");

        List<PieData> datas = this.getPieData(jobRoleNatureStatistics);
        pie.setDatas(datas);

        return pie;
    }

    @PostMapping("/countByCompanyAge")
    @RestResult
    public PieChart countByCompanyAge() {
        List<EmployeePieStatisticDTO> companyAgeStatistics = this.employeeStatisticService.countByCompanyAge();

        if (ArrayUtils.isEmpty(companyAgeStatistics)) {
            return null;
        }

        PieChart pie = new PieChart();
        pie.setTitle("员工数量分布（按司龄）");
        pie.setItemTitle("司龄");

        List<PieData> datas = this.getPieData(companyAgeStatistics);
        pie.setDatas(datas);

        return pie;
    }

    @PostMapping("/countByEducationLevel")
    @RestResult
    public PieChart countByEducationLevel() {
        List<EmployeePieStatisticDTO> educationLevelStatistics = this.employeeStatisticService.countByEducationLevel();

        if (ArrayUtils.isEmpty(educationLevelStatistics)) {
            return null;
        }

        PieChart pie = new PieChart();
        pie.setTitle("员工数量分布（按学历）");
        pie.setItemTitle("学历");

        List<PieData> datas = this.getPieData(educationLevelStatistics);
        pie.setDatas(datas);

        return pie;
    }

    @PostMapping("/countByType")
    @RestResult
    public PieChart countByType() {
        List<EmployeePieStatisticDTO> typeStatistics = this.employeeStatisticService.countByType();

        if (ArrayUtils.isEmpty(typeStatistics)) {
            return null;
        }

        PieChart pie = new PieChart();
        pie.setTitle("员工数量分布（按类型）");
        pie.setItemTitle("类型");

        List<PieData> datas = this.getPieData(typeStatistics);
        pie.setDatas(datas);

        return pie;
    }

    private List<PieData> getPieData(List<EmployeePieStatisticDTO> pieStatistics) {
        List<PieData> datas = new ArrayList<>();
        if (ArrayUtils.isEmpty(pieStatistics)) {
            return datas;
        }
        for (EmployeePieStatisticDTO statistic : pieStatistics) {
            PieData data = new PieData();
            data.setName(statistic.getName());
            data.setValue(String.valueOf(statistic.getNum()));
            datas.add(data);
        }
        return datas;
    }
}
