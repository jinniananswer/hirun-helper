package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.domain.EmployeeBlackListDO;
import com.microtomato.hirun.modules.organization.entity.domain.EmployeeDO;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.*;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeBlacklist;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeWorkExperience;
import com.microtomato.hirun.modules.organization.exception.EmployeeException;
import com.microtomato.hirun.modules.organization.mapper.EmployeeMapper;
import com.microtomato.hirun.modules.organization.service.*;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import com.microtomato.hirun.modules.user.entity.domain.UserDO;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.service.IUserService;
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
    private IEmployeeWorkExperienceService employeeWorkExperienceService;

    @Autowired
    private IOrgService orgService;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IEmployeeBlacklistService employeeBlacklistService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeInfoDTO> searchEmployee(String searchText) {
        List<EmployeeInfoDTO> employees = employeeMapper.searchByNameMobileNo(searchText);

        if (ArrayUtils.isEmpty(employees)) {
            return null;
        }

        for (EmployeeInfoDTO employee : employees) {
            employee.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", employee.getJobRole()));
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, employee.getOrgId());
            employee.setOrgPath(orgDO.getCompanyLinePath());
        }
        return employees;
    }

    /**
     * 验证证件号码是否存在
     * @param identityNo
     */
    @Override
    public EmployeeDTO verifyIdentityNo(String createType, String identityNo) {
        boolean isBlack = this.employeeBlacklistService.exists(identityNo);
        if (isBlack) {
            throw new EmployeeException(EmployeeException.EmployeeExceptionEnum.IS_BLACK);
        }

        Employee employee = this.employeeService.queryByIdentityNo(identityNo);
        if (employee == null && (StringUtils.equals(EmployeeConst.CREATE_TYPE_NEW_ENTRY, createType) || StringUtils.equals(EmployeeConst.CREATE_TYPE_PRACTICE, createType))) {
            return null;
        }
        if (employee == null && !StringUtils.equals(EmployeeConst.CREATE_TYPE_NEW_ENTRY, createType) && !StringUtils.equals(EmployeeConst.CREATE_TYPE_PRACTICE, createType)) {
            throw new EmployeeException(EmployeeException.EmployeeExceptionEnum.CREATE_TYPE_ERROR);
        }
        String status = employee.getStatus();
        if (StringUtils.equals(EmployeeConst.STATUS_NORMAL, status)) {
            //创建员工时如果存在证件号码相同的正常员工，则报错
            throw new EmployeeException(EmployeeException.EmployeeExceptionEnum.IS_EXISTS, "证件号码", employee.getName(), employee.getMobileNo());
        } else {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            BeanUtils.copyProperties(employee, employeeDTO);
            return employeeDTO;
        }
    }

    @Override
    public void verifyMobileNo(String mobileNo) {
        User user = this.userService.queryByUsername(mobileNo);
        if (user != null) {
            if (!StringUtils.equals("0", user.getStatus())) {
                return;
            }
            Employee employee = this.employeeService.queryByUserId(user.getUserId());
            if (employee != null) {
                throw new EmployeeException(EmployeeException.EmployeeExceptionEnum.IS_EXISTS, "手机号码",employee.getName(), employee.getMobileNo());
            }
        }
    }

    /**
     * 装载员工档案信息
     * @param employeeId 员工ID
     * @param normal 是否在职员工
     * @return
     */
    @Override
    public EmployeeDTO load(Long employeeId, boolean normal) {
        EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employeeId);
        Employee employee = employeeDO.getEmployee();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        int age = employeeDO.getAge();
        int jobYear = employeeDO.getJobYear();

        employeeDTO.setJobYear(new Integer(jobYear));

        //获取员工岗位信息
        EmployeeJobRole jobRole = null;
        if (normal) {
            jobRole = this.employeeJobRoleService.queryValidMain(employeeId);
        } else {
            jobRole = this.employeeJobRoleService.queryLast(employeeId);
        }

        if (jobRole != null) {
            EmployeeJobRoleDTO jobRoleDTO = new EmployeeJobRoleDTO();
            BeanUtils.copyProperties(jobRole, jobRoleDTO);
            String jobRoleName = this.staticDataService.getCodeName("JOB_ROLE", jobRole.getJobRole());
            jobRoleDTO.setJobRoleName(jobRoleName);

            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, jobRole.getOrgId());
            jobRoleDTO.setOrgPath(orgDO.getCompanyLinePath());

            employeeDTO.setEmployeeJobRole(jobRoleDTO);
        }

        List<EmployeeWorkExperience> workExperiences = this.employeeWorkExperienceService.queryByEmployeeId(employeeId);
        if (ArrayUtils.isNotEmpty(workExperiences)) {
            List<EmployeeWorkExperienceDTO> workExperienceDTOS = new ArrayList<>();
            for (EmployeeWorkExperience workExperience : workExperiences) {
                EmployeeWorkExperienceDTO workExperienceDTO = new EmployeeWorkExperienceDTO();
                BeanUtils.copyProperties(workExperience, workExperienceDTO);
                workExperienceDTOS.add(workExperienceDTO);
            }
            employeeDTO.setEmployeeWorkExperiences(workExperienceDTOS);
        }

        return employeeDTO;
    }

    @Override
    public Double calculateDiscountRate(Long orgId, String jobRoleNature) {
        OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
        boolean isHomeDecoration = orgDO.isHomeDecoration();
        if (!isHomeDecoration) {
            return 0.0;
        } else {
            if (StringUtils.equals("2", jobRoleNature)) {
                return 1.0;
            } else if (StringUtils.equals("3", jobRoleNature)) {
                return 0.5;
            } else {
                return 0.0;
            }
        }
    }

    /**
     * 新员工入职
     * @param employeeDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @DS("ins")
    public void employeeEntry(EmployeeDTO employeeDTO) {
        if (employeeDTO.getEmployeeId() == null) {
            //没有employeeId，表示是新入职操作
            Employee employee = new Employee();
            BeanUtils.copyProperties(employeeDTO, employee);

            UserDO userDO = SpringContextUtils.getBean(UserDO.class);
            //默认创建用户
            Long userId = userDO.create(employee.getMobileNo(), null);
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
            EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employee);
            employeeDO.newEntry(jobRole, workExperiences);
        } else {
            //有employeeId，表示是复职操作
            EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employeeDTO.getEmployeeId());
            Employee employee = employeeDO.getEmployee();

            Long userId = employee.getUserId();
            UserDO userDO = SpringContextUtils.getBean(UserDO.class, userId);


        }

    }

    /**
     * 员工离职
     *
     * @param employeeDestroyInfoDTO
     */
    @Override
    @DS("ins")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean destroyEmployee(EmployeeDestroyInfoDTO employeeDestroyInfoDTO) {
        //拼装employee信息
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDestroyInfoDTO, employee);
        employee.setStatus("1");
        //拼装更新employeeJobRole信息
        LambdaUpdateWrapper<EmployeeJobRole> updateWrapper= Wrappers.lambdaUpdate();
        updateWrapper.eq(EmployeeJobRole::getEmployeeId,employee.getEmployeeId()).set(EmployeeJobRole::getEndDate,TimeUtils.getCurrentLocalDateTime());
        updateWrapper.apply("end_date > start_date and end_date > NOW() ");

        EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employee);
        employeeDO.destroy(updateWrapper);

        //todo 根据社保停买时间更新社保记录

        //todo 根据离职时间终止合同记录

        //如果为永不录用插入黑名单表
        if (StringUtils.equals(employeeDestroyInfoDTO.getIsBlackList(), "on")) {
            EmployeeBlacklist employeeBlacklist = new EmployeeBlacklist();
            BeanUtils.copyProperties(employeeDestroyInfoDTO, employeeBlacklist);
            employeeBlacklist.setStartTime(employeeDestroyInfoDTO.getDestroyDate());
            employeeBlacklist.setEndTime(TimeUtils.stringToLocalDateTime("2999-12-31 00:00:00", "yyyy-MM-dd HH:mm:ss"));

            EmployeeBlackListDO employeeBlackListDO = SpringContextUtils.getBean(EmployeeBlackListDO.class);
            employeeBlackListDO.addBlackList(employeeBlacklist);
        }

        //todo 调系统管理接口暂停操作员账号

        //根据调权限管理的返回结果决定返回boolean
        return true;
    }

    /**
     * 员工档案信息查询
     * @param employeeInfoDTO
     * @param page
     * @return
     */
    @Override
    public IPage<EmployeeInfoDTO> queryEmployeeList(EmployeeInfoDTO employeeInfoDTO, Page<EmployeeInfoDTO> page) {
        IPage<EmployeeInfoDTO> iPage=employeeService.queryEmployeeList(employeeInfoDTO,page);
        if(iPage==null){
            return null;
        }
        List<EmployeeInfoDTO> employeeDTOList=new ArrayList<EmployeeInfoDTO>();
        for(EmployeeInfoDTO employeeInfoDTOResult :iPage.getRecords()){
            employeeInfoDTOResult.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", employeeInfoDTOResult.getJobRole()));
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, employeeInfoDTOResult.getOrgId());
            employeeInfoDTOResult.setOrgPath(orgDO.getCompanyLinePath());
            employeeDTOList.add(employeeInfoDTOResult);


        }
        return iPage.setRecords(employeeDTOList);
    }


}
