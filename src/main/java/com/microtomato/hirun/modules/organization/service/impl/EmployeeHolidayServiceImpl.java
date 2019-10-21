package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeHoliday;
import com.microtomato.hirun.modules.organization.mapper.EmployeeHolidayMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeHolidayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2019-10-20
 */
@Slf4j
@Service
public class EmployeeHolidayServiceImpl extends ServiceImpl<EmployeeHolidayMapper, EmployeeHoliday> implements IEmployeeHolidayService {

    @Autowired EmployeeHolidayMapper employeeHolidayMapper;
    /**
     * 查询员工休假记录
     * @param employeeId
     * @param employeeHolidayPage
     * @return
     */
    @Override
    public IPage<EmployeeHoliday> selectEmployeeHolidayList(Long employeeId, Page<EmployeeHoliday> employeeHolidayPage) {
        QueryWrapper<EmployeeHoliday> queryWrapper=new QueryWrapper<EmployeeHoliday>();
        queryWrapper.eq(employeeId!=null,"employee_id",employeeId);
        queryWrapper.apply("start_time < end_time");
        queryWrapper.orderByDesc("create_time");
        IPage<EmployeeHoliday> iPage=employeeHolidayMapper.selectPage(employeeHolidayPage,queryWrapper);
        return iPage;
    }
}
