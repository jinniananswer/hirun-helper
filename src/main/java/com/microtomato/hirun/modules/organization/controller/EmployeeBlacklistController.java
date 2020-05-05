package com.microtomato.hirun.modules.organization.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeBlacklist;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.organization.service.IEmployeeBlacklistService;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2019-10-14
 */
@RestController
@Slf4j
@RequestMapping("api/organization/employee-blacklist")
public class EmployeeBlacklistController {

    @Autowired
    private IEmployeeBlacklistService employeeBlacklistServiceImpl;

    @GetMapping("queryEmployeeBlackList")
    @RestResult
    private List<EmployeeBlacklist> queryEmployeeBlackList(String employeeName,String identityNo){
        List<EmployeeBlacklist> list=employeeBlacklistServiceImpl.queryEmployeeBlackList(employeeName,identityNo);
        return list;
    }

    @PostMapping("deleteEmployeeBlackList")
    @RestResult
    private void deleteEmployeeBlackList(Long id,String remark){
        employeeBlacklistServiceImpl.deleteBlackList(id,remark);
    }

}
