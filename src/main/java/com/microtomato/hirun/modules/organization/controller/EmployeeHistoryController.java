package com.microtomato.hirun.modules.organization.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeHistoryDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeHistory;
import com.microtomato.hirun.modules.organization.service.IEmployeeHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2019-11-05
 */
@RestController
@Slf4j
@RequestMapping("/api/organization/employee-history")
public class EmployeeHistoryController {

    @Autowired
    private IEmployeeHistoryService employeeHistoryServiceImpl;

    @GetMapping("queryEmployeeHistory")
    @RestResult
    private List<EmployeeHistory> queryEmployeeHistory(Long employeeId){
        List<EmployeeHistory> list=employeeHistoryServiceImpl.queryHistories(employeeId);
        return list;
    }

    @PostMapping("updateEmployeeHistory")
    @RestResult
    private void updateEmployeeHistory(EmployeeHistory employeeHistory){
        employeeHistoryServiceImpl.updateById(employeeHistory);
    }

    @PostMapping("deleteEmployeeHistory")
    @RestResult
    private void deleteEmployeeHistory(EmployeeHistory employeeHistory){
        employeeHistory.setStatus("2");
        employeeHistoryServiceImpl.updateById(employeeHistory);
    }

}
