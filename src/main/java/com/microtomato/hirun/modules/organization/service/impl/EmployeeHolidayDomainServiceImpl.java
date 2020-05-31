package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.domain.EmployeeHolidayDO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.*;
import com.microtomato.hirun.modules.organization.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @program: hirun-helper
 * @description: 员工休假领域服务实现类
 **/
@Slf4j
@Service
public class EmployeeHolidayDomainServiceImpl implements IEmployeeHolidayDomainService {

    @Autowired
    private IEmployeeHolidayService employeeHolidayService;


    @Autowired
    private EmployeeHolidayDO employeeHolidayDO;

    @Autowired
    private IStatEmployeeTransitionService transitionService;

    @Autowired
    IEmployeeService employeeService;

    @Override
    public IPage<EmployeeHoliday> selectEmployeeHolidayList(Long employeeId, Page<EmployeeHoliday> employeeHolidayPage) {
        IPage<EmployeeHoliday> iPage = employeeHolidayService.selectEmployeeHolidayList(employeeId, employeeHolidayPage);
        return iPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean addEmployeeHoliday(EmployeeHoliday employeeHoliday) {
        boolean result = false;
        String jobRole = "9999";
        String jobRoleNature = "0";
        String jobGrade = "0";
        String orgNature = "0";
        EmployeeInfoDTO employeeInfoDTO = employeeService.queryEmployeeInfoByEmployeeId(employeeHoliday.getEmployeeId());
        if (employeeInfoDTO != null) {
            if (StringUtils.isNotBlank(employeeInfoDTO.getJobRole())) {
                jobRole = employeeInfoDTO.getJobRole();
            }
            if (StringUtils.isNotBlank(employeeInfoDTO.getJobRoleNature())) {
                jobRoleNature = employeeInfoDTO.getJobRoleNature();
            }
            if (StringUtils.isNotBlank(employeeInfoDTO.getJobGrade())) {
                jobGrade = employeeInfoDTO.getJobGrade();
            }
            if (StringUtils.isNotBlank(employeeInfoDTO.getOrgNature())) {
                orgNature = employeeInfoDTO.getOrgNature();
            }
        }

        employeeHoliday.setJobRole(jobRole);
        employeeHoliday.setJobRoleNature(jobRoleNature);
        employeeHoliday.setJobGrade(jobGrade);
        employeeHoliday.setOrgNature(orgNature);
        employeeHoliday.setOrgId(employeeInfoDTO.getOrgId());

        Integer newResult = employeeHolidayDO.add(employeeHoliday);
        if (newResult != null || newResult != 0) {
            result = true;
        }
        EmployeeInfoDTO infoDTO = employeeService.queryEmployeeInfoByEmployeeId(employeeHoliday.getEmployeeId());
        //新增一条休假记录，则新增一条异动信息
        if (infoDTO != null) {
            transitionService.addEmployeeHolidayTransition(infoDTO.getOrgId(), infoDTO.getEmployeeId(), employeeHoliday.getStartTime().toLocalDate());
        }
        return result;
    }

    /**
     * 更新休假记录
     *
     * @param employeeHoliday
     * @return
     */
    @Override
    public boolean updateEmployeeHoliday(EmployeeHoliday employeeHoliday) {
        Integer updateResult = employeeHolidayDO.modify(employeeHoliday);
        if (updateResult != null || updateResult != 0) {
            return true;
        }
        return false;
    }


    @Override
    public boolean deleteEmployeeHoliday(EmployeeHoliday employeeHoliday) {
        employeeHoliday.setEndTime(employeeHoliday.getStartTime());
        Integer deleteResult = employeeHolidayDO.delete(employeeHoliday);
        if (deleteResult != null || deleteResult != 0) {
            return true;
        }
        return false;
    }
}
