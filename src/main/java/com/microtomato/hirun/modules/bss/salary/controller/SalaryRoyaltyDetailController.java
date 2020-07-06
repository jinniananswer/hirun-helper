package com.microtomato.hirun.modules.bss.salary.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.salary.entity.dto.*;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryRoyaltyDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 工资明细控制器
 * @author: jinnian
 * @create: 2020-06-13 01:18
 **/
@RestController
@RequestMapping("/api/bss.salary/salary-royalty-detail")
public class SalaryRoyaltyDetailController {

    @Autowired
    private ISalaryRoyaltyDetailService salaryRoyaltyDetailService;

    @GetMapping("/queryRoyaltyByOrderId")
    @RestResult
    public SalaryRoyaltyDetailDTO querySalary(Long orderId) {
        return this.salaryRoyaltyDetailService.queryByOrderId(orderId);
    }

    @PostMapping("/saveDesignRoyaltyDetails")
    @RestResult
    public void saveDesignRoyaltyDetails(@RequestBody List<DesignRoyaltyDetailDTO> designRoyaltyDetails) {
        this.salaryRoyaltyDetailService.saveDesignRoyaltyDetails(designRoyaltyDetails);
    }

    @PostMapping("/auditDesignRoyaltyDetails")
    @RestResult
    public void auditDesignRoyaltyDetails(@RequestBody List<DesignRoyaltyDetailDTO> designRoyaltyDetails) {
        this.salaryRoyaltyDetailService.auditDesignRoyaltyDetails(designRoyaltyDetails);
    }

    @GetMapping("/afterCreateDesignDetail")
    @RestResult
    public DesignRoyaltyDetailDTO afterCreateDesignDetail(DesignRoyaltyDetailDTO detail) {
        return this.salaryRoyaltyDetailService.afterCreateDesignDetail(detail);
    }

    @GetMapping("/afterCreateProjectDetail")
    @RestResult
    public ProjectRoyaltyDetailDTO afterCreateProjectDetail(ProjectRoyaltyDetailDTO detail) {
        return this.salaryRoyaltyDetailService.afterCreateProjectDetail(detail);
    }

    @PostMapping("/saveProjectRoyaltyDetails")
    @RestResult
    public void saveProjectRoyaltyDetails(@RequestBody List<ProjectRoyaltyDetailDTO> projectRoyaltyDetails) {
        this.salaryRoyaltyDetailService.saveProjectRoyaltyDetails(projectRoyaltyDetails);
    }

    @PostMapping("/auditProjectRoyaltyDetails")
    @RestResult
    public void auditProjectRoyaltyDetails(@RequestBody List<ProjectRoyaltyDetailDTO> projectRoyaltyDetails) {
        this.salaryRoyaltyDetailService.auditProjectRoyaltyDetails(projectRoyaltyDetails);
    }

    @GetMapping("/queryAuditDesignRoyaltyDetails")
    @RestResult
    public IPage<DesignRoyaltyDetailDTO> queryAuditDesignRoyaltyDetails(QueryRoyaltyDetailDTO condition) {
        return this.salaryRoyaltyDetailService.queryAuditDesignRoyaltyDetails(condition);
    }

    @GetMapping("/queryAuditProjectRoyaltyDetails")
    @RestResult
    public IPage<ProjectRoyaltyDetailDTO> queryAuditProjectRoyaltyDetails(QueryRoyaltyDetailDTO condition) {
        return this.salaryRoyaltyDetailService.queryAuditProjectRoyaltyDetails(condition);
    }

    @PostMapping("/auditDesignRoyaltyPass")
    @RestResult
    public void auditDesignRoyaltyPass(@RequestBody List<DesignRoyaltyDetailDTO> designRoyaltyDetails) {
        this.salaryRoyaltyDetailService.auditDesignRoyaltyPass(designRoyaltyDetails);
    }

    @PostMapping("/auditDesignRoyaltyNo")
    @RestResult
    public void auditDesignRoyaltyNo(@RequestBody List<DesignRoyaltyDetailDTO> designRoyaltyDetails) {
        this.salaryRoyaltyDetailService.auditDesignRoyaltyNo(designRoyaltyDetails);
    }

    @PostMapping("/auditProjectRoyaltyPass")
    @RestResult
    public void auditProjectRoyaltyPass(@RequestBody List<ProjectRoyaltyDetailDTO> projectRoyaltyDetails) {
        this.salaryRoyaltyDetailService.auditProjectRoyaltyPass(projectRoyaltyDetails);
    }

    @PostMapping("/auditProjectRoyaltyNo")
    @RestResult
    public void auditProjectRoyaltyNo(@RequestBody List<ProjectRoyaltyDetailDTO> projectRoyaltyDetails) {
        this.salaryRoyaltyDetailService.auditProjectRoyaltyNo(projectRoyaltyDetails);
    }
}
