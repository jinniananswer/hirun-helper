package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.cases.AlreadyExistException;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.*;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.consts.OrgConst;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private IEmployeeKeymanService employeeKeyManService;

    @Autowired
    private IEmployeeHistoryService employeeHistoryService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private IEmployeeContractService employeeContractService;

    @Autowired
    private IHrPendingService hrPendingService;

    @Autowired
    private IOrgHrRelService orgHrRelService;

    @Autowired
    private IStatEmployeeTransitionService transitionService;

    @Autowired
    private IEmployeeTagService employeeTagService;

    @Autowired
    private IEmployeeHolidayService holidayService;


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

        if ((StringUtils.equals(EmployeeConst.CREATE_TYPE_NEW_ENTRY, createType) && StringUtils.equals(operType, EmployeeConst.OPER_TYPE_CREATE)) && StringUtils.equals(EmployeeConst.STATUS_NORMAL, employee.getStatus())) {
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

        int companyAge = employeeDO.getCompanyAge();
        employeeDTO.setCompanyAge(new Integer(companyAge));

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

        List<EmployeeKeyman> keyMen = this.employeeKeyManService.queryByEmployeeId(employeeId);

        EmployeeContactManDTO contactMan = new EmployeeContactManDTO();
        if (ArrayUtils.isNotEmpty(keyMen)) {
            List<EmployeeChildrenDTO> childrenDTOS = new ArrayList<>();
            for (EmployeeKeyman keyMan : keyMen) {
                if (StringUtils.equals(EmployeeConst.KEYMAN_TYPE_CHILD, keyMan.getType())) {
                    EmployeeChildrenDTO childDTO = new EmployeeChildrenDTO();
                    BeanUtils.copyProperties(keyMan, childDTO);
                    childrenDTOS.add(childDTO);
                } else if (StringUtils.equals(EmployeeConst.KEYMAN_TYPE_CONTACT, keyMan.getType())) {
                    BeanUtils.copyProperties(keyMan, contactMan);
                }
            }
            employeeDTO.setChildren(childrenDTOS);
            employeeDTO.setContactMan(contactMan);
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
    @DataSource(DataSourceKey.INS)
    public void employeeEntry(EmployeeDTO employeeDTO) {

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        /**如果常用名为空，设置姓名为常用名*/
        if(StringUtils.isBlank(employeeDTO.getCompanyUsedName())){
            employee.setCompanyUsedName(employeeDTO.getName());
        }

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
        List<EmployeeKeyman> keyMen = new ArrayList<>();

        EmployeeContactManDTO contactManDTO = employeeDTO.getContactMan();
        if (contactManDTO != null && (StringUtils.isNotBlank(contactManDTO.getName()) || StringUtils.isNotBlank(contactManDTO.getContactNo()))) {
            EmployeeKeyman keyMan = new EmployeeKeyman();
            BeanUtils.copyProperties(contactManDTO, keyMan);
            keyMan.setType(EmployeeConst.KEYMAN_TYPE_CONTACT);
            keyMen.add(keyMan);
        }

        if (ArrayUtils.isNotEmpty(childrenDTOS)) {
            for (EmployeeChildrenDTO childrenDTO : childrenDTOS) {
                if (StringUtils.isNotBlank(childrenDTO.getName())) {
                    EmployeeKeyman keyMan = new EmployeeKeyman();
                    BeanUtils.copyProperties(childrenDTO, keyMan);
                    keyMan.setType(EmployeeConst.KEYMAN_TYPE_CHILD);
                    keyMen.add(keyMan);
                }
            }
        }

        Long orgId = jobRole.getOrgId();
        Org org = this.orgService.queryByOrgId(orgId);

        if (employeeDTO.getEmployeeId() == null) {
            //没有employeeId，表示是新入职操作

            UserDO userDO = SpringContextUtils.getBean(UserDO.class);
            //默认创建用户
            Long userId = userDO.create(employee.getMobileNo(), null);
            employee.setUserId(userId);

            EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employee);
            employeeDO.newEntry(jobRole, workExperiences, keyMen);

            //分配默认权限
            this.userRoleService.createRole(userId, jobRole.getOrgId(), jobRole.getJobRole(), org.getNature());
        } else {
            EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employeeDTO.getEmployeeId());
            Employee oldEmployee = employeeDO.getEmployee();

            Long userId = oldEmployee.getUserId();
            employee.setUserId(userId);

            UserDO userDO = SpringContextUtils.getBean(UserDO.class, userId);

            String createType = employeeDTO.getCreateType();
            if (StringUtils.equals(EmployeeConst.CREATE_TYPE_REHIRE, createType)) {
                userDO.modify(employeeDTO.getMobileNo(), UserConst.INIT_PASSWORD, UserConst.STATUS_NORMAL);
                employeeDO.rehire(employee, jobRole, workExperiences, keyMen);
                //新增部门异动记录
                transitionService.addEmployeeEntryTransition(employeeDTO.getEmployeeJobRole().getOrgId(), employeeDTO.getEmployeeId(), employeeDTO.getInDate().toLocalDate());
                //分配默认权限
                this.userRoleService.createRole(userId, jobRole.getOrgId(), jobRole.getJobRole(), org.getNature());
                //2020/05/04 新增员工标签
                this.employeeTagService.addEmployeeTag(EmployeeConst.CREATE_TYPE_REHIRE,employeeDTO.getEmployeeId(),"复职");
            } else if (StringUtils.equals(createType, EmployeeConst.CREATE_TYPE_REHELLORING)) {
                userDO.modify(employeeDTO.getMobileNo(), UserConst.INIT_PASSWORD, UserConst.STATUS_NORMAL);
                employeeDO.rehelloring(employee, jobRole, workExperiences, keyMen);
                //新增部门异动记录
                transitionService.addEmployeeEntryTransition(employeeDTO.getEmployeeJobRole().getOrgId(), employeeDTO.getEmployeeId(), employeeDTO.getInDate().toLocalDate());
                //分配默认权限
                this.userRoleService.createRole(userId, jobRole.getOrgId(), org.getNature(), jobRole.getJobRole());
                //2020/05/04 新增员工标签
                this.employeeTagService.addEmployeeTag(EmployeeConst.CREATE_TYPE_REHELLORING,employeeDTO.getEmployeeId(),"返聘");
            } else {
                EmployeeJobRole oldJobRole = this.employeeJobRoleService.queryLast(employeeDTO.getEmployeeId());

                userDO.modify(employeeDTO.getMobileNo(), null, null);

                if (oldJobRole != null && (!StringUtils.equals(oldJobRole.getJobRole(), jobRole.getJobRole()) || !jobRole.getOrgId().equals(oldJobRole.getOrgId()) || !StringUtils.equals(oldJobRole.getJobGrade(), jobRole.getJobGrade()))) {
                    this.employeeHistoryService.createChangeJobRole(employeeDTO.getEmployeeId(), jobRole, null);
                }

                if (!StringUtils.equals(jobRole.getJobRole(), oldJobRole.getJobRole()) || !jobRole.getOrgId().equals(oldJobRole.getOrgId())) {
                    this.userRoleService.switchRole(userId, jobRole.getOrgId(), jobRole.getJobRole(), org.getNature());
                }
                employeeDO.modify(employee, jobRole, workExperiences, keyMen);
            }

        }


    }

    /**
     * 员工离职
     *
     * @param employeeDestroyInfoDTO
     */
    @Override
    @DataSource(DataSourceKey.INS)
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean destroyEmployee(EmployeeDestroyInfoDTO employeeDestroyInfoDTO) {

        List<HrPending> hrPendingList = hrPendingService.queryPendingByExecuteId(employeeDestroyInfoDTO.getEmployeeId());

        if (hrPendingList.size() > 0) {
            throw new AlreadyExistException(" 该员工下存在未处理的待办任务，请将待办任务转移之后再办理离职！", ErrorKind.ALREADY_EXIST.getCode());
        }

        EmployeeJobRole employeeJobRole = employeeJobRoleService.queryValidMain(employeeDestroyInfoDTO.getEmployeeId());
        //判断离职员工是否为后台任务对应人资提醒人员
        List<OrgHrRel> orgHrRelList = orgHrRelService.queryOrgHrRelByEmployeeId(employeeDestroyInfoDTO.getEmployeeId());

        if (ArrayUtils.isNotEmpty(orgHrRelList)) {
            throw new AlreadyExistException(" 该员工为后台人资提醒人员，请到人资部门关系管理菜单将该员工对应的部门转移到其他员工下，再办理离职！", ErrorKind.ALREADY_EXIST.getCode());
        }

        UserContext userContext = WebContextUtils.getUserContext();
        Long loginUserId = userContext.getUserId();
        EmployeeInfoDTO infoDTO = employeeService.queryEmployeeInfoByEmployeeId(employeeDestroyInfoDTO.getEmployeeId());

        //注销employee信息
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDestroyInfoDTO, employee);

        EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employee);
        employeeDO.destroy(employeeDestroyInfoDTO.getDestroyDate());

        //根据离职时间终止合同记录
        employeeContractService.stopEmployeeContract(employeeDestroyInfoDTO.getEmployeeId(), employeeDestroyInfoDTO.getDestroyDate(), loginUserId);

        //终止暂停操作员账号
        Employee userEmployee = employeeService.getById(employeeDestroyInfoDTO.getEmployeeId());
        Long userId = userEmployee.getUserId();
        UserDO userDO = SpringContextUtils.getBean(UserDO.class, userId);
        userDO.destory(employeeDestroyInfoDTO.getDestroyDate());

        //如果为永不录用插入黑名单表
        if (StringUtils.equals(employeeDestroyInfoDTO.getIsBlackList(), EmployeeConst.YES)) {
            EmployeeBlacklist employeeBlacklist = new EmployeeBlacklist();
            BeanUtils.copyProperties(employeeDestroyInfoDTO, employeeBlacklist);
            employeeBlacklist.setStartTime(employeeDestroyInfoDTO.getDestroyDate());
            employeeBlacklist.setEndTime(TimeUtils.getForeverTime());
            employeeBlacklist.setRemark(employeeDestroyInfoDTO.getDestroyReason());

            EmployeeBlackListDO employeeBlackListDO = SpringContextUtils.getBean(EmployeeBlackListDO.class);
            employeeBlackListDO.addBlackList(employeeBlacklist);
        }

        //变更直属下级的上级员工
        if (employeeDestroyInfoDTO.getNewParentEmployeeId() != null) {
            employeeJobRoleService.changeParentEmployee(employeeDestroyInfoDTO.getEmployeeId(), employeeDestroyInfoDTO.getNewParentEmployeeId(), loginUserId);
        }
        //新增离职员工异动报表记录
        EmployeeTransitonDTO dto = new EmployeeTransitonDTO();
        dto.setEmployeeId(employeeJobRole.getEmployeeId());
        dto.setOrgId(employeeJobRole.getOrgId());
        dto.setJobRole(employeeJobRole.getJobRole());
        dto.setJobRoleNature(employeeJobRole.getJobRoleNature());
        dto.setJobGrade(employeeJobRole.getJobGrade());
        dto.setHappenDate(employeeDestroyInfoDTO.getDestroyDate().toLocalDate());
        transitionService.addEmployeeDestroyTransition(dto);
        //新增员工在鸿扬的工作经历信息
        employeeHistoryService.createDestroy(employeeDestroyInfoDTO.getEmployeeId(), employeeDestroyInfoDTO.getDestroyDate().toLocalDate(), employeeDestroyInfoDTO.getDestroyWay());
        return true;
    }

    /**
     * 员工档案信息查询
     *
     * @param conditionDTO
     * @param page
     * @return
     */
    @Override
    public IPage<EmployeeInfoDTO> queryEmployeeList4Page(EmployeeQueryConditionDTO conditionDTO, Page<EmployeeQueryConditionDTO> page) {
        //拼装除了界面传过来的筛选条件
        conditionDTO = this.conditionPlus(conditionDTO);

        IPage<EmployeeInfoDTO> iPage = employeeService.queryEmployeeList4Page(conditionDTO, page);
        if (iPage == null) {
            return null;
        }
        for (EmployeeInfoDTO employeeInfoDTOResult : iPage.getRecords()) {
            employeeInfoDTOResult.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", employeeInfoDTOResult.getJobRole()));
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, employeeInfoDTOResult.getOrgId());
            employeeInfoDTOResult.setOrgPath(orgDO.getCompanyLinePath());
            employeeInfoDTOResult.setTypeName(this.staticDataService.getCodeName("EMPLOYEE_TYPE", employeeInfoDTOResult.getType()));
            employeeInfoDTOResult.setJobRoleNatureName(this.staticDataService.getCodeName("JOB_NATURE", employeeInfoDTOResult.getJobRoleNature()));
            employeeInfoDTOResult.setParentEmployeeName(employeeService.getEmployeeNameEmployeeId(employeeInfoDTOResult.getParentEmployeeId()));

            List<EmployeeTag> employeeTags=employeeTagService.list(new QueryWrapper<EmployeeTag>().lambda()
                    .eq(EmployeeTag::getEmployeeId,employeeInfoDTOResult.getEmployeeId())
                    .eq(EmployeeTag::getTagType,"2"));
            if(ArrayUtils.isEmpty(employeeTags)){
                employeeInfoDTOResult.setSecondEntry("否");
            }else {
                employeeInfoDTOResult.setSecondEntry("是");
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

        List<EmployeeKeyman> keyMen = this.employeeKeyManService.queryByEmployeeId(employeeId);
        EmployeeContactManDTO contactMan = new EmployeeContactManDTO();

        if (ArrayUtils.isNotEmpty(keyMen)) {
            List<EmployeeChildrenDTO> childrenDTO = new ArrayList<>();
            for (EmployeeKeyman keyMan : keyMen) {
                if (StringUtils.equals(EmployeeConst.KEYMAN_TYPE_CHILD, keyMan.getType())) {
                    EmployeeChildrenDTO childDTO = new EmployeeChildrenDTO();
                    BeanUtils.copyProperties(keyMan, childDTO);
                    childDTO.setSexName(this.staticDataService.getCodeName("SEX", keyMan.getSex()));

                    int age = 0;
                    LocalDate birthday = keyMan.getBirthday();
                    if (birthday != null) {
                        LocalDate today = LocalDate.now();
                        age = TimeUtils.getAbsDateDiffYear(birthday, today);
                    }
                    childDTO.setAge(age);
                    childrenDTO.add(childDTO);
                } else if (StringUtils.equals(EmployeeConst.KEYMAN_TYPE_CONTACT, keyMan.getType())) {
                    BeanUtils.copyProperties(keyMan, contactMan);
                    contactMan.setRelTypeName(this.staticDataService.getCodeName("KEYMAN_REL_TYPE", contactMan.getRelType()));
                }
            }
            archive.setChildren(childrenDTO);
            archive.setContactMan(contactMan);
        }

        EmployeeDO employeeDO = new EmployeeDO(employee);
        archive.setSexName(this.staticDataService.getCodeName("SEX", employee.getSex()));
        archive.setAge(employeeDO.getAge() + "");
        archive.setTypeName(this.staticDataService.getCodeName("EMPLOYEE_TYPE", employee.getType()));
        archive.setDestroyWayName(this.staticDataService.getCodeName("DESTROY_WAY", employee.getDestroyWay()));

        AddressDO addressDO = SpringContextUtils.getBean(AddressDO.class);
        archive.setNativeArea(addressDO.getFullName(employee.getNativeRegion()));
        archive.setHomeArea(addressDO.getFullName(employee.getHomeRegion()));
        archive.setEducationLevelName(this.staticDataService.getCodeName("EDUCATION_LEVEL", employee.getEducationLevel()));
        archive.setFirstEducationLevelName(this.staticDataService.getCodeName("EDUCATION_LEVEL", employee.getFirstEducationLevel()));
        archive.setSchoolTypeName(this.staticDataService.getCodeName("SCHOOL_TYPE", employee.getSchoolType()));
        archive.setIsSocialSecurityName(this.staticDataService.getCodeName("YES_NO", employee.getIsSocialSecurity() + ""));
        archive.setIsSocialSecurityName(this.staticDataService.getCodeName("SOCIAL_SECURITY_STATUS", employee.getSocialSecurityStatus()));
        archive.setStatusName(this.staticDataService.getCodeName("EMPLOYEE_STATUS", employee.getStatus()));
        archive.setIsSocialSecurityName(this.staticDataService.getCodeName("YES_NO", employee.getIsSocialSecurity() + ""));
        archive.setSocialSecurityStatusName(this.staticDataService.getCodeName("SOCIAL_SECURITY_STATUS", employee.getSocialSecurityStatus()));
        archive.setCompanyAge(employeeDO.getCompanyAge() + "");
        archive.setJobRoleName(this.staticDataService.getCodeName("JOB_ROLE", jobRole.getJobRole()));
        archive.setJobGradeName(this.staticDataService.getCodeName("JOB_GRADE", jobRole.getJobGrade()));

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

        List<EmployeeTag> employeeTags=employeeTagService.list(new QueryWrapper<EmployeeTag>().lambda()
                .eq(EmployeeTag::getEmployeeId,employeeId).eq(EmployeeTag::getTagType,"2"));

        if(ArrayUtils.isEmpty(employeeTags)){
            archive.setSecondEntry("否");
        }else {
            archive.setSecondEntry("是");
        }

        return archive;
    }

    @Override
    public List<EmployeeInfoDTO> queryEmployeeList(EmployeeQueryConditionDTO conditionDTO) {
        //拼装除了界面传过来的筛选条件
        conditionDTO = this.conditionPlus(conditionDTO);

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
            AddressDO addressDO = SpringContextUtils.getBean(AddressDO.class);
            employeeInfo.setNativeArea(addressDO.getFullName(employeeInfo.getNativeRegion()));
            employeeInfo.setHomeArea(addressDO.getFullName(employeeInfo.getHomeRegion()));
            employeeInfo.setEducationLevelName(this.staticDataService.getCodeName("EDUCATION_LEVEL", employeeInfo.getEducationLevel()));
            employeeInfo.setFirstEducationLevelName(this.staticDataService.getCodeName("EDUCATION_LEVEL", employeeInfo.getFirstEducationLevel()));
            employeeInfo.setSchoolTypeName(this.staticDataService.getCodeName("SCHOOL_TYPE", employeeInfo.getSchoolType()));
            employeeInfo.setParentEmployeeName(employeeService.getEmployeeNameEmployeeId(employeeInfo.getParentEmployeeId()));
            employeeInfo.setEmployeeStatusName(this.staticDataService.getCodeName("EMPLOYEE_STATUS", employeeInfo.getEmployeeStatus()));
            employeeInfo.setJobRoleNatureName(this.staticDataService.getCodeName("JOB_NATURE", employeeInfo.getJobRoleNature()));
            employeeInfo.setDestroyWay(this.staticDataService.getCodeName("DESTROY_WAY", employeeInfo.getDestroyWay()));
            List<EmployeeHoliday> holidays=this.holidayService.list(new QueryWrapper<EmployeeHoliday>().lambda()
                    .eq(EmployeeHoliday::getEmployeeId,employeeInfo.getEmployeeId()).orderByDesc(EmployeeHoliday::getCreateTime));

            if(ArrayUtils.isEmpty(holidays)){
                continue;
            }
            String holidayRecord="";
            for(EmployeeHoliday holiday:holidays){
                holidayRecord+=this.staticDataService.getCodeName("HOLIDAY_TYPE",holiday.getHolidayType()+"")
                        +" 开始时间: "+holiday.getStartTime()+" 结束时间: "+holiday.getEndTime()+"  "+holiday.getRemark()
                        +"\r\n";
            }
            employeeInfo.setHolidayRecord(holidayRecord);
        }
        return list;
    }

    @Override
    public Map<String, String> queryExtendCondition4Destroy(Long employeeId) {
        Map<String, String> resultMap = new HashMap<>();
        //获取离职员工的是否有下级
        List<EmployeeInfoDTO> list = employeeService.findSubordinate(employeeId);
        if (list.size() <= 0) {
            resultMap.put("hasChildEmployee", "NO");
        } else {
            resultMap.put("hasChildEmployee", "YES");
        }
        //获取员工离职次数 1、离职员工未做复职做的员工新增，存在多条身份证一致的信息 2、做复职处理判断之前记录的离职次数
        Employee employee = this.employeeMapper.selectById(employeeId);
        List<Employee> employeeList = this.employeeMapper.selectList(Wrappers.<Employee>lambdaQuery().eq(Employee::getIdentityNo, employee.getIdentityNo()));
        if (employeeList.size() > 1) {
            resultMap.put("destroyTimes", "2");
        } else {
            Integer destroyTimes = employee.getDestroyTimes();
            if (null == destroyTimes || 0 == destroyTimes) {
                resultMap.put("destroyTimes", "1");
            } else {
                resultMap.put("destroyTimes", "2");
            }
        }
        return resultMap;
    }

    @Override
    public IPage<EmployeeInfoDTO> queryEmployee4BatchChange(Long parentEmployeeId, Long orgId, Page<EmployeeQueryConditionDTO> page) {
        String orgLine = "";
        if (null == orgId) {
            UserContext userContext = WebContextUtils.getUserContext();
            orgId = userContext.getOrgId();
            orgLine = orgService.listOrgSecurityLine();
        } else {
            OrgDO conditionOrgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
            orgLine = conditionOrgDO.getOrgLine(orgId);
        }

        IPage<EmployeeInfoDTO> iPage = employeeService.queryEmployee4BatchChange(parentEmployeeId, orgLine, page);
        if (iPage == null) {
            return null;
        }
        for (EmployeeInfoDTO employeeInfoDTOResult : iPage.getRecords()) {
            employeeInfoDTOResult.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", employeeInfoDTOResult.getJobRole()));
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, employeeInfoDTOResult.getOrgId());
            employeeInfoDTOResult.setOrgPath(orgDO.getCompanyLinePath());
            employeeInfoDTOResult.setJobRoleNatureName(this.staticDataService.getCodeName("JOB_NATURE", employeeInfoDTOResult.getJobRoleNature()));
            employeeInfoDTOResult.setParentEmployeeName(employeeService.getEmployeeNameEmployeeId(employeeInfoDTOResult.getParentEmployeeId()));
        }

        return iPage;
    }


    /**
     * 拼装除了界面传过来的筛选条件
     * 根据员工权限判断对应的数据权限
     * 判断是否存在部门人资的关联关系数据
     * 根据员工上下级筛选数据
     *
     * @param conditionDTO
     * @return
     */
    private EmployeeQueryConditionDTO conditionPlus(EmployeeQueryConditionDTO conditionDTO) {
        //根据登录员工的部门计算得到可以查询的部门集合
        UserContext userContext = WebContextUtils.getUserContext();
        Long orgId = userContext.getOrgId();
        Long employeeId = userContext.getEmployeeId();
        String employeeIds = "";
        String orgLine = "";

        if (StringUtils.isEmpty(conditionDTO.getOrgId())) {
            orgLine = orgService.listOrgSecurityLine();
        } else {
/*            OrgDO conditionOrgDO = SpringContextUtils.getBean(OrgDO.class, conditionDTO.getOrgId());
            orgLine = conditionOrgDO.getOrgLine(conditionDTO.getOrgId());*/
            orgLine = conditionDTO.getOrgId();
        }

        if (!SecurityUtils.hasFuncId(OrgConst.SECURITY_ALL_ORG)
                && !SecurityUtils.hasFuncId(OrgConst.SECURITY_ALL_BU)
                && !SecurityUtils.hasFuncId(OrgConst.SECURITY_ALL_SUB_COMPANY)
                && !SecurityUtils.hasFuncId(OrgConst.SECURITY_ALL_SHOP)
                && !SecurityUtils.hasFuncId(OrgConst.SECURITY_SELF_SHOP)
                && !SecurityUtils.hasFuncId(OrgConst.SECURITY_SELF_BU)
                && !SecurityUtils.hasFuncId(OrgConst.SECURITY_SELF_SUB_COMPANY)
                && !SecurityUtils.hasFuncId(OrgConst.SECURITY_ALL_COMANY_SHOP)
                && !StringUtils.isNotBlank(orgHrRelService.getOrgLine(employeeId))) {

            List<EmployeeInfoDTO> employeeList = employeeService.recursiveAllSubordinates(employeeId + "");
            if (ArrayUtils.isEmpty(employeeList)) {
                conditionDTO.setEmployeeIds(employeeId + "");
                conditionDTO.setOrgLine(orgId + "");
            }
            for (EmployeeInfoDTO employee : employeeList) {
                employeeIds += employee.getEmployeeId() + ",";
            }
            conditionDTO.setOrgLine("");
            conditionDTO.setEmployeeIds(employeeIds + employeeId);
        } else {
            conditionDTO.setOrgLine(orgLine);
        }
        return conditionDTO;
    }
}
