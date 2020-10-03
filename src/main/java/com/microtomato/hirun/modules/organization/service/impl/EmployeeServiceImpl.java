package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.organization.entity.consts.EmployeeConst;
import com.microtomato.hirun.modules.organization.entity.domain.EmployeeDO;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.*;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.mapper.EmployeeMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.system.UserContextUtils;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    @Autowired
    private IOrgService orgService;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    @Autowired
    private IStaticDataService staticDataService;

    @Override
    public Employee queryByIdentityNo(String identityNo) {
        List<Employee> employees = employeeMapper.selectList(new QueryWrapper<Employee>().lambda().eq(Employee::getIdentityNo, identityNo));
        if (ArrayUtils.isEmpty(employees)) {
            return null;
        }
        return employees.get(0);
    }

    @Override
    /**
     * 查询员工档案分页数据
     */
    public IPage<EmployeeInfoDTO> queryEmployeeList4Page(EmployeeQueryConditionDTO conditionDTO, Page<EmployeeQueryConditionDTO> employeePage) {
        QueryWrapper<EmployeeQueryConditionDTO> queryWrapper = this.buildQuyerCondition(conditionDTO);
        IPage<EmployeeInfoDTO> iPage = employeeMapper.selectEmployeePage(employeePage, queryWrapper);

        return iPage;
    }

    /**
     * 测试（后期删除）
     */
    @Override
    public IPage<EmployeeExampleDTO> selectEmployeePageExample(String name, Long orgId, Long jobRole) {
        Page<EmployeeExampleDTO> page = new Page<>(1, 10);
        QueryWrapper<EmployeeExampleDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("b.employee_id = a.employee_id AND now() < b.end_date AND c.org_id = b.org_id");
        queryWrapper.like(StringUtils.isNotEmpty(name), "a.name", name);
        queryWrapper.eq(null != orgId, "b.org_id", orgId);
        queryWrapper.eq(null != jobRole, "b.job_role", jobRole);
        IPage<EmployeeExampleDTO> employeeExampleDTOIPage = employeeMapper.selectEmployeePageExample(page, queryWrapper);
        return employeeExampleDTOIPage;
    }

    @Override
    @Cacheable(value = "employee_name_cache")
    public String getEmployeeNameEmployeeId(Long employeeId) {
        Employee employee = this.employeeMapper.selectById(employeeId);
        if (employee == null) {
            return null;
        }
        return employee.getName();
    }

    @Override
    public Employee queryByUserId(Long userId) {
        Employee employee = this.getOne(new QueryWrapper<Employee>().lambda().eq(Employee::getUserId, userId));
        return employee;
    }

    @Override
    public List<EmployeeInfoDTO> findSubordinate(Long parentEmployeeId) {
        List<EmployeeInfoDTO> childEmployees = employeeMapper.queryEmployeeByParentEmployeeId(parentEmployeeId);
        return childEmployees;
    }

    /**
     * @param conditionDTO
     * @return
     */
    @Override
    public List<EmployeeInfoDTO> queryEmployeeList(EmployeeQueryConditionDTO conditionDTO) {
        QueryWrapper<EmployeeQueryConditionDTO> queryWrapper = this.buildQuyerCondition(conditionDTO);
        List<EmployeeInfoDTO> list = employeeMapper.queryEmployeeList(queryWrapper);

        return list;
    }

    @Override
    public EmployeeInfoDTO queryEmployeeInfoByEmployeeId(Long employeeId) {
        if (employeeId == null) {
            return null;
        }
        return employeeMapper.queryEmployeeInfoByEmployeeId(employeeId);
    }

    @Override
    public Map<String, String> showBirthdayWish(Long employeeId) {
        EmployeeDO employeeDO = SpringContextUtils.getBean(EmployeeDO.class, employeeId);
        Map<String, String> result = new HashMap<String, String>();
        if (employeeDO.isTodayBirthday()) {
            result.put("name", employeeDO.getEmployee().getName());

            long pastDays = Duration.between(employeeDO.getEmployee().getInDate(), TimeUtils.getCurrentLocalDateTime()).toDays();
            result.put("day", pastDays + "");
        }
        return result;
    }

    @Override
    public List<EmployeeQuantityStatDTO> countEmployeeQuantityByOrgId() {
        return employeeMapper.countEmployeeQuantityByOrgId();
    }

    /**
     * 加载所有员工
     *
     * @return
     */
    @Override
    public List<Employee> loadEmployee() {
        List<Employee> employeeList = this.list(
                Wrappers.<Employee>lambdaQuery()
                        .select(Employee::getUserId, Employee::getName, Employee::getMobileNo)
                        .eq(Employee::getStatus, "0")
        );
        return employeeList;
    }

    @Override
    public IPage<EmployeeInfoDTO> queryEmployee4BatchChange(Long parentEmployeeId, String orgLine, Page<EmployeeQueryConditionDTO> employeePage) {
        QueryWrapper<EmployeeJobRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(null != parentEmployeeId, "b.parent_employee_id", parentEmployeeId);
        queryWrapper.apply(StringUtils.isNotEmpty(orgLine), "b.org_id in (" + orgLine + ")");
        queryWrapper.apply("a.employee_id=b.employee_id and a.status='0' and b.org_id = c.org_id ");
        queryWrapper.apply("now() between b.start_date and b.end_date and c.status='0' ");
        IPage<EmployeeInfoDTO> iPage = employeeMapper.queryEmployee4BatchChange(employeePage, queryWrapper);
        return iPage;
    }

    /**
     * 查询员工转正信息
     * @param inDate
     * @param orgLine
     * @param isSign
     * @return
     */
    @Override
    public List<EmployeeInfoDTO> queryEmployeeRegularInfo(LocalDate inDate, String orgLine, String isSign) {
        List<EmployeeInfoDTO> employeeList=null;
        if(StringUtils.isBlank(orgLine)){
            orgLine=orgService.listOrgSecurityLine();
        }
        if(inDate==null){
            inDate= LocalDate.now().minusDays(90);
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if(StringUtils.equals(isSign,"1")){
             employeeList=this.employeeMapper.queryEmployeeRegular(orgLine,inDate);
        }

        if(StringUtils.equals(isSign,"0")){
            employeeList=this.employeeMapper.queryEmployeeExistsExam(orgLine,inDate);
        }

        if (ArrayUtils.isEmpty(employeeList)) {
            return new ArrayList<EmployeeInfoDTO>();
        }
        for (EmployeeInfoDTO employeeInfo : employeeList) {
            employeeInfo.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", employeeInfo.getJobRole()));
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, employeeInfo.getOrgId());
            employeeInfo.setOrgPath(orgDO.getCompanyLinePath());
            employeeInfo.setHrEmployeeName(this.getEmployeeNameEmployeeId(employeeInfo.getHrEmployeeId()));
            employeeInfo.setEmployeeStatusName(this.staticDataService.getCodeName("EMPLOYEE_STATUS", employeeInfo.getEmployeeStatus()));
            employeeInfo.setJobRoleNatureName(this.staticDataService.getCodeName("JOB_NATURE", employeeInfo.getJobRoleNature()));
        }

        return employeeList;
    }

    /**
     * 递归查找所有下级员工
     *
     * @param parentEmployeeId
     * @return
     */
    @Override
    public List<EmployeeInfoDTO> recursiveAllSubordinates(String parentEmployeeId) {
        List<EmployeeInfoDTO> subordinates = this.employeeMapper.queryEmployeeByParentEmployeeIds(parentEmployeeId);

        if (ArrayUtils.isEmpty(subordinates)) {
            return subordinates;
        }

        List<EmployeeInfoDTO> resultList = new ArrayList<>();
        resultList.addAll(subordinates);

        String recursiveParentEmployeeIds = "";
        for (EmployeeInfoDTO employee : subordinates) {
            recursiveParentEmployeeIds += employee.getEmployeeId() + ",";
        }
        recursiveParentEmployeeIds = recursiveParentEmployeeIds.substring(0, recursiveParentEmployeeIds.length() - 1);

        if (StringUtils.isNotEmpty(recursiveParentEmployeeIds)) {
            List<EmployeeInfoDTO> tmpSubordinates = recursiveAllSubordinates(recursiveParentEmployeeIds);
            if (ArrayUtils.isNotEmpty(tmpSubordinates)) {
                resultList.addAll(tmpSubordinates);
            }
        }
        return resultList;
    }

    /**
     * 构建档案查询条件
     *
     * @param conditionDTO
     * @return
     */
    private QueryWrapper<EmployeeQueryConditionDTO> buildQuyerCondition(EmployeeQueryConditionDTO conditionDTO) {
        if (conditionDTO == null) {
            return null;
        }
        QueryWrapper<EmployeeQueryConditionDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("c.org_id = b.org_id");
        queryWrapper.like(StringUtils.isNotEmpty(conditionDTO.getName()), "a.name", conditionDTO.getName());
        queryWrapper.eq(StringUtils.isNotEmpty(conditionDTO.getSex()), "a.sex", conditionDTO.getSex());
        queryWrapper.likeRight(StringUtils.isNotEmpty(conditionDTO.getMobileNo()), "a.mobile_no", conditionDTO.getMobileNo());
        queryWrapper.eq(StringUtils.isNotEmpty(conditionDTO.getEmployeeStatus()), "a.status", conditionDTO.getEmployeeStatus());
        queryWrapper.eq(StringUtils.isNotEmpty(conditionDTO.getType()), "a.type", conditionDTO.getType());
        queryWrapper.in(StringUtils.isNotEmpty(conditionDTO.getJobRole()), "b.job_role", Arrays.asList(conditionDTO.getJobRole().split(",")));
        queryWrapper.eq(StringUtils.isNotEmpty(conditionDTO.getJobRoleNature()), "b.job_role_nature", conditionDTO.getJobRoleNature());
        queryWrapper.eq(StringUtils.isNotEmpty(conditionDTO.getDiscountRate()), "b.discount_rate", conditionDTO.getDiscountRate());
        queryWrapper.ge(conditionDTO.getInDateStart() != null, "a.in_date", conditionDTO.getInDateStart());
        queryWrapper.le(conditionDTO.getInDateEnd() != null, "a.in_date", conditionDTO.getInDateEnd());
        queryWrapper.ge(conditionDTO.getDestroyDateStart() != null, "a.destroy_date", conditionDTO.getDestroyDateStart());
        queryWrapper.le(conditionDTO.getDestroyDateEnd() != null, "a.destroy_date", conditionDTO.getDestroyDateEnd());
        queryWrapper.ge(StringUtils.isNotEmpty(conditionDTO.getAgeStart()), "x.age", conditionDTO.getAgeStart());
        queryWrapper.le(StringUtils.isNotEmpty(conditionDTO.getAgeEnd()), "x.age", conditionDTO.getAgeEnd());
        queryWrapper.ge(StringUtils.isNotEmpty(conditionDTO.getJobYearStart()), "x.job_age", conditionDTO.getJobYearStart());
        queryWrapper.le(StringUtils.isNotEmpty(conditionDTO.getJobYearEnd()), "x.job_age", conditionDTO.getJobYearEnd());
        queryWrapper.ge(StringUtils.isNotEmpty(conditionDTO.getCompanyAgeStart()), "x.company_age", conditionDTO.getCompanyAgeStart());
        queryWrapper.le(StringUtils.isNotEmpty(conditionDTO.getCompanyAgeEnd()), "x.company_age", conditionDTO.getCompanyAgeEnd());

        //新增查询条件是否转正以及转正时间判断
        if (StringUtils.equals(conditionDTO.getIsRegular(), EmployeeConst.YES)) {
            queryWrapper.apply("a.regular_date<now()");
        }

        if (StringUtils.equals(conditionDTO.getIsRegular(), EmployeeConst.NO)) {
            queryWrapper.apply("a.regular_date > now()");
        }

        queryWrapper.ge(conditionDTO.getRegularDateStart() != null, "a.regular_date", conditionDTO.getRegularDateStart());
        queryWrapper.le(conditionDTO.getRegularDateEnd() != null, "a.regular_date", conditionDTO.getRegularDateEnd());

        queryWrapper.apply(StringUtils.isNotBlank(conditionDTO.getEmployeeIds()), "a.employee_id in (" + conditionDTO.getEmployeeIds() + ")");

        //如果为查询调动信息的情况不判断现归属部门
        if (!StringUtils.equals(conditionDTO.getOtherStatus(), EmployeeConst.EMPLOYEE_BORROW_STATUS) &&
                !StringUtils.equals(conditionDTO.getOtherStatus(), EmployeeConst.EMPLOYEE_TRANS_STATUS)) {
            queryWrapper.apply(StringUtils.isNotBlank(conditionDTO.getOrgLine()), "b.org_id in (" + conditionDTO.getOrgLine() + ")");
        }

        //休假
        if (StringUtils.equals(conditionDTO.getOtherStatus(), EmployeeConst.EMPLOYEE_HOLIDAY_STATUS)) {
            queryWrapper.exists("select * from ins_employee_holiday ieh where a.employee_id=ieh.employee_id and (now() between ieh.start_time and ieh.end_time)");
        }
        //借调（入）
        if (StringUtils.equals(conditionDTO.getOtherStatus(), EmployeeConst.EMPLOYEE_BORROW_STATUS)) {
            queryWrapper.exists("select * from ins_employee_trans_detail iep where a.employee_id=iep.employee_id  " +
                    "and iep.trans_type='1' and (now() between iep.start_time and iep.end_time)" +
                    "and (iep.org_id in (" + conditionDTO.getOrgLine() + "))");
        }
        //借调（出）
        if (StringUtils.equals(conditionDTO.getOtherStatus(),"3")) {
            queryWrapper.exists("select * from ins_employee_trans_detail iep where a.employee_id=iep.employee_id  " +
                    "and iep.trans_type='1' and (now() between iep.start_time and iep.end_time)" +
                    "and (iep.source_org_id in(" + conditionDTO.getOrgLine() + "))");
        }
        //调动(入)
        if (StringUtils.equals(conditionDTO.getOtherStatus(), "4")) {
            queryWrapper.exists("select * from ins_employee_trans_detail iep where a.employee_id=iep.employee_id  " +
                    "and iep.trans_type='2' and (now() between iep.start_time and iep.end_time)" +
                    "and (iep.org_id in (" + conditionDTO.getOrgLine() + "))");
        }
        //调动(出)
        if (StringUtils.equals(conditionDTO.getOtherStatus(), "5")) {
            queryWrapper.exists("select * from ins_employee_trans_detail iep where a.employee_id=iep.employee_id  " +
                    "and iep.trans_type='2' and (now() between iep.start_time and iep.end_time)" +
                    "and (iep.source_org_id in(" + conditionDTO.getOrgLine() + "))");
        }

        //内部调动（入）
        if (StringUtils.equals(conditionDTO.getOtherStatus(), "6")) {
            queryWrapper.exists("select * from ins_employee_trans_detail iep where a.employee_id=iep.employee_id  " +
                    "and iep.trans_type='4' and (now() between iep.start_time and iep.end_time)" +
                    "and (iep.org_id in (" + conditionDTO.getOrgLine() + "))");
        }

        //内部调动（出）
        if (StringUtils.equals(conditionDTO.getOtherStatus(), "7")) {
            queryWrapper.exists("select * from ins_employee_trans_detail iep where a.employee_id=iep.employee_id  " +
                    "and iep.trans_type='4' and (now() between iep.start_time and iep.end_time)" +
                    "and (iep.source_org_id in(" + conditionDTO.getOrgLine() + "))");
        }

        //是黑名单
        if (StringUtils.equals(conditionDTO.getIsBlackList(), EmployeeConst.YES)) {
            queryWrapper.exists("select * from ins_employee_blacklist ieb where a.identity_no=ieb.identity_no  and (now() between ieb.start_time and ieb.end_time)");
        }
        //不是黑名单
        if (StringUtils.equals(conditionDTO.getIsBlackList(), EmployeeConst.NO)) {
            queryWrapper.notExists("select * from ins_employee_blacklist ieb where a.identity_no=ieb.identity_no  and (now() between ieb.start_time and ieb.end_time)");
        }
        //根据入职类型筛选
        if(StringUtils.isNotBlank(conditionDTO.getCreateType())){
            queryWrapper.exists("select * from ins_employee_tag iet where a.employee_id=iet.employee_id and iet.tag_type= "+ conditionDTO.getCreateType()
                    +" and (now() between iet.start_time and iet.end_time)");
        }

        queryWrapper.orderByAsc("c.nature,c.org_id,a.employee_id,a.status");

        return queryWrapper;
    }

    @Override
    public List<SimpleEmployeeDTO> querySimpleEmployeeInfo(Long orgId, Long roleId, Boolean isSelf) {
        List<SimpleEmployeeDTO> employees = null;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.apply("a.employee_id=b.employee_id " +
                " and a.status='0' and (now() between b.start_date and b.end_date) and is_main='1'");
        if (isSelf) {
            //只查询自己
            UserContext userContext = WebContextUtils.getUserContext();
            queryWrapper.eq("a.employee_id", userContext.getEmployeeId());
            employees=this.employeeMapper.queryEmployee4Select(queryWrapper);
        } else if (StringUtils.equals(roleId + "", "-1")) {
            //查所有
            employees=this.employeeMapper.queryEmployee4Select(queryWrapper);
        } else {
            //按role_id和org_id查
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
            Org root = orgDO.getBelongCompany();

            if (root != null) {
                String orgLine = orgDO.getOrgLine(root.getOrgId());
                employees = this.employeeMapper.querySimpleEmployees(roleId, orgLine);
            }
        }
        return employees;
    }

    /**
     * 权限权限和查询条件查询简易员工信息， 主要供员工选择组件使用
     * @param select
     * @return
     */
    @Override
    public List<SimpleEmployeeDTO> queryEmployeeBySelectMode(EmployeeSelectDTO select) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.apply("a.employee_id=b.employee_id " +
                " and a.status='0' and (now() between b.start_date and b.end_date) and is_main='1'");

        List<SimpleEmployeeDTO> employees = new ArrayList<>();
        List<Long> orgIds = new ArrayList<>();
        String mode = select.getMode();
        Long roleId = select.getRoleId();
        if (StringUtils.equals("self", mode)) {
            //自身模式，只查自己
            UserContext userContext = WebContextUtils.getUserContext();
            queryWrapper.eq("a.employee_id", userContext.getEmployeeId());
            //清空查询条件
            roleId = null;
        } else if (StringUtils.equals("priv", mode)) {
            //权限模式，根据自己的部门查看权限选择对应角色的人
            List<Org> orgs = this.orgService.listOrgsSecurity();
            orgs.forEach((org) -> {
                orgIds.add(org.getOrgId());
            });
        } else if (StringUtils.equals("stride", mode)) {
            //跨店模式，根据自己所在的公司的所有下级部门，根据角色id选择对应的人
            UserContext userContext = WebContextUtils.getUserContext();
            Long orgId = userContext.getOrgId();
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);

            List<Org> orgLines = orgDO.getCompanyLine();
            if (ArrayUtils.isNotEmpty(orgLines)) {
                orgLines.forEach((org) -> {
                    orgIds.add(org.getOrgId());
                });
            }
        }

        if (ArrayUtils.isNotEmpty(orgIds)) {
            queryWrapper.in("b.org_id", orgIds);
        }

        if (roleId != null && !roleId.equals(-1L)) {
            queryWrapper.apply(" exists( select 1 from ins_user_role c where c.user_id = a.user_id and (now() between c.start_date and c.end_date) and c.role_id = "+ roleId +")");
        }

        employees = this.employeeMapper.queryEmployee4Select(queryWrapper);
        if (ArrayUtils.isNotEmpty(employees)) {
            employees.forEach((employee) -> {
                employee.setJobRoleName(this.staticDataService.getCodeName("JOB_ROLE", employee.getJobRole()));
            });
        }
        return employees;
    }

    @Override
    public List<SimpleEmployeeDTO> queryEmployeeByOrgId(Long orgId) {
        if(orgId==null){
            orgId=WebContextUtils.getUserContext().getOrgId();
        }
        OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
        String orgLine=orgDO.getOrgLine(orgId);
        List<SimpleEmployeeDTO> employees=this.employeeMapper.querySimpleEmployeesByOrgId(orgLine);
        return employees;
    }

    @Override
    public List<SimpleEmployeeDTO> queryEmployeeByOrgLine(String orgLine) {
        return this.employeeMapper.querySimpleEmployeesByOrgId(orgLine);
    }

    @Override
    public List<SimpleEmployeeDTO> queryEmployeeByRoleAndOrg(Long orgId, String roleType) {
        if(orgId==null){
            orgId=WebContextUtils.getUserContext().getOrgId();
        }
        OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
        String orgLine=orgDO.getOrgLine(orgId);

        QueryWrapper<SimpleEmployeeDTO> wrapper = new QueryWrapper<>();
        wrapper.apply("c.org_id = b.org_id and a.user_id = e.user_id " +
                "  and e.end_date > now() and a.status='0' and e.role_id in ( " +
                roleType+") and b.org_id in ("+orgLine+")");
        return this.employeeMapper.queryEmployeeByRoleAndOrg(wrapper);
    }

    @Override
    public SimpleEmployeeDTO getLoginEmployeeInfo() {
        UserContext userContext = WebContextUtils.getUserContext();
        if (userContext == null) {
            userContext = UserContextUtils.getUserContext();
        }

        Long employeeId = userContext.getEmployeeId();
        SimpleEmployeeDTO result = new SimpleEmployeeDTO();
        Employee employee = this.getById(employeeId);
        result.setEmployeeId(employee.getEmployeeId());
        result.setName(employee.getName());
        result.setSex(employee.getSex());

        EmployeeJobRole employeeJobRole = this.employeeJobRoleService.queryLast(employeeId);
        result.setJobRole(employeeJobRole.getJobRole());
        result.setJobRoleName(this.staticDataService.getCodeName("JOB_ROLE", result.getJobRole()));

        OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, userContext.getOrgId());
        result.setOrgId(userContext.getOrgId());
        result.setOrgName(orgDO.getOrg().getName());
        return result;
    }
}
