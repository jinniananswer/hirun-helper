package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SecurityUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeTransitionStatDTO;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.entity.po.StatEmployeeTransition;
import com.microtomato.hirun.modules.organization.mapper.StatEmployeeTransitionMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.organization.service.IStatEmployeeTransitionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.system.entity.consts.FuncConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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

    @Override
    public StatEmployeeTransition queryTransitionRecord(Long orgId, LocalDate localDate) {
        String year = "";
        String month = "";
        if (localDate == null) {
            year = getYear();
            month = getMonth();
        } else {
            year = localDate.getYear() + "";
            month = localDate.getMonthValue() + "";
        }

        StatEmployeeTransition statEmployeeTransition = this.mapper.selectOne(new QueryWrapper<StatEmployeeTransition>().lambda()
                .eq(StatEmployeeTransition::getYear, year)
                .eq(StatEmployeeTransition::getOrgId, orgId)
                .eq(StatEmployeeTransition::getMonth, month));
        return statEmployeeTransition;


    }

    @Override
    public void addEmployeeEntryTransition(Long orgId, Long employeeId, LocalDate inDate) {
        StatEmployeeTransition statEmployeeTransitionNew = buildTransition(orgId, inDate);
        StatEmployeeTransition statEmployeeTransitionRecord = queryTransitionRecord(orgId, inDate);
        if (statEmployeeTransitionRecord == null) {
            statEmployeeTransitionNew.setEntryEmployeeId(employeeId + "");
            statEmployeeTransitionNew.setEmployeeEntryQuantity(1);
            this.mapper.insert(statEmployeeTransitionNew);
        } else {
            statEmployeeTransitionRecord.setEmployeeEntryQuantity(statEmployeeTransitionRecord.getEmployeeEntryQuantity() + 1);
            if(StringUtils.isEmpty(statEmployeeTransitionRecord.getEntryEmployeeId())){
                statEmployeeTransitionRecord.setEntryEmployeeId(employeeId+"");
            }else{
                statEmployeeTransitionRecord.setEntryEmployeeId(statEmployeeTransitionRecord.getEntryEmployeeId() + "," + employeeId);
            }
            this.mapper.updateById(statEmployeeTransitionRecord);
        }
    }

    @Override
    public void addEmployeeHolidayTransition(Long orgId, Long employeeId, LocalDate holidayDate) {
        StatEmployeeTransition statEmployeeTransitionNew = buildTransition(orgId, holidayDate);
        StatEmployeeTransition statEmployeeTransitionRecord = queryTransitionRecord(orgId, holidayDate);
        if (statEmployeeTransitionRecord == null) {
            statEmployeeTransitionNew.setHolidayEmployeeId(employeeId + "");
            statEmployeeTransitionNew.setEmployeeHolidayQuantity(1);
            this.mapper.insert(statEmployeeTransitionNew);
        } else {
            statEmployeeTransitionRecord.setEmployeeHolidayQuantity(statEmployeeTransitionRecord.getEmployeeHolidayQuantity() + 1);
            if(StringUtils.isEmpty(statEmployeeTransitionRecord.getHolidayEmployeeId())){
                statEmployeeTransitionRecord.setHolidayEmployeeId(employeeId+"");
            }else{
                statEmployeeTransitionRecord.setHolidayEmployeeId(statEmployeeTransitionRecord.getHolidayEmployeeId() + "," + employeeId);
            }
            this.mapper.updateById(statEmployeeTransitionRecord);
        }
    }

    @Override
    public void addEmployeeTransTransition(Long inOrgId, Long outOrgId, Long employeeId, LocalDate transDate) {
        StatEmployeeTransition transInTransition = buildTransition(inOrgId, transDate);
        StatEmployeeTransition transOutTransition = buildTransition(outOrgId, transDate);

        //处理转入部门的数据
        StatEmployeeTransition transInTransitionRecord = queryTransitionRecord(inOrgId, transDate);
        if (transInTransitionRecord == null) {
            transInTransition.setTransInEmployeeId(employeeId + "");
            transInTransition.setEmployeeTransInQuantity(1);
            this.mapper.insert(transInTransition);
        } else {
            transInTransitionRecord.setEmployeeTransInQuantity(transInTransitionRecord.getEmployeeTransInQuantity() + 1);
            transInTransitionRecord.setTransInEmployeeId(transInTransitionRecord.getTransInEmployeeId() + "," + employeeId);
            this.mapper.updateById(transInTransitionRecord);
        }
        //处理转出部门的数据
        StatEmployeeTransition transOutTransitionRecord = queryTransitionRecord(outOrgId, transDate);
        if (transOutTransitionRecord == null) {
            transOutTransition.setTransOutEmployeeId(employeeId + "");
            transOutTransition.setEmployeeTransOutQuantity(1);
            this.mapper.insert(transOutTransition);
        } else {
            transOutTransitionRecord.setEmployeeTransOutQuantity(transOutTransitionRecord.getEmployeeTransOutQuantity() + 1);
            transOutTransitionRecord.setTransOutEmployeeId(transOutTransitionRecord.getTransInEmployeeId() + "," + employeeId);
            this.mapper.updateById(transOutTransitionRecord);
        }
    }

    @Override
    public void addEmployeeBorrowTransition(Long inOrgId, Long outOrgId, Long employeeId, LocalDate borrowDate) {
        StatEmployeeTransition borrowInTransition = buildTransition(inOrgId, borrowDate);
        StatEmployeeTransition borrowOutTransition = buildTransition(outOrgId, borrowDate);

        //处理转入部门的数据
        StatEmployeeTransition borrowInTransitionRecord = queryTransitionRecord(inOrgId, borrowDate);
        if (borrowInTransitionRecord == null) {
            borrowInTransition.setBorrowInEmployeeId(employeeId + "");
            borrowInTransition.setEmployeeBorrowInQuantity(1);
            this.mapper.insert(borrowInTransition);
        } else {
            borrowInTransitionRecord.setEmployeeBorrowInQuantity(borrowInTransitionRecord.getEmployeeBorrowInQuantity() + 1);
            borrowInTransitionRecord.setBorrowInEmployeeId(borrowInTransitionRecord.getBorrowInEmployeeId() + "," + employeeId);
            this.mapper.updateById(borrowInTransitionRecord);
        }
        //处理转出部门的数据
        StatEmployeeTransition borrowOutTransitionRecord = queryTransitionRecord(outOrgId, borrowDate);
        if (borrowOutTransitionRecord == null) {
            borrowOutTransition.setBorrowOutEmployeeId(employeeId + "");
            borrowOutTransition.setEmployeeBorrowOutQuantity(1);
            this.mapper.insert(borrowOutTransition);
        } else {
            borrowOutTransitionRecord.setEmployeeBorrowOutQuantity(borrowOutTransitionRecord.getEmployeeBorrowOutQuantity() + 1);
            borrowOutTransitionRecord.setBorrowOutEmployeeId(borrowOutTransitionRecord.getEmployeeBorrowOutQuantity() + "," + employeeId);
            this.mapper.updateById(borrowOutTransitionRecord);
        }
    }

    @Override
    public void addEmployeeDestroyTransition(Long orgId, Long employeeId, LocalDate destroyDate) {
        StatEmployeeTransition statEmployeeTransitionNew = buildTransition(orgId, destroyDate);

        StatEmployeeTransition statEmployeeTransitionRecord = queryTransitionRecord(orgId, destroyDate);
        if (statEmployeeTransitionRecord == null) {
            statEmployeeTransitionNew.setDestroyEmployeeId(employeeId + "");
            statEmployeeTransitionNew.setEmployeeDestroyQuantity(1);
            this.mapper.insert(statEmployeeTransitionNew);
        } else {
            statEmployeeTransitionRecord.setEmployeeDestroyQuantity(statEmployeeTransitionRecord.getEmployeeDestroyQuantity() + 1);
            if(StringUtils.isEmpty(statEmployeeTransitionRecord.getDestroyEmployeeId())){
                statEmployeeTransitionRecord.setDestroyEmployeeId(employeeId+"");
            }else{
                statEmployeeTransitionRecord.setDestroyEmployeeId(statEmployeeTransitionRecord.getDestroyEmployeeId() + "," + employeeId);
            }
            this.mapper.updateById(statEmployeeTransitionRecord);
        }
    }

    @Override
    public List<EmployeeTransitionStatDTO> queryTransitionList(Long orgId, String queryTime) {
        String year = "";
        String month = "";
        if (StringUtils.isNotEmpty(queryTime)) {
            year = queryTime.split("-")[0];
            month = queryTime.split("-")[1];
        }

        UserContext userContext = WebContextUtils.getUserContext();
        Long userOrgId = userContext.getOrgId();
        OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, userOrgId);

        if (orgId == null) {
            if (SecurityUtils.hasFuncId(FuncConst.ALL_ORG)) {
                orgId = 122L;
            } else if (SecurityUtils.hasFuncId(FuncConst.ALL_CITY)) {
                orgId = 7L;
            } else if (SecurityUtils.hasFuncId(FuncConst.ALL_WOODEN)) {
                orgId = 9L;
            } else if (SecurityUtils.hasFuncId(FuncConst.ALL_SHOP)) {
                Org parentOrg = orgDO.findParent("2", orgService.listAllOrgs(), userOrgId);
                orgId = parentOrg.getOrgId();
            } else {
                orgId = userOrgId;
            }
        }

        String orgLine = orgDO.getOrgLine(orgId);

        List<StatEmployeeTransition> statEmployeeTransitionList = this.mapper.selectList(new QueryWrapper<StatEmployeeTransition>().lambda()
                .eq(StringUtils.isNotEmpty(year),StatEmployeeTransition::getYear, year)
                .eq(StringUtils.isNotEmpty(month),StatEmployeeTransition::getMonth, month)
                .in(StatEmployeeTransition::getOrgId, Arrays.asList(orgLine.split(","))));

        if (ArrayUtils.isEmpty(statEmployeeTransitionList)) {
            return new ArrayList<>();
        }

        List<EmployeeTransitionStatDTO> resultList = new ArrayList<>();
        for (StatEmployeeTransition employeeTransition : statEmployeeTransitionList) {
            EmployeeTransitionStatDTO dto = new EmployeeTransitionStatDTO();
            BeanUtils.copyProperties(employeeTransition, dto);
            OrgDO orgDo = SpringContextUtils.getBean(OrgDO.class, employeeTransition.getOrgId());
            dto.setOrgName(orgDo.getCompanyLinePath());
            dto.setEntryEmployeeName(transEmployeeName(employeeTransition.getEntryEmployeeId()));
            dto.setDestroyEmployeeName(transEmployeeName(employeeTransition.getDestroyEmployeeId()));
            dto.setHolidayEmployeeName(transEmployeeName(employeeTransition.getHolidayEmployeeId()));
            dto.setTransInEmployeeName(transEmployeeName(employeeTransition.getTransInEmployeeId()));
            dto.setTransOutEmployeeName(transEmployeeName(employeeTransition.getTransInEmployeeId()));
            dto.setBorrowInEmployeeName(transEmployeeName(employeeTransition.getBorrowInEmployeeId()));
            dto.setBorrowOutEmployeeName(transEmployeeName(employeeTransition.getBorrowOutEmployeeId()));
            resultList.add(dto);
        }
        return resultList;
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

    public StatEmployeeTransition buildTransition(Long orgId, LocalDate localDate) {

        String year = "";
        String month = "";
        if (localDate == null) {
            year = getYear();
            month = getMonth();
        } else {
            year = localDate.getYear() + "";
            month = localDate.getMonthValue() + "";
        }

        StatEmployeeTransition statEmployeeTransition = new StatEmployeeTransition();
        statEmployeeTransition.setOrgId(orgId);
        statEmployeeTransition.setYear(year);
        statEmployeeTransition.setMonth(month);
        statEmployeeTransition.setEmployeeEntryQuantity(0);
        statEmployeeTransition.setEmployeeDestroyQuantity(0);
        statEmployeeTransition.setEmployeeHolidayQuantity(0);
        statEmployeeTransition.setEmployeeTransInQuantity(0);
        statEmployeeTransition.setEmployeeTransOutQuantity(0);
        statEmployeeTransition.setEmployeeBorrowInQuantity(0);
        statEmployeeTransition.setEmployeeBorrowOutQuantity(0);
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
