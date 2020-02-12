package com.microtomato.hirun.modules.organization.service.impl;

import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.util.*;
import com.microtomato.hirun.modules.organization.entity.consts.OrgConst;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeePieStatisticDTO;
import com.microtomato.hirun.modules.organization.entity.dto.StatisticBarDTO;
import com.microtomato.hirun.modules.organization.entity.dto.StatisticBarValueDTO;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.mapper.EmployeeMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeStatisticService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 员工图表服务实现类
 * @author: jinnian
 * @create: 2019-11-21 16:47
 **/
@Slf4j
@Service
public class EmployeeStatisticServiceImpl implements IEmployeeStatisticService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IOrgService orgService;

    /**
     * 按性别统计员工数量
     * @return
     */
    @Override
    public List<EmployeePieStatisticDTO> countBySex(String orgIds) {

        if(StringUtils.isEmpty(orgIds)){
            orgIds = this.getSecurityOrgIds(null);
        }
        List<EmployeePieStatisticDTO> sexStatistics = this.employeeMapper.countBySex(orgIds);

        if (ArrayUtils.isNotEmpty(sexStatistics)) {
            for (EmployeePieStatisticDTO sexStatistic : sexStatistics) {
                sexStatistic.setName(this.staticDataService.getCodeName("SEX", sexStatistic.getName()) + ": " + sexStatistic.getNum());
            }
        }

        return sexStatistics;
    }

    /**
     * 按年龄段统计员工数量
     * @return
     */
    @Override
    public List<EmployeePieStatisticDTO> countByAge(String orgIds) {
        if(StringUtils.isEmpty(orgIds)){
            orgIds = this.getSecurityOrgIds(null);
        }
        List<EmployeePieStatisticDTO> ageStatistics = this.employeeMapper.countByAge(orgIds);
        if (ArrayUtils.isNotEmpty(ageStatistics)) {
            for (EmployeePieStatisticDTO ageStatistic : ageStatistics) {
                ageStatistic.setName(ageStatistic.getName() + ": " + ageStatistic.getNum());
            }
        }
        return ageStatistics;
    }

    @Override
    public List<EmployeePieStatisticDTO> countByJobRoleNature(String orgIds) {
        if(StringUtils.isEmpty(orgIds)){
            orgIds = this.getSecurityOrgIds(null);
        }
        List<EmployeePieStatisticDTO> jobRoleNatureStatistics = this.employeeMapper.countByJobRoleNature(orgIds);

        if (ArrayUtils.isNotEmpty(jobRoleNatureStatistics)) {
            for (EmployeePieStatisticDTO jobRoleNatureStatistic : jobRoleNatureStatistics) {
                if (StringUtils.isBlank(jobRoleNatureStatistic.getName())) {
                    jobRoleNatureStatistic.setName("无岗位性质: " + jobRoleNatureStatistic.getNum());
                } else {
                    jobRoleNatureStatistic.setName(this.staticDataService.getCodeName("JOB_NATURE", jobRoleNatureStatistic.getName())+": "+jobRoleNatureStatistic.getNum());
                }
            }
        }
        return jobRoleNatureStatistics;
    }

    @Override
    public List<EmployeePieStatisticDTO> countByCompanyAge(String orgIds) {
        if(StringUtils.isEmpty(orgIds)){
            orgIds = this.getSecurityOrgIds(null);
        }
        List<EmployeePieStatisticDTO> ageStatistics = this.employeeMapper.countByCompanyAge(orgIds);

        if (ArrayUtils.isNotEmpty(ageStatistics)) {
            for (EmployeePieStatisticDTO ageStatistic : ageStatistics) {
                ageStatistic.setName(ageStatistic.getName() + ": " + ageStatistic.getNum());
            }
        }
        return ageStatistics;
    }

    @Override
    public List<EmployeePieStatisticDTO> countByEducationLevel(String orgIds) {
        if(StringUtils.isEmpty(orgIds)){
            orgIds = this.getSecurityOrgIds(null);
        }
        List<EmployeePieStatisticDTO> educationLevelStatistics = this.employeeMapper.countByEducationLevel(orgIds);

        if (ArrayUtils.isNotEmpty(educationLevelStatistics)) {
            for (EmployeePieStatisticDTO educationLevelStatistic : educationLevelStatistics) {
                if (StringUtils.isBlank(educationLevelStatistic.getName())) {
                    educationLevelStatistic.setName("无学历: " + educationLevelStatistic.getNum());
                } else {
                    educationLevelStatistic.setName(this.staticDataService.getCodeName("EDUCATION_LEVEL", educationLevelStatistic.getName()) + ": " + educationLevelStatistic.getNum());
                }
            }
        }
        return educationLevelStatistics;
    }

    @Override
    public List<EmployeePieStatisticDTO> countByType(String orgIds) {
        if(StringUtils.isEmpty(orgIds)){
            orgIds = this.getSecurityOrgIds(null);
        }
        List<EmployeePieStatisticDTO> typeStatistics = this.employeeMapper.countByType(orgIds);

        if (ArrayUtils.isNotEmpty(typeStatistics)) {
            for (EmployeePieStatisticDTO typeStatistic : typeStatistics) {
                typeStatistic.setName(this.staticDataService.getCodeName("EMPLOYEE_TYPE", typeStatistic.getName()) + ": " + typeStatistic.getNum());
            }
        }
        return typeStatistics;
    }

    @Override
    public StatisticBarDTO countInAndDestroyOneYear() {
        StatisticBarDTO bar = new StatisticBarDTO();
        String chartTitle = this.getChartOrgTitle(null);
        bar.setTitle(chartTitle + "入职数与离职数对比");
        bar.setSubtitle("最近12个月");
        List<String> legend = new ArrayList<>();
        legend.add("入职数");
        legend.add("离职数");
        bar.setLegend(legend);

        List<String> months = new ArrayList<>();
        for (int i=11;i>=0;i--) {
            String month = TimeUtils.addMonths(TimeUtils.now(TimeUtils.DATE_FMT_3), TimeUtils.DATE_FMT_3, -i);
            month = month.substring(0,4) + month.substring(5,7);
            months.add(month);
        }
        bar.setXAxis(months);

        List<StatisticBarValueDTO> values = new ArrayList<>();
        StatisticBarValueDTO in = new StatisticBarValueDTO();
        StatisticBarValueDTO destroy = new StatisticBarValueDTO();

        String orgIds = this.getSecurityOrgIds(null);
        List<Integer> inNums = this.employeeMapper.countInOneYear(orgIds);
        List<Integer> destroyNums = this.employeeMapper.countDestroyOneYear(orgIds);
        in.setName("入职数");
        in.setData(inNums);

        destroy.setName("离职数");
        destroy.setData(destroyNums);

        values.add(in);
        values.add(destroy);

        bar.setYAxis(values);
        return bar;
    }

    private String getSecurityOrgIds(Long orgId) {
        OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class);
        String orgIds = "";
        if (orgId == null) {
            List<Org> securityOrgs = this.orgService.listOrgsSecurity();
            for (Org org : securityOrgs) {
                orgIds += org.getOrgId() + ",";
            }
            orgIds = orgIds.substring(0, orgIds.length() - 1);
        } else {
            orgIds = orgDO.getOrgLine(orgId);
        }

        return orgIds;
    }

    @Override
    public String getChartOrgTitle(Long orgId) {
        if (orgId == null) {
            List<Org> orgs = this.orgService.listOrgsSecurity();
            if (SecurityUtils.hasFuncId(OrgConst.SECURITY_ALL_ORG)) {
                return "鸿扬家居";
            } else if (SecurityUtils.hasFuncId(OrgConst.SECURITY_ALL_BU)) {
                return "所有事业部";
            } else if (SecurityUtils.hasFuncId(OrgConst.SECURITY_ALL_SUB_COMPANY)) {
                return "所有分公司";
            } else if (SecurityUtils.hasFuncId(OrgConst.SECURITY_ALL_COMANY_SHOP)) {
                return "所有门店";
            } else {
                orgId = orgs.get(0).getOrgId();
            }
        }

        OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
        return orgDO.getCompanyLinePath();
    }
}
