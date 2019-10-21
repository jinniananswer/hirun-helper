package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeDestroyInfoDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQueryInfoDTO;
import com.microtomato.hirun.modules.organization.entity.dto.SearchEmployeeDTO;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工领域服务接口
 * @author: jinnian
 * @create: 2019-10-08 14:42
 **/
public interface IEmployeeDomainService {

    List<SearchEmployeeDTO> selectEmployee(String searchText);

    void employeeEntry(EmployeeDTO employeeDTO);

    boolean destroyEmployee(EmployeeDestroyInfoDTO employeeDestroyInfoDTO);

    IPage<EmployeeQueryInfoDTO> queryEmployeeList(EmployeeQueryInfoDTO employeeQueryInfoDTO, Page<EmployeeQueryInfoDTO> page);
}