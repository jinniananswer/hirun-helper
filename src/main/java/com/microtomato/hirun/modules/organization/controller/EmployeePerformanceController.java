package com.microtomato.hirun.modules.organization.controller;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.harbour.excel.AbstractExcelHarbour;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeePerformanceImportDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeePerformanceInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeePerformance;
import com.microtomato.hirun.modules.organization.listener.EmployeePerformanceImplortListener;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.organization.service.IEmployeePerformanceService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2019-11-20
 */
@RestController
@Slf4j
@RequestMapping("api/organization/employee-performance")
public class EmployeePerformanceController extends AbstractExcelHarbour {

    @Autowired
    private IEmployeePerformanceService employeePerformanceServiceImpl;

    @PostMapping("importEmployeePerformance")
    @RestResult
    public void upload(@RequestParam("fileUpload") MultipartFile multipartFile) throws IOException {
        // 调用基类导入函数
        importExcel(multipartFile, EmployeePerformanceImportDTO.class, new EmployeePerformanceImplortListener(employeePerformanceServiceImpl));

    }

    @GetMapping("employeePerformanceList")
    @RestResult
    public IPage<EmployeePerformanceInfoDTO> queryEmployeePerformanceList(String name, String orgSet,String year,String performance,Integer page,Integer limit){
        Page <EmployeePerformanceInfoDTO> dtoPage=new Page<>(page,limit);
        return employeePerformanceServiceImpl.queryPerformanceList(name,orgSet,year,performance,dtoPage);
    }

    @PostMapping("addEmployeePerformance")
    @RestResult
    public void addEmployeePerformance(EmployeePerformance employeePerformance){
        employeePerformanceServiceImpl.addEmployeePerformance(employeePerformance);
    }

    @PostMapping("updateEmployeePerformance")
    @RestResult
    public boolean updateEmployeePerformance(EmployeePerformance employeePerformance){
       return employeePerformanceServiceImpl.updateEmployeePerformance(employeePerformance);
    }

    @GetMapping("/exportEmployee4ImportPerformance")
    @RestResult
    public void exportEmployee4ImportPerformance(String name, String orgSet,String year,String performance,HttpServletResponse response) throws IOException {
        List<EmployeePerformanceInfoDTO> list=employeePerformanceServiceImpl.queryPerformanceList(name,orgSet,year,performance);
        exportExcel(response, "员工列表", EmployeePerformanceInfoDTO.class, list, ExcelTypeEnum.XLSX);
    }
}
