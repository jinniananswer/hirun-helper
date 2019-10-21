package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.organization.entity.domain.EmployeeBlackListDO;
import com.microtomato.hirun.modules.organization.entity.domain.EmployeeDO;
import com.microtomato.hirun.modules.organization.entity.dto.*;
import com.microtomato.hirun.modules.organization.entity.po.*;
import com.microtomato.hirun.modules.organization.mapper.EmployeeMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeDomainService;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import com.microtomato.hirun.modules.user.entity.domain.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工领域服务实现类
 * @author: jinnian
 * @create: 2019-10-08 14:54
 **/
@Slf4j
@Service
public class EmployeeDomainServiceImpl implements IEmployeeDomainService {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    @Autowired
    private IOrgService orgService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private EmployeeDO employeeDO;

    @Autowired
    private UserDO userDO;

    @Autowired
    private EmployeeBlackListDO employeeBlackListDO;

    @Autowired
    private EmployeeMapper employeeMapper;


    @Override
    public List<SearchEmployeeDTO> selectEmployee(String searchText) {
        List<SearchEmployeeDTO> employees = employeeMapper.searchByNameMobileNo(searchText);

        if (ArrayUtils.isEmpty(employees)) {
            return null;
        }

        for (SearchEmployeeDTO employee : employees) {
            employee.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", employee.getJobRole()));
        }
        return employees;
    }

    /**
     * 新员工入职
     *
     * @param employeeDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @DS("ins")
    public void employeeEntry(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        //默认创建用户
        Long userId = this.userDO.create(employee.getMobileNo(), null);
        employee.setUserId(userId);

        EmployeeJobRole jobRole = null;
        EmployeeJobRoleDTO jobRoleDTO = employeeDTO.getEmployeeJobRole();
        if (jobRoleDTO != null) {
            jobRole = new EmployeeJobRole();
            BeanUtils.copyProperties(jobRoleDTO, jobRole);
        }

        List<EmployeeWorkExperienceDTO> workExperienceDTOS = employeeDTO.getEmployeeWorkExperiences();
        List<EmployeeWorkExperience> workExperiences = new ArrayList<EmployeeWorkExperience>();
        if (ArrayUtils.isNotEmpty(workExperienceDTOS)) {
            for (EmployeeWorkExperienceDTO workExperienceDTO : workExperienceDTOS) {
                if (StringUtils.isNotBlank(workExperienceDTO.getContent())) {
                    //如果content不为空，才是有意义的内容
                    EmployeeWorkExperience workExperience = new EmployeeWorkExperience();
                    BeanUtils.copyProperties(workExperienceDTO, workExperience);
                    workExperiences.add(workExperience);
                }
            }
        }

        employeeDO.newEntry(employee, jobRole, workExperiences);
    }

    /**
     * 员工离职
     *
     * @param employeeDestroyInfoDTO
     */
    @Override
    public boolean destroyEmployee(EmployeeDestroyInfoDTO employeeDestroyInfoDTO) {
        boolean destroyResult = false;
        //修改employee状态
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDestroyInfoDTO, employee);
        employee.setStatus("1");
        employeeService.updateById(employee);

        //todo 根据社保停买时间更新社保记录

        //todo 根据离职时间终止合同记录

        //如果为永不录用插入黑名单表
        if (StringUtils.equals(employeeDestroyInfoDTO.getIsBlackList(), "on")) {
            EmployeeBlacklist employeeBlacklist = new EmployeeBlacklist();
            BeanUtils.copyProperties(employeeDestroyInfoDTO, employeeBlacklist);
            employeeBlacklist.setStartTime(employeeDestroyInfoDTO.getDestroyDate());
            employeeBlacklist.setEndTime(TimeUtils.stringToLocalDateTime("2999-12-31 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            employeeBlackListDO.newEntry(employeeBlacklist);
        }

        //todo 调系统管理接口暂停操作员账号

        //根据调权限管理的返回结果决定返回boolean
        return true;
    }

    /**
     * 员工档案信息查询
     * @param employeeQueryInfoDTO
     * @param page
     * @return
     */
    @Override
    public IPage<EmployeeQueryInfoDTO> queryEmployeeList(EmployeeQueryInfoDTO employeeQueryInfoDTO, Page<EmployeeQueryInfoDTO> page) {
        IPage<EmployeeQueryInfoDTO> iPage=employeeService.queryEmployeeList(employeeQueryInfoDTO,page);
        if(iPage==null){
            return null;
        }
        List<EmployeeQueryInfoDTO> employeeDTOList=new ArrayList<EmployeeQueryInfoDTO>();
        for(EmployeeQueryInfoDTO employeeQueryInfoDTOResult :iPage.getRecords()){
            employeeQueryInfoDTOResult.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", employeeQueryInfoDTOResult.getJobRole()));
            employeeDTOList.add(employeeQueryInfoDTOResult);
        }
        return iPage.setRecords(employeeDTOList);
    }


}