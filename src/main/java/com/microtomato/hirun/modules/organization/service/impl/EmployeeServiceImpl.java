package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.mapper.EmployeeMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2019-09-24
 */
@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<Employee> searchByNameMobileNo(String searchText) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>();
        wrapper.eq(Employee::getStatus, "0");
        wrapper.and(search -> search.like(Employee::getName, searchText).or().like(Employee::getMobileNo, searchText));
        return this.list(wrapper);
    }

    @Override
    /**
     * 查询员工档案
     */
    public IPage<Employee> queryEmployeeList(String name, String sex, String orgId, String mobileNo, String status, Page<Employee> employeePage) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();

        queryWrapper.like(StringUtils.isNotEmpty(name), "NAME", name);
        queryWrapper.eq(StringUtils.isNotEmpty(sex), "SEX", sex);
        queryWrapper.like(StringUtils.isNotEmpty(mobileNo), "MOBILE_NO", mobileNo);
        queryWrapper.eq(StringUtils.isNotEmpty(status), "STATUS", status);

        //IPage<Employee> iPage = employeeMapper.selectPage(pageEmployee, queryWrapper);
        IPage<Employee> iPage = employeeMapper.selectEmployeePage(employeePage, queryWrapper);

        return iPage;
    }

}
