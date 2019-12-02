package com.microtomato.hirun.modules.organization.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeTransDetailDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeTransDetail;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.organization.service.IEmployeeTransDetailService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2019-10-29
 */
@RestController
@Slf4j
@RequestMapping("/api/organization/employee-trans-detail")
public class EmployeeTransDetailController {

    @Autowired
    private IEmployeeTransDetailService employeeTransDetailServiceImpl;

    @Autowired
    private IEmployeeService employeeService;

    @PostMapping("/queryDetail4Confirm")
    @RestResult
    public EmployeeTransDetailDTO queryDetail4Confirm(Long relPendingId){
        EmployeeTransDetail transDetail=employeeTransDetailServiceImpl.queryPendingDetailById(relPendingId);
        EmployeeTransDetailDTO transDetailDTO = new EmployeeTransDetailDTO();
        BeanUtils.copyProperties(transDetail, transDetailDTO);

        transDetailDTO.setSourceParentEmployeeName(employeeService.getEmployeeNameEmployeeId(transDetailDTO.getSourceParentEmployeeId()));
        OrgDO sourceOrgDO = SpringContextUtils.getBean(OrgDO.class, transDetailDTO.getSourceOrgId());
        transDetailDTO.setSourceOrgPath(sourceOrgDO.getCompanyLinePath());

        return transDetailDTO;
    }

    @PostMapping("/confirmReturnDetail")
    @RestResult
    public boolean confirmReturnDetail(EmployeeTransDetail transDetail){
        return employeeTransDetailServiceImpl.confirmReturnDetail(transDetail);
    }

}
