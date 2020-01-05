package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.cases.AlreadyExistException;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SecurityUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.organization.entity.consts.OrgConst;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeInfoDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeePerformanceInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeePerformance;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.mapper.EmployeePerformanceMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeePerformanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgHrRelService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.system.entity.consts.FuncConst;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2019-11-20
 */
@Slf4j
@Service
public class EmployeePerformanceServiceImpl extends ServiceImpl<EmployeePerformanceMapper, EmployeePerformance> implements IEmployeePerformanceService {

    @Autowired
    private EmployeePerformanceMapper mapper;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IOrgHrRelService orgHrRelService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IOrgService orgService;

    @Override
    public IPage<EmployeePerformanceInfoDTO> queryPerformanceList(String name, String orgSet, String year, String performance, String jobRoleNature, Page<EmployeePerformanceInfoDTO> page) {
        QueryWrapper queryWrapper = bulidQueryCondition(name, orgSet, year, performance, jobRoleNature);

        if (StringUtils.isEmpty(year)) {
            Calendar date = Calendar.getInstance();
            year = String.valueOf(date.get(Calendar.YEAR));
        }

        IPage<EmployeePerformanceInfoDTO> infoDTOIPage = mapper.selectEmployeePerformancePage(page, queryWrapper, year);

        if (infoDTOIPage.getRecords().size() <= 0) {
            return infoDTOIPage;
        }

        for (EmployeePerformanceInfoDTO dto : infoDTOIPage.getRecords()) {
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, dto.getOrgId());
            dto.setOrgPath(orgDO.getCompanyLinePath());
            dto.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", dto.getJobRole()));
            dto.setJobRoleNatureName(staticDataService.getCodeName("JOB_NATURE", dto.getJobRoleNature()));
            dto.setPerformanceName(staticDataService.getCodeName("PERFORMANCE_LEVEL", dto.getPerformance()));
        }

        return infoDTOIPage;
    }

    @Override
    public void addEmployeePerformance(EmployeePerformance employeePerformance) {
        List<EmployeePerformance> list = this.queryEmployeePerformance(employeePerformance.getEmployeeId(), employeePerformance.getYear());
        if (list.size() > 0) {
            throw new AlreadyExistException("该员工已存在该年度绩效，请进行修改！", ErrorKind.ALREADY_EXIST.getCode());
        }
        employeePerformance.setStatus("1");
        mapper.insert(employeePerformance);
    }

    @Override
    public boolean updateEmployeePerformance(EmployeePerformance employeePerformance) {
        if (employeePerformance.getId() == null) {
            this.addEmployeePerformance(employeePerformance);
            return true;
        }

        int result = mapper.updateById(employeePerformance);
        if (result <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<EmployeePerformance> queryEmployeePerformance(Long employeeId, String year) {
        LambdaQueryWrapper<EmployeePerformance> lambdaQueryWrapper = Wrappers.<EmployeePerformance>lambdaQuery();
        lambdaQueryWrapper.eq(EmployeePerformance::getEmployeeId, employeeId).eq(EmployeePerformance::getYear, year);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public List<EmployeePerformanceInfoDTO> queryPerformanceList(String name, String orgSet, String year, String performance, String jobRoleNature) {
        QueryWrapper queryWrapper = bulidQueryCondition(name, orgSet, year, performance, jobRoleNature);
        List<EmployeePerformanceInfoDTO> list = mapper.queryEmployeePerformance(queryWrapper, year);
        if (list.size() <= 0) {
            return list;
        }

        for (EmployeePerformanceInfoDTO dto : list) {
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, dto.getOrgId());
            dto.setOrgPath(orgDO.getCompanyLinePath());
            dto.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", dto.getJobRole()));
            dto.setJobRoleNatureName(staticDataService.getCodeName("JOB_NATURE", dto.getJobRoleNature()));
        }
        return list;
    }

    private QueryWrapper bulidQueryCondition(String name, String orgSet, String year, String performance, String jobRoleNature) {
        QueryWrapper<EmployeePerformanceInfoDTO> queryWrapper = new QueryWrapper<>();

        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId = userContext.getEmployeeId();
        String employeeIds = "";

        if (StringUtils.isEmpty(orgSet)) {
            orgSet = orgService.listOrgSecurityLine();
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
                employeeIds = employeeId + "";
            } else {
                for (EmployeeInfoDTO employee : employeeList) {
                    employeeIds += employee.getEmployeeId() + ",";
                }
                employeeIds = employeeIds + employeeId;
            }
            queryWrapper.apply("a.employee_id in (" + employeeIds + ")");
        }

        queryWrapper.apply(StringUtils.isNotEmpty(orgSet), "b.org_id in (" + orgSet + ")");
        queryWrapper.eq(StringUtils.isNotEmpty(performance), "d.performance", performance);
        queryWrapper.eq(StringUtils.isNotEmpty(jobRoleNature), "b.job_role_nature", jobRoleNature);
        queryWrapper.like(StringUtils.isNotEmpty(name), "a.name", name);
        queryWrapper.apply("b.employee_id=a.employee_id and b.is_main='1' AND (now() between b.start_date and b.end_date) and b.is_main = '1' AND c.org_id = b.org_id and a.status='0' ");
        queryWrapper.orderByDesc("d.create_time");
        queryWrapper.orderByDesc("b.org_id");
        return queryWrapper;
    }
}
