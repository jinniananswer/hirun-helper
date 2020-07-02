package com.microtomato.hirun.modules.bss.customer.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.bss.customer.entity.dto.ReportQueryCondDTO;
import com.microtomato.hirun.modules.bss.customer.service.ICustomerServiceReportDomainService;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户代表报表 前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2020-04-20
 */
@RestController
@Slf4j
@RequestMapping("/api/bss.customer/customerServiceReport")
public class CustomerServiceReportController {

    @Autowired
    private ICustomerServiceReportDomainService customerServiceReportDomainService;

    @Autowired
    private IOrgService orgService;

    @GetMapping("/queryReport")
    @RestResult
    public Map<String, List> queryReport(ReportQueryCondDTO param) {
        return  customerServiceReportDomainService.queryReport(param);
    }

    @GetMapping("/queryAgentPlanAcutalReport")
    @RestResult
    public Map<String, List> queryAgentPlanAcutalReport(ReportQueryCondDTO param) {
        return  customerServiceReportDomainService.queryAgentPlanAcutalReport(param);
    }

    @GetMapping("/initShopData")
    @RestResult
    public List<Org> initShopData() {
        return orgService.listByType("4");
    }

    @GetMapping("/initCompanyData")
    @RestResult
    public List<Org> initCompanyData() {
        return orgService.listByType("2");
    }
}
