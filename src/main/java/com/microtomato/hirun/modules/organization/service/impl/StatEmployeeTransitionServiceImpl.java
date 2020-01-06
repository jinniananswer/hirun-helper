package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQuantityStatDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeTransitonDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeQuantityMonth;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeTransition;
import com.microtomato.hirun.modules.organization.mapper.StatEmployeeQuantityMonthMapper;
import com.microtomato.hirun.modules.organization.mapper.StatEmployeeTransitionMapper;
import com.microtomato.hirun.modules.organization.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2019-12-22
 */
@Slf4j
@Service
public class StatEmployeeTransitionServiceImpl extends ServiceImpl<StatEmployeeTransitionMapper, StatEmployeeTransition> implements IStatEmployeeTransitionService {

    @Autowired
    private StatEmployeeTransitionMapper mapper;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IOrgService orgService;

    @Autowired
    private IEmployeeJobRoleService jobRoleService;

    @Autowired
    private IStaticDataService staticDataService;


    public StatEmployeeTransition queryTransitionRecord(StatEmployeeTransition employeeTransition) {


        StatEmployeeTransition statEmployeeTransition = this.mapper.selectOne(new QueryWrapper<StatEmployeeTransition>().lambda()
                .eq(StatEmployeeTransition::getYear, employeeTransition.getYear())
                .eq(StatEmployeeTransition::getOrgId, employeeTransition.getOrgId())
                .eq(StatEmployeeTransition::getMonth, employeeTransition.getMonth())
                .eq(StatEmployeeTransition::getJobRole, employeeTransition.getJobRole())
                .eq(StatEmployeeTransition::getOrgNature, employeeTransition.getOrgNature())
                .eq(StatEmployeeTransition::getJobGrade, employeeTransition.getJobGrade())
                .eq(StatEmployeeTransition::getJobRoleNature, employeeTransition.getJobRoleNature()));
        return statEmployeeTransition;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addEmployeeEntryTransition(Long orgId, Long employeeId, LocalDate inDate) {
        StatEmployeeTransition statEmployeeTransitionNew = buildTransition(orgId, inDate, employeeId);
        StatEmployeeTransition statEmployeeTransitionRecord = queryTransitionRecord(statEmployeeTransitionNew);
        if (statEmployeeTransitionRecord == null) {
            statEmployeeTransitionNew.setEntryEmployeeId(employeeId + "");
            statEmployeeTransitionNew.setEmployeeEntryQuantity(1f);
            this.mapper.insert(statEmployeeTransitionNew);
        } else {
            statEmployeeTransitionRecord.setEmployeeEntryQuantity(statEmployeeTransitionRecord.getEmployeeEntryQuantity() + 1);
            if (StringUtils.isEmpty(statEmployeeTransitionRecord.getEntryEmployeeId())) {
                statEmployeeTransitionRecord.setEntryEmployeeId(employeeId + "");
            } else {
                statEmployeeTransitionRecord.setEntryEmployeeId(statEmployeeTransitionRecord.getEntryEmployeeId() + "," + employeeId);
            }
            this.mapper.updateById(statEmployeeTransitionRecord);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addEmployeeHolidayTransition(Long orgId, Long employeeId, LocalDate holidayDate) {
        StatEmployeeTransition statEmployeeTransitionNew = buildTransition(orgId, holidayDate, employeeId);
        StatEmployeeTransition statEmployeeTransitionRecord = queryTransitionRecord(statEmployeeTransitionNew);
        if (statEmployeeTransitionRecord == null) {
            statEmployeeTransitionNew.setHolidayEmployeeId(employeeId + "");
            statEmployeeTransitionNew.setEmployeeHolidayQuantity(1f);
            this.mapper.insert(statEmployeeTransitionNew);
        } else {
            statEmployeeTransitionRecord.setEmployeeHolidayQuantity(statEmployeeTransitionRecord.getEmployeeHolidayQuantity() + 1);
            if (StringUtils.isEmpty(statEmployeeTransitionRecord.getHolidayEmployeeId())) {
                statEmployeeTransitionRecord.setHolidayEmployeeId(employeeId + "");
            } else {
                statEmployeeTransitionRecord.setHolidayEmployeeId(statEmployeeTransitionRecord.getHolidayEmployeeId() + "," + employeeId);
            }
            this.mapper.updateById(statEmployeeTransitionRecord);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addEmployeeTransTransition(Long inOrgId, Long outOrgId, Long employeeId, LocalDate transDate,String oldJobRole,String oldJobRoleNature,String oldJobGrade) {
        StatEmployeeTransition transInTransition = buildTransition(inOrgId, transDate, employeeId);
        StatEmployeeTransition transOutTransition = buildTransition(outOrgId, transDate, employeeId);

        //这个地方需特殊处理jobRole,jobRoleNature,因为在借调之后数据已在数据中更新
        if(StringUtils.isEmpty(oldJobRole)){
            transOutTransition.setJobRole("9999");
        }else{
            transOutTransition.setJobRole(oldJobRole);
        }
        if(StringUtils.isEmpty(oldJobRoleNature)){
            transOutTransition.setJobRoleNature("0");
        }else{
            transOutTransition.setJobRoleNature(oldJobRoleNature);
        }
        if(StringUtils.isEmpty(oldJobGrade)){
            transOutTransition.setJobGrade("0");
        }else{
            transOutTransition.setJobGrade(oldJobGrade);
        }

        //处理转入部门的数据
        StatEmployeeTransition transInTransitionRecord = queryTransitionRecord(transInTransition);
        if (transInTransitionRecord == null) {
            transInTransition.setTransInEmployeeId(employeeId + "");
            transInTransition.setEmployeeTransInQuantity(1f);
            this.mapper.insert(transInTransition);
        } else {
            transInTransitionRecord.setEmployeeTransInQuantity(transInTransitionRecord.getEmployeeTransInQuantity() + 1);
            if (StringUtils.isEmpty(transInTransitionRecord.getTransInEmployeeId())) {
                transInTransitionRecord.setTransInEmployeeId(employeeId + "");
            } else {
                transInTransitionRecord.setTransInEmployeeId(transInTransitionRecord.getTransInEmployeeId() + "," + employeeId);
            }
            this.mapper.updateById(transInTransitionRecord);
        }
        //处理转出部门的数据
        StatEmployeeTransition transOutTransitionRecord = queryTransitionRecord(transOutTransition);
        if (transOutTransitionRecord == null) {
            transOutTransition.setTransOutEmployeeId(employeeId + "");
            transOutTransition.setEmployeeTransOutQuantity(1f);
            this.mapper.insert(transOutTransition);
        } else {
            transOutTransitionRecord.setEmployeeTransOutQuantity(transOutTransitionRecord.getEmployeeTransOutQuantity() + 1);
            if (StringUtils.isEmpty(transOutTransitionRecord.getTransOutEmployeeId())) {
                transOutTransitionRecord.setTransOutEmployeeId(employeeId + "");
            } else {
                transOutTransitionRecord.setTransOutEmployeeId(transOutTransitionRecord.getTransOutEmployeeId() + "," + employeeId);
            }
            this.mapper.updateById(transOutTransitionRecord);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addEmployeeBorrowTransition(Long inOrgId, Long outOrgId, Long employeeId, LocalDate borrowDate,String oldJobRole,String oldJobRoleNature,String oldJobGrade) {
        StatEmployeeTransition borrowInTransition = buildTransition(inOrgId, borrowDate, employeeId);
        StatEmployeeTransition borrowOutTransition = buildTransition(outOrgId, borrowDate, employeeId);
        //这个地方需特殊处理jobRole,jobRoleNature,因为在借调之后数据已在数据中更新
        if(StringUtils.isEmpty(oldJobRole)){
            borrowOutTransition.setJobRole("9999");
        }else{
            borrowOutTransition.setJobRole(oldJobRole);

        }

        if(StringUtils.isEmpty(oldJobRoleNature)){
            borrowOutTransition.setJobRoleNature("0");

        }else {
            borrowOutTransition.setJobRoleNature(oldJobRoleNature);
        }

        if(StringUtils.isEmpty(oldJobGrade)){
            borrowOutTransition.setJobGrade("0");

        }else {
            borrowOutTransition.setJobGrade(oldJobGrade);
        }

        //处理转入部门的数据
        StatEmployeeTransition borrowInTransitionRecord = queryTransitionRecord(borrowInTransition);
        if (borrowInTransitionRecord == null) {
            borrowInTransition.setBorrowInEmployeeId(employeeId + "");
            borrowInTransition.setEmployeeBorrowInQuantity(1f);
            this.mapper.insert(borrowInTransition);
        } else {
            borrowInTransitionRecord.setEmployeeBorrowInQuantity(borrowInTransitionRecord.getEmployeeBorrowInQuantity() + 1);
            if (StringUtils.isEmpty(borrowInTransitionRecord.getBorrowInEmployeeId())) {
                borrowInTransitionRecord.setBorrowInEmployeeId(employeeId + "");
            } else {
                borrowInTransitionRecord.setBorrowInEmployeeId(borrowInTransitionRecord.getBorrowInEmployeeId() + "," + employeeId);
            }

            this.mapper.updateById(borrowInTransitionRecord);
        }
        //处理转出部门的数据
        StatEmployeeTransition borrowOutTransitionRecord = queryTransitionRecord(borrowOutTransition);
        if (borrowOutTransitionRecord == null) {
            borrowOutTransition.setBorrowOutEmployeeId(employeeId + "");
            borrowOutTransition.setEmployeeBorrowOutQuantity(1f);
            this.mapper.insert(borrowOutTransition);
        } else {
            borrowOutTransitionRecord.setEmployeeBorrowOutQuantity(borrowOutTransitionRecord.getEmployeeBorrowOutQuantity() + 1);
            if (StringUtils.isEmpty(borrowOutTransitionRecord.getBorrowOutEmployeeId())) {
                borrowOutTransitionRecord.setBorrowOutEmployeeId(employeeId + "");
            } else {
                borrowOutTransitionRecord.setBorrowOutEmployeeId(borrowOutTransitionRecord.getBorrowOutEmployeeId() + "," + employeeId);
            }
            this.mapper.updateById(borrowOutTransitionRecord);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addEmployeeDestroyTransition(EmployeeTransitonDTO dto) {
        StatEmployeeTransition statEmployeeTransition = buildTransition(dto.getOrgId(), dto.getHappenDate(), dto.getEmployeeId());

        if(StringUtils.isEmpty(dto.getJobRole())){
            statEmployeeTransition.setJobRole("9999");
        }else {
            statEmployeeTransition.setJobRole(dto.getJobRole());
        }
        if(StringUtils.isEmpty(dto.getJobRoleNature())){
            statEmployeeTransition.setJobRoleNature("0");
        }else {
            statEmployeeTransition.setJobRoleNature(dto.getJobRoleNature());
        }
        if (StringUtils.isEmpty(dto.getJobGrade())) {
            statEmployeeTransition.setJobGrade("0");
        } else {
            statEmployeeTransition.setJobGrade(dto.getJobGrade());
        }

        StatEmployeeTransition statEmployeeTransitionRecord = queryTransitionRecord(statEmployeeTransition);
        if (statEmployeeTransitionRecord == null) {
            statEmployeeTransition.setDestroyEmployeeId(dto.getEmployeeId() + "");
            statEmployeeTransition.setEmployeeDestroyQuantity(1f);
            this.mapper.insert(statEmployeeTransition);
        } else {
            statEmployeeTransitionRecord.setEmployeeDestroyQuantity(statEmployeeTransitionRecord.getEmployeeDestroyQuantity() + 1);
            if (StringUtils.isEmpty(statEmployeeTransitionRecord.getDestroyEmployeeId())) {
                statEmployeeTransitionRecord.setDestroyEmployeeId(dto.getEmployeeId() + "");
            } else {
                statEmployeeTransitionRecord.setDestroyEmployeeId(statEmployeeTransitionRecord.getDestroyEmployeeId() + "," + dto.getEmployeeId());
            }
            this.mapper.updateById(statEmployeeTransitionRecord);
        }
    }

    @Override
    public List<Map<String,String>> queryTransitionList(Long orgId, String queryTime) {
        String year = "";
        String month = "";
        if (StringUtils.isNotEmpty(queryTime)) {
            year = queryTime.split("-")[0];
            month = queryTime.split("-")[1];
        }else{
            Calendar date = Calendar.getInstance();
            year = String.valueOf(date.get(Calendar.YEAR));
            month = String.valueOf(date.get(Calendar.MONTH) + 1);
        }

        UserContext userContext = WebContextUtils.getUserContext();
        Long userOrgId = userContext.getOrgId();
        OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, userOrgId);
        String orgLine="";

        if (null == orgId) {
            orgLine = orgService.listOrgSecurityLine();
        } else {
            OrgDO orgDo = SpringContextUtils.getBean(OrgDO.class, orgId);
            orgLine = orgDo.getOrgLine(orgId);
        }

        List<Map<String,String>> statEmployeeTransitionList = this.mapper.employeeTransitionDetail(year,month,orgLine);

        if (ArrayUtils.isEmpty(statEmployeeTransitionList)) {
            return new ArrayList<>();
        }

        for (Map<String,String> employeeTransition : statEmployeeTransitionList) {
            OrgDO orgDo = SpringContextUtils.getBean(OrgDO.class, employeeTransition.get("org_id"));
            employeeTransition.put("orgName",orgDo.getCompanyLinePath());
            employeeTransition.put("jobRoleName",staticDataService.getCodeName("JOB_ROLE",employeeTransition.get("job_role")));
            employeeTransition.put("destroyEmployeeName",this.transEmployeeName(employeeTransition.get("destroy_employee_ids")));
            employeeTransition.put("holidayEmployeeName",this.transEmployeeName(employeeTransition.get("holiday_employee_ids")));
            employeeTransition.put("entryEmployeeName",this.transEmployeeName(employeeTransition.get("entry_employee_ids")));
            employeeTransition.put("transInEmployeeName",this.transEmployeeName(employeeTransition.get("trans_in_employee_ids")));
            employeeTransition.put("transOutEmployeeName",this.transEmployeeName(employeeTransition.get("trans_out_employee_ids")));
            employeeTransition.put("borrowInEmployeeName",this.transEmployeeName(employeeTransition.get("borrow_in_employee_ids")));
            employeeTransition.put("borrowOutEmployeeName",this.transEmployeeName(employeeTransition.get("borrow_out_employee_ids")));
        }
        return statEmployeeTransitionList;
    }

    /**
     * 获取年
     */
    private String getYear() {
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        return year;
    }

    /**
     * 获取月
     */
    private String getMonth() {
        Calendar date = Calendar.getInstance();
        String month = String.valueOf(date.get(Calendar.MONTH) + 1);
        return month;
    }

    public StatEmployeeTransition buildTransition(Long orgId, LocalDate localDate, Long employeeId) {

        String year = "";
        String month = "";
        if (localDate == null) {
            year = getYear();
            month = getMonth();
        } else {
            year = localDate.getYear() + "";
            month = localDate.getMonthValue() + "";
        }

        Org org = orgService.getById(orgId);

        StatEmployeeTransition statEmployeeTransition = new StatEmployeeTransition();
        statEmployeeTransition.setOrgId(orgId);
        statEmployeeTransition.setYear(year);
        statEmployeeTransition.setMonth(month);
        if (StringUtils.isEmpty(org.getNature())) {
            statEmployeeTransition.setOrgNature("0");
        } else {
            statEmployeeTransition.setOrgNature(org.getNature());
        }
        statEmployeeTransition.setOrgType(org.getType());
        statEmployeeTransition.setOrgCity(org.getCity());

        EmployeeJobRole employeeJobRole = jobRoleService.queryLast(employeeId);
        if (employeeJobRole == null) {
            return statEmployeeTransition;
        }


        if(StringUtils.isEmpty(employeeJobRole.getJobRole())){
            statEmployeeTransition.setJobRole("9999");
        }else{
            statEmployeeTransition.setJobRole(employeeJobRole.getJobRole());
        }

        if (StringUtils.isEmpty(employeeJobRole.getJobRoleNature())) {
            statEmployeeTransition.setJobRoleNature("0");
        } else {
            statEmployeeTransition.setJobRoleNature(employeeJobRole.getJobRoleNature());
        }

        if (StringUtils.isEmpty(employeeJobRole.getJobGrade())) {
            statEmployeeTransition.setJobGrade("0");
        } else {
            statEmployeeTransition.setJobGrade(employeeJobRole.getJobGrade());
        }
        statEmployeeTransition.setEmployeeEntryQuantity(0f);
        statEmployeeTransition.setEmployeeDestroyQuantity(0f);
        statEmployeeTransition.setEmployeeHolidayQuantity(0f);
        statEmployeeTransition.setEmployeeTransInQuantity(0f);
        statEmployeeTransition.setEmployeeTransOutQuantity(0f);
        statEmployeeTransition.setEmployeeBorrowInQuantity(0f);
        statEmployeeTransition.setEmployeeBorrowOutQuantity(0f);
        return statEmployeeTransition;
    }

    /**
     * 翻译异动员工姓名
     *
     * @param employeeId
     * @return
     */
    private String transEmployeeName(String employeeId) {
        if (StringUtils.isEmpty(employeeId)) {
            return "";
        }
        String[] employeeIds = employeeId.split(",");
        String employeeNames = "";
        for (int i = 0; i < employeeIds.length; i++) {
            employeeNames += employeeService.getEmployeeNameEmployeeId(Long.parseLong(employeeIds[i])) + ",";
        }
        return employeeNames.substring(0, employeeNames.length() - 1);
    }



    }
