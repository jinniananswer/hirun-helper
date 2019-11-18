package com.microtomato.hirun.modules.organization.controller;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.harbour.excel.AbstractExcelHarbour;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.organization.entity.dto.*;
import com.microtomato.hirun.modules.organization.service.IEmployeeDomainService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2019-09-24
 */
@RestController
@Slf4j
@RequestMapping("api/organization/employee")
public class EmployeeController extends AbstractExcelHarbour  {

    @Autowired
    private IEmployeeService employeeServiceImpl;

    @Autowired
    private IEmployeeDomainService employeeDomainServiceImpl;

    @PostMapping("/create")
    @RestResult
    public Map create(EmployeeDTO employee) {
        employeeDomainServiceImpl.employeeEntry(employee);
        return new HashMap();
    }

    @GetMapping("/searchEmployee")
    @RestResult
    public List<EmployeeInfoDTO> searchEmployee(String searchText) {
        return employeeDomainServiceImpl.searchEmployee(searchText);
    }

    @RequestMapping("/verifyIdentityNo")
    @RestResult
    public EmployeeDTO verifyIdentityNo(String createType, String identityNo, Long employeeId, String operType) {
        return this.employeeDomainServiceImpl.verifyIdentityNo(createType, identityNo, employeeId, operType);
    }

    @RequestMapping("/verifyMobileNo")
    @RestResult
    public void verifyMobileNo(String mobileNo, String operType, Long employeeId) {
        this.employeeDomainServiceImpl.verifyMobileNo(mobileNo, operType, employeeId);
    }

    @RequestMapping("/loadAbnormal")
    @RestResult
    public EmployeeDTO loadAbnormal(Long employeeId) {
        return this.employeeDomainServiceImpl.load(employeeId, false);
    }

    @RequestMapping("/calculateDiscountRate")
    @RestResult
    public Map calculateDiscountRate(Long orgId, String jobRoleNature) {
        Double discountRate = this.employeeDomainServiceImpl.calculateDiscountRate(orgId, jobRoleNature);
        Map result = new HashMap<>();
        result.put("discountRate", discountRate);
        return result;
    }

    @GetMapping("/queryEmployeeList4Page")
    @RestResult
    public IPage<EmployeeInfoDTO> queryEmployeeList4Page(EmployeeInfoDTO employeeInfoDTO, Integer page, Integer limit) {
        Page<EmployeeInfoDTO> employeeInfoDTOPage = new Page<>(page, limit);
        IPage<EmployeeInfoDTO> employeeList = employeeDomainServiceImpl.queryEmployeeList4Page(employeeInfoDTO,employeeInfoDTOPage);
        return employeeList;
    }

    @PostMapping("/destroyEmployee")
    @RestResult
    public boolean destroyEmployee(EmployeeDestroyInfoDTO employeeDestroyInfoDTO) {
        boolean destroyRusult=employeeDomainServiceImpl.destroyEmployee(employeeDestroyInfoDTO);
        return destroyRusult;
    }

    @RequestMapping("/loadEmployeeArchive")
    @RestResult
    public EmployeeArchiveDTO loadEmployeeArchive(Long employeeId) {
        if (employeeId == null) {
            UserContext userContext = WebContextUtils.getUserContext();
            employeeId = userContext.getEmployeeId();
        }

        return this.employeeDomainServiceImpl.loadMyArchive(employeeId);
    }

    /**
     * 测试后期删除
     */
    @GetMapping("/selectEmployeePageExample")
    @RestResult
    public IPage<EmployeeExampleDTO> selectEmployeePageExample(EmployeeExampleDTO stevenDTO) {
        return employeeServiceImpl.selectEmployeePageExample(stevenDTO.getName(), stevenDTO.getOrgId(), stevenDTO.getJobRole());
    }

    @GetMapping("/queryEmployeeList4Export")
    @RestResult
    public void queryEmployeeList4Export(EmployeeInfoDTO employeeInfoDTO, HttpServletResponse response) throws IOException {
        List<EmployeeInfoDTO> list=employeeDomainServiceImpl.queryEmployeeList(employeeInfoDTO);
        exportExcel(response, "users", EmployeeInfoDTO.class, list, ExcelTypeEnum.XLSX);
    }
}
