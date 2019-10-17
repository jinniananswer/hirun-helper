package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.po.Employee;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liuhui
 * @since 2019-09-24
 */
public interface IEmployeeService extends IService<Employee> {

    List<Employee> searchByNameMobileNo(String searchText);

    IPage<Employee> queryEmployeeList(String name, String sex, String orgId, String mobile, String status, Integer page, Integer limit);

}
