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
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeePerformanceInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.EmployeePerformance;
import com.microtomato.hirun.modules.organization.mapper.EmployeePerformanceMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeePerformanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.organization.service.IOrgHrRelService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

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

    @Override
    public IPage<EmployeePerformanceInfoDTO> queryPerformanceList(String name, String orgSet, String year,String performance,String jobRoleNature, Page<EmployeePerformanceInfoDTO> page) {
        QueryWrapper queryWrapper=bulidQueryCondition(name,orgSet,year,performance,jobRoleNature);

        IPage<EmployeePerformanceInfoDTO> infoDTOIPage = mapper.selectEmployeePerformancePage(page, queryWrapper,year);

        if (infoDTOIPage.getRecords().size() <= 0) {
            return infoDTOIPage;
        }

        for (EmployeePerformanceInfoDTO dto : infoDTOIPage.getRecords()) {
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, dto.getOrgId());
            dto.setOrgPath(orgDO.getCompanyLinePath());
            dto.setJobRoleName(staticDataService.getCodeName("JOB_ROLE", dto.getJobRole()));
            dto.setJobRoleNatureName(staticDataService.getCodeName("JOB_NATURE", dto.getJobRoleNature()));
            dto.setPerformanceName(staticDataService.getCodeName("PERFORMANCE_LEVEL",dto.getPerformance()));
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
        if(employeePerformance.getId()==null){
            this.addEmployeePerformance(employeePerformance);
            return true;
        }

        int result=mapper.updateById(employeePerformance);
        if(result <=0){
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
    public List<EmployeePerformanceInfoDTO> queryPerformanceList(String name, String orgSet, String year,String performance,String jobRoleNature) {
        QueryWrapper queryWrapper=bulidQueryCondition(name,orgSet,year,performance,jobRoleNature);
        List<EmployeePerformanceInfoDTO> list=mapper.queryEmployeePerformance(queryWrapper,year);
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

    private QueryWrapper bulidQueryCondition(String name,String orgSet,String year,String performance,String jobRoleNature){
        QueryWrapper<EmployeePerformanceInfoDTO> queryWrapper = new QueryWrapper<>();

            UserContext userContext = WebContextUtils.getUserContext();
            Long orgId = userContext.getOrgId();
            Long employeeId = userContext.getEmployeeId();
            String employeeIds = "";
            if (SecurityUtils.hasFuncId("ALL_CITY")) {

            } else if (SecurityUtils.hasFuncId("ALL_SHOP")) {
                OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
                String orgLine = orgDO.getOrgLine();
                queryWrapper.apply("b.org_id in (" + orgLine + ")");
            } else if (StringUtils.isNotBlank(orgHrRelService.getOrgLine(employeeId))) {
                queryWrapper.apply("b.org_id in (" + orgHrRelService.getOrgLine(employeeId) + ")");
            } else {
                List<Employee> employeeList = employeeService.findSubordinate(employeeId);
                if (ArrayUtils.isEmpty(employeeList)) {
                    queryWrapper.eq("a.employee_id",employeeId);
                }
                for (Employee employee : employeeList) {
                    employeeIds += employee.getEmployeeId() + ",";
                    queryWrapper.apply("a.employee_id in ("+employeeIds+")");
                }

            }

        queryWrapper.apply(StringUtils.isNotEmpty(orgSet),"b.org_id in (" + orgSet + ")");
        queryWrapper.eq(StringUtils.isNotEmpty(performance),"d.performance",performance);
        queryWrapper.eq(StringUtils.isNotEmpty(jobRoleNature),"b.job_role_nature",jobRoleNature);
        queryWrapper.like(StringUtils.isNotEmpty(name), "a.name", name);
        queryWrapper.apply("b.employee_id=a.employee_id AND (now() between b.start_date and b.end_date) AND c.org_id = b.org_id and a.status='0' ");
        queryWrapper.orderByDesc("d.create_time");
        queryWrapper.orderByDesc("b.org_id");
        return queryWrapper;
    }
}
