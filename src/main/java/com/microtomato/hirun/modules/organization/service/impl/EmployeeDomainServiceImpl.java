package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.cases.AlreadyExistException;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.domain.EmployeeBlackListDO;
import com.microtomato.hirun.modules.organization.entity.domain.EmployeeDO;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.*;
import com.microtomato.hirun.modules.organization.entity.po.*;
import com.microtomato.hirun.modules.organization.exception.EmployeeException;
import com.microtomato.hirun.modules.organization.mapper.EmployeeMapper;
import com.microtomato.hirun.modules.organization.service.*;
import com.microtomato.hirun.modules.system.entity.domain.AddressDO;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import com.microtomato.hirun.modules.user.entity.consts.UserConst;
import com.microtomato.hirun.modules.user.entity.domain.UserDO;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.service.IUserRoleService;
import com.microtomato.hirun.modules.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private IUserRoleService userRoleService;

    @Autowired
    private IEmployeeBlacklistService employeeBlacklistService;

    @Autowired
    private IEmployeeHistoryService employeeHistoryService;

    @Autowired
    private IEmployeeChildrenService employeeChildrenService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private IEmployeeContractService employeeContractService;

    @Autowired
    private IHrPendingService hrPendingService;

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
     *
     * @param identityNo
     */
    @Override
    public EmployeeDTO verifyIdentityNo(String createType, String identityNo, Long employeeId, String operType) {
        boolean isBlack = this.employeeBlacklistService.exists(identityNo);
        if (isBlack) {
            throw new EmployeeException(EmployeeException.EmployeeExceptionEnum.IS_BLACK);
        }

        Employee employee = this.employeeService.queryByIdentityNo(identityNo);

        if (employee == null && (StringUtils.equals(EmployeeConst.CREATE_TYPE_NEW_ENTRY, createType) || StringUtils.isBlank(createType))) {
            return null;
        }
        if (employee == null && !StringUtils.equals(EmployeeConst.CREATE_TYPE_NEW_ENTRY, createType)) {
            throw new EmployeeException(EmployeeException.EmployeeExceptionEnum.CREATE_TYPE_ERROR);
        }

        if ((StringUtils.equals(EmployeeConst.CREATE_TYPE_NEW_ENTRY, createType) && StringUtils.equals(operType, EmployeeConst.OPER_TYPE_CREATE))) {
            //创建员工时如果存在证件号码相同的员工，则报错
            throw new EmployeeException(EmployeeException.EmployeeExceptionEnum.IS_EXISTS, "证件号码", employee.getName(), employee.getMobileNo());
        } else if (StringUtils.equals(operType, EmployeeConst.OPER_TYPE_EDIT) && employeeId != null && !employeeId.equals(employee.getEmployeeId())) {
            throw new EmployeeException(EmployeeException.EmployeeExceptionEnum.IS_EXISTS, "证件号码", employee.getName(), employee.getMobileNo());
        } else {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            BeanUtils.copyProperties(employee, employeeDTO);
            return employeeDTO;
        }
    }

    @Override
    public void verifyMobileNo(String mobileNo, String operType, Long employeeId) {
        User user = this.userService.queryByUsername(mobileNo);
        if (user != null) {
            if (!StringUtils.equals(EmployeeConst.STATUS_NORMAL, user.getStatus())) {
                return;
            }
            Employee employee = this.employeeService.queryByUserId(user.getUserId());
            if (StringUtils.equals(EmployeeConst.OPER_TYPE_EDIT, operType) && employeeId != null && !employeeId.equals(employee.getEmployeeId())) {
                throw new EmployeeException(EmployeeException.EmployeeExceptionEnum.IS_EXISTS, "手机号码", employee.getName(), employee.getMobileNo());
            } else if (StringUtils.equals(EmployeeConst.OPER_TYPE_EDIT, operType)) {
                return;
            }

            if (employee != null) {
                throw new EmployeeException(EmployeeException.EmployeeExceptionEnum.IS_EXISTS, "手机号码", employee.getName(), employee.getMobileNo());
            }
        }
    }

    /**
     * 装载员工档案信息
     *
     * @param employeeId 员工ID
     * @return
     */
    @Override
    public EmployeeDTO load(Long employeeId) {
        EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employeeId);
        Employee employee = employeeDO.getEmployee();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        int jobYear = employeeDO.getJobYear();

        employeeDTO.setJobYear(new Integer(jobYear));

        AddressDO addressDO = SpringContextUtils.getBean(AddressDO.class);
        employeeDTO.setNatives(addressDO.getFullName(employee.getNativeRegion()));
        employeeDTO.setHome(addressDO.getFullName(employee.getHomeRegion()));

        //获取员工岗位信息
        EmployeeJobRole jobRole = null;
        String status = employee.getStatus();
        if (StringUtils.equals(EmployeeConst.STATUS_NORMAL, status)) {
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

            Long parentEmployeeId = jobRole.getParentEmployeeId();
            if (parentEmployeeId != null) {
                Employee parentEmployee = this.employeeService.getById(parentEmployeeId);
                if (parentEmployee != null) {
                    jobRoleDTO.setParentEmployeeName(parentEmployee.getName());
                }
            }

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

        List<EmployeeChildren> children = this.employeeChildrenService.queryByEmployeeId(employeeId);
        if (ArrayUtils.isNotEmpty(children)) {
            List<EmployeeChildrenDTO> childrenDTOS = new ArrayList<>();
            for (EmployeeChildren child : children) {
                EmployeeChildrenDTO childDTO = new EmployeeChildrenDTO();
                BeanUtils.copyProperties(child, childDTO);
                childrenDTOS.add(childDTO);
            }
            employeeDTO.setChildren(childrenDTOS);
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
     *
     * @param employeeDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @DS("ins")
    public void employeeEntry(EmployeeDTO employeeDTO) {

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

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

        List<EmployeeChildrenDTO> childrenDTOS = employeeDTO.getChildren();
        List<EmployeeChildren> children = new ArrayList<>();

        if (ArrayUtils.isNotEmpty(childrenDTOS)) {
            for (EmployeeChildrenDTO childrenDTO : childrenDTOS) {
                if (StringUtils.isNotBlank(childrenDTO.getName())) {
                    EmployeeChildren child = new EmployeeChildren();
                    BeanUtils.copyProperties(childrenDTO, child);
                    children.add(child);
                }
            }
        }

        if (employeeDTO.getEmployeeId() == null) {
            //没有employeeId，表示是新入职操作

            UserDO userDO = SpringContextUtils.getBean(UserDO.class);
            //默认创建用户
            Long userId = userDO.create(employee.getMobileNo(), null);
            employee.setUserId(userId);

            EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employee);
            employeeDO.newEntry(jobRole, workExperiences, children);

            //分配默认权限
            this.userRoleService.createRole(userId, jobRole.getOrgId(), jobRole.getJobRole());
        } else {
            EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employeeDTO.getEmployeeId());
            Employee oldEmployee = employeeDO.getEmployee();

            Long userId = oldEmployee.getUserId();
            employee.setUserId(userId);

            UserDO userDO = SpringContextUtils.getBean(UserDO.class, userId);

            String createType = employeeDTO.getCreateType();
            if (StringUtils.equals(EmployeeConst.CREATE_TYPE_REHIRE, createType)) {
                userDO.modify(employeeDTO.getMobileNo(), UserConst.INIT_PASSWORD, UserConst.STATUS_NORMAL);
                employeeDO.rehire(employee, jobRole, workExperiences, children);
                //分配默认权限
                this.userRoleService.createRole(userId, jobRole.getOrgId(), jobRole.getJobRole());
            } else if (StringUtils.equals(createType, EmployeeConst.CREATE_TYPE_REHELLORING)) {
                userDO.modify(employeeDTO.getMobileNo(), UserConst.INIT_PASSWORD, UserConst.STATUS_NORMAL);
                employeeDO.rehelloring(employee, jobRole, workExperiences, children);
                //分配默认权限
                this.userRoleService.createRole(userId, jobRole.getOrgId(), jobRole.getJobRole());
            } else {
                userDO.modify(employeeDTO.getMobileNo(), null, null);
                employeeDO.modify(employee, jobRole, workExperiences, children);
            }
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

        List<HrPending> hrPendingList=hrPendingService.queryPendingByExecuteId(employeeDestroyInfoDTO.getEmployeeId());

        if (hrPendingList.size() > 0) {
            throw new AlreadyExistException(" 该员工下存在未处理的待办任务，请将待办任务转移之后再办理离职！.", ErrorKind.ALREADY_EXIST.getCode());
        }

        UserContext userContext=WebContextUtils.getUserContext();
        Long loginUserId=userContext.getUserId();

        //注销employee信息
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDestroyInfoDTO, employee);

        EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employee);
        employeeDO.destroy(employeeDestroyInfoDTO.getDestroyDate());

        //根据离职时间终止合同记录
        employeeContractService.stopEmployeeContract(employeeDestroyInfoDTO.getEmployeeId(),employeeDestroyInfoDTO.getDestroyDate(),loginUserId);

        //终止暂停操作员账号
        Employee userEmployee = employeeService.getById(employeeDestroyInfoDTO.getEmployeeId());
        Long userId = userEmployee.getUserId();
        UserDO userDO = SpringContextUtils.getBean(UserDO.class, userId);
        userDO.destory(employeeDestroyInfoDTO.getDestroyDate());

        //如果为永不录用插入黑名单表
        if (StringUtils.equals(employeeDestroyInfoDTO.getIsBlackList(), "on")) {
            EmployeeBlacklist employeeBlacklist = new EmployeeBlacklist();
            BeanUtils.copyProperties(employeeDestroyInfoDTO, employeeBlacklist);
            employeeBlacklist.setStartTime(employeeDestroyInfoDTO.getDestroyDate());
            employeeBlacklist.setEndTime(TimeUtils.getForeverTime());

            EmployeeBlackListDO employeeBlackListDO = SpringContextUtils.getBean(EmployeeBlackListDO.class);
            employeeBlackListDO.addBlackList(employeeBlacklist);
        }

        //变更直属下级的上级员工
        if (employeeDestroyInfoDTO.getNewParentEmployeeId() != null) {
            employeeJobRoleService.changeParentEmployee(employeeDestroyInfoDTO.getEmployeeId(), employeeDestroyInfoDTO.getNewParentEmployeeId(),loginUserId);
        }

        return true;
    }

    /**
     * 员工档案信息查询
     *
     * @param employeeInfoDTO
     * @param page
     * @return
     */
    @Override
    public IPage<EmployeeInfoDTO> queryEmployeeList4Page(EmployeeQueryConditionDTO employeeInfoDTO, Page<EmployeeQueryConditionDTO> page) {
         //当传入部门为空时判断，根据登录员工的部门计算得到可以查询的部门集合
        if(StringUtils.isBlank(employeeInfoDTO.getOrgSet())){
            UserContext userContext= WebContextUtils.getUserContext();
            Long orgId=userContext.getOrgId();

            OrgDO orgDO=SpringContextUtils.getBean(OrgDO.class,orgId);
            String orgLine=orgDO.getOrgLine();

            employeeInfoDTO.setOrgSet(orgLine);
        }
        IPage<EmployeeInfoDTO> iPage = employeeService.queryEmployeeList4Page(employeeInfoDTO, page);
        if (iPage == null) {
            return null;
        }
        for (EmployeeInfoDTO employeeInfoDTOResult : iPage.getRecords()) {
            employeeInfoDTOResult.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", employeeInfoDTOResult.getJobRole()));
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, employeeInfoDTOResult.getOrgId());
            employeeInfoDTOResult.setOrgPath(orgDO.getCompanyLinePath());
            employeeInfoDTOResult.setTypeName(this.staticDataService.getCodeName("EMPLOYEE_TYPE", employeeInfoDTOResult.getType()));

            LocalDate jobDate = employeeInfoDTOResult.getJobDate();
            if (jobDate == null) {
                employeeInfoDTOResult.setCompanyAge("0");
            }else{
                employeeInfoDTOResult.setCompanyAge(TimeUtils.getAbsDateDiffYear(jobDate, LocalDate.now())+"");
            }

        }
        return iPage;
    }

    /**
     * 加载员工档案
     *
     * @param employeeId
     * @return
     */
    @Override
    public EmployeeArchiveDTO loadMyArchive(Long employeeId) {
        Employee employee = this.employeeService.getById(employeeId);

        EmployeeArchiveDTO archive = new EmployeeArchiveDTO();
        BeanUtils.copyProperties(employee, archive);

        EmployeeJobRole jobRole = this.employeeJobRoleService.queryLast(employeeId);
        BeanUtils.copyProperties(jobRole, archive);

        List<EmployeeHistory> histories = this.employeeHistoryService.queryHistories(employeeId);
        if (ArrayUtils.isNotEmpty(histories)) {
            List<EmployeeHistoryDTO> historyDTOs = new ArrayList<>();
            for (EmployeeHistory history : histories) {
                EmployeeHistoryDTO historyDTO = new EmployeeHistoryDTO();
                BeanUtils.copyProperties(history, historyDTO);

                historyDTO.setEventTypeName(this.staticDataService.getCodeName("HISTORY_EVENT_TYPE", history.getEventType()));
                historyDTOs.add(historyDTO);
            }
            archive.setHistories(historyDTOs);
        }

        List<EmployeeWorkExperience> workExperiences = this.employeeWorkExperienceService.queryByEmployeeId(employeeId);
        if (ArrayUtils.isNotEmpty(workExperiences)) {
            List<EmployeeWorkExperienceDTO> workExperienceDTOS = new ArrayList<>();
            for (EmployeeWorkExperience workExperience : workExperiences) {
                EmployeeWorkExperienceDTO workExperienceDTO = new EmployeeWorkExperienceDTO();
                BeanUtils.copyProperties(workExperience, workExperienceDTO);
                workExperienceDTOS.add(workExperienceDTO);
            }
            archive.setWorkExperiences(workExperienceDTOS);
        }

        List<EmployeeChildren> children = this.employeeChildrenService.queryByEmployeeId(employeeId);
        if (ArrayUtils.isNotEmpty(children)) {
            List<EmployeeChildrenDTO> childrenDTO = new ArrayList<>();
            for (EmployeeChildren child :children) {
                EmployeeChildrenDTO childDTO = new EmployeeChildrenDTO();
                BeanUtils.copyProperties(child, childDTO);
                childDTO.setSexName(this.staticDataService.getCodeName("SEX", child.getSex()));

                int age = 0;
                LocalDate birthday = child.getBirthday();
                if (birthday != null) {
                    LocalDate today = LocalDate.now();
                    age = TimeUtils.getAbsDateDiffYear(birthday, today);
                }
                childDTO.setAge(age);
                childrenDTO.add(childDTO);
            }
            archive.setChildren(childrenDTO);
        }

        EmployeeDO employeeDO = new EmployeeDO(employee);
        archive.setSexName(this.staticDataService.getCodeName("SEX", employee.getSex()));
        archive.setAge(employeeDO.getAge() + "");
        archive.setTypeName(this.staticDataService.getCodeName("EMPLOYEE_TYPE", employee.getType()));

        AddressDO addressDO = SpringContextUtils.getBean(AddressDO.class);
        archive.setNativeArea(addressDO.getFullName(employee.getNativeRegion()));
        archive.setHomeArea(addressDO.getFullName(employee.getHomeRegion()));
        archive.setCompanyAge(employeeDO.getJobYear() + "");
        archive.setEducationLevelName(this.staticDataService.getCodeName("EDUCATION_LEVEL", employee.getEducationLevel()));
        archive.setFirstEducationLevelName(this.staticDataService.getCodeName("EDUCATION_LEVEL", employee.getFirstEducationLevel()));
        archive.setSchoolTypeName(this.staticDataService.getCodeName("SCHOOL_TYPE", employee.getSchoolType()));
        archive.setIsSocialSecurityName(this.staticDataService.getCodeName("YES_NO", employee.getIsSocialSecurity() + ""));
        archive.setIsSocialSecurityName(this.staticDataService.getCodeName("SOCIAL_SECURITY_STATUS", employee.getSocialSecurityStatus()));
        archive.setStatusName(this.staticDataService.getCodeName("EMPLOYEE_STATUS", employee.getStatus()));
        archive.setIsSocialSecurityName(this.staticDataService.getCodeName("YES_NO", employee.getIsSocialSecurity()+""));
        archive.setSocialSecurityStatusName(this.staticDataService.getCodeName("SOCIAL_SECURITY_STATUS", employee.getSocialSecurityStatus()));

        archive.setJobRoleName(this.staticDataService.getCodeName("JOB_ROLE", jobRole.getJobRole()));

        Long parentEmployeeId = jobRole.getParentEmployeeId();
        if (parentEmployeeId != null) {
            Employee parentEmployee = this.employeeService.getById(parentEmployeeId);
            if (parentEmployee != null) {
                archive.setParentEmployeeName(parentEmployee.getName());
            }
        }

        OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, jobRole.getOrgId());
        archive.setOrgPath(orgDO.getCompanyLinePath());
        archive.setJobRoleNatureName(this.staticDataService.getCodeName("JOB_NATURE", jobRole.getJobRoleNature()));

        return archive;
    }

    @Override
    public List<EmployeeInfoDTO> queryEmployeeList(EmployeeQueryConditionDTO conditionDTO) {
        //当传入部门为空时判断，根据登录员工的部门计算得到可以查询的部门集合
        if(StringUtils.isBlank(conditionDTO.getOrgSet())){
            UserContext userContext= WebContextUtils.getUserContext();
            Long orgId=userContext.getOrgId();

            OrgDO orgDO=SpringContextUtils.getBean(OrgDO.class,orgId);
            String orgLine=orgDO.getOrgLine();

            conditionDTO.setOrgSet(orgLine);
        }

        List<EmployeeInfoDTO> list = employeeService.queryEmployeeList(conditionDTO);
        if (list.size() <= 0) {
            return list;
        }
        for (EmployeeInfoDTO employeeInfo : list) {
            employeeInfo.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", employeeInfo.getJobRole()));
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, employeeInfo.getOrgId());
            employeeInfo.setOrgPath(orgDO.getCompanyLinePath());
            employeeInfo.setSex(this.staticDataService.getCodeName("SEX", employeeInfo.getSex()));
            employeeInfo.setTypeName(this.staticDataService.getCodeName("EMPLOYEE_TYPE", employeeInfo.getType()));
            LocalDate jobDate = employeeInfo.getJobDate();
            if (jobDate == null) {
                employeeInfo.setCompanyAge("0");
            }else{
                employeeInfo.setCompanyAge(TimeUtils.getAbsDateDiffYear(jobDate, LocalDate.now())+"");
            }
            AddressDO addressDO = SpringContextUtils.getBean(AddressDO.class);
            employeeInfo.setNativeArea(addressDO.getFullName(employeeInfo.getNativeRegion()));
            employeeInfo.setHomeArea(addressDO.getFullName(employeeInfo.getHomeRegion()));
            employeeInfo.setEducationLevelName(this.staticDataService.getCodeName("EDUCATION_LEVEL", employeeInfo.getEducationLevel()));
            employeeInfo.setFirstEducationLevelName(this.staticDataService.getCodeName("EDUCATION_LEVEL", employeeInfo.getFirstEducationLevel()));
            employeeInfo.setSchoolTypeName(this.staticDataService.getCodeName("SCHOOL_TYPE", employeeInfo.getSchoolType()));
            employeeInfo.setParentEmployeeName(employeeService.getEmployeeNameEmployeeId(employeeInfo.getParentEmployeeId()));
            employeeInfo.setEmployeeStatusName(this.staticDataService.getCodeName("EMPLOYEE_STATUS", employeeInfo.getEmployeeStatus()));
            employeeInfo.setJobRoleNatureName(this.staticDataService.getCodeName("JOB_NATURE", employeeInfo.getJobRoleNature()));
        }
        return list;
    }
}
