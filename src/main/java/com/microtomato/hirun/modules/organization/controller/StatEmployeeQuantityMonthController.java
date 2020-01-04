package com.microtomato.hirun.modules.organization.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQuantityStatDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.organization.service.IStatEmployeeQuantityMonthService;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2019-12-17
 */
@RestController
@Slf4j
@RequestMapping("/api/organization/stat-employee-quantity-month")
public class StatEmployeeQuantityMonthController {

    @Autowired
    private IStatEmployeeQuantityMonthService statEmployeeQuantityMonthServiceImpl;

    @GetMapping("/queryEmployeeQuantityStat")
    @RestResult
    public List<EmployeeQuantityStatDTO> queryEmployeeQuantityStat(String year, Long orgId){
        return statEmployeeQuantityMonthServiceImpl.queryEmployeeQuantityStat(year,orgId);
    }

    @GetMapping("/queryEmployeeTrendsStat")
    @RestResult
    public List<Map<String,String>> queryEmployeeTrendsStat(String queryTime, Long orgId,String orgNature){
        return statEmployeeQuantityMonthServiceImpl.queryEmployeeTrendsStat(queryTime,orgId,orgNature);
    }

    @GetMapping("/queryEmployeeCompanyStat")
    @RestResult
    public List<Map<String,String>> queryEmployeeCompanyStat(String queryTime,String orgNature,String jobRole){
        return statEmployeeQuantityMonthServiceImpl.queryEmployeeCompanyStat(queryTime,orgNature,jobRole);
    }

    @GetMapping("/busiCountByOrgNatureAndJobRole")
    @RestResult
    public List<Map<String,String>> busiCountByOrgNatureAndJobRole(String queryTime,Long orgId,String orgNature){
        return statEmployeeQuantityMonthServiceImpl.busiCountByOrgNatureAndJobRole(queryTime,orgId,orgNature);
    }

    @GetMapping("/busiAndAllCountTrend")
    @RestResult
    public List<Map<String,String>> busiAndAllCountTrend(String queryTime,Long orgId){
        return statEmployeeQuantityMonthServiceImpl.busiAndAllCountTrend(queryTime,orgId);
    }
}
