package com.microtomato.hirun.modules.bss.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.bss.plan.entity.dto.AgentMonthPlanDTO;
import com.microtomato.hirun.modules.bss.plan.entity.dto.AgentMonthPlanQueryDTO;
import com.microtomato.hirun.modules.bss.plan.entity.po.PlanAgentMonth;
import com.microtomato.hirun.modules.bss.plan.mapper.PlanAgentMonthMapper;
import com.microtomato.hirun.modules.bss.plan.service.IPlanAgentMonthService;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryMonthlyQueryDTO;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * (PlanAgentMonth)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-06-30 00:13:31
 */
@Service
@Slf4j
@DataSource(DataSourceKey.INS)

public class PlanAgentMonthServiceImpl extends ServiceImpl<PlanAgentMonthMapper, PlanAgentMonth> implements IPlanAgentMonthService {

    @Autowired
    private PlanAgentMonthMapper planAgentMonthMapper;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IOrgService orgService;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    /**
     * 查询客户代表计划数据
     * @param param
     * @return
     */
    @Override
    public List<AgentMonthPlanDTO> queryAgentPlan(AgentMonthPlanQueryDTO param) {
        //如果前台传入的orgId为空，根据查询权限加载orgId
        if (ArrayUtils.isEmpty(param.getOrgIds())) {
            List<Org> orgs = orgService.listOrgsSecurity();
            List<Long> orgIds = new ArrayList<>();
            if (ArrayUtils.isNotEmpty(orgs)) {
                for (Org org : orgs) {
                    orgIds.add(org.getOrgId());
                }
                param.setOrgIds(orgIds);
            }
        }

        QueryWrapper<SalaryMonthlyQueryDTO> wrapper = new QueryWrapper<>();
        wrapper.apply("c.org_id = b.org_id and a.user_id = e.user_id " +
                " and e.role_id in ('15','16','17','18','19') and e.end_date > now() and a.status='0'  ")
                .like(StringUtils.isNotBlank(param.getName()), "a.name", param.getName())
                .in(ArrayUtils.isNotEmpty(param.getOrgIds()), "b.org_id", param.getOrgIds());

        List<AgentMonthPlanDTO> plans = this.planAgentMonthMapper.queryAgentPlan(wrapper, param.getMonth());

        if (ArrayUtils.isNotEmpty(plans)) {
            for (AgentMonthPlanDTO dto : plans) {
                if (param.getMonth() != null) {
                    dto.setMonth(param.getMonth());
                }
                if (dto.getOrgId() != null) {
                    OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, dto.getOrgId());
                    dto.setOrgPath(orgDO.getCompanyLinePath());
                }
                dto.setJobRoleName(this.staticDataService.getCodeName("JOB_ROLE", dto.getJobRole()));
            }
        }

        return plans;
    }

    /**
     * 保存客户代表计划数据
     * @param dtoList
     */
    @Override
    public void saveAgentPlan(List<AgentMonthPlanDTO> dtoList) {
        if (ArrayUtils.isEmpty(dtoList)) {
            return;
        }
        Integer month = dtoList.get(0).getMonth();
        List<PlanAgentMonth> createPlanList = new ArrayList<>();
        List<PlanAgentMonth> modifyPlanList = new ArrayList<>();

        for (AgentMonthPlanDTO planDto : dtoList) {
            if (planDto.getId() == null) {
                PlanAgentMonth planAgentMonth = new PlanAgentMonth();
                BeanUtils.copyProperties(planDto, planAgentMonth);
                this.buildShopAndCompanyData(planAgentMonth, planDto.getOrgId());
                createPlanList.add(planAgentMonth);
                continue;
            } else {
                PlanAgentMonth existPlan = this.getById(planDto.getId());
                if (existPlan == null) {
                    return;
                }
                this.transPlanDto2Plan(planDto, existPlan);
                EmployeeJobRole employeeJobRole=employeeJobRoleService.queryValidMain(existPlan.getEmployeeId());
                this.buildShopAndCompanyData(existPlan,employeeJobRole.getOrgId());
                modifyPlanList.add(existPlan);
            }
        }

        if (ArrayUtils.isNotEmpty(createPlanList)) {
            this.saveBatch(createPlanList);
        }

        if (ArrayUtils.isNotEmpty(modifyPlanList)) {
            this.updateBatchById(modifyPlanList);
        }
    }

    @Override
    public PlanAgentMonth queryAgentPlanByEmployeeId(Long employeeId, Integer month) {
        return this.baseMapper.selectOne(new QueryWrapper<PlanAgentMonth>().lambda()
                .eq(PlanAgentMonth::getEmployeeId,employeeId).eq(PlanAgentMonth::getMonth,month));
    }

    @Override
    public PlanAgentMonth queryAgentPlanByShopId(Long orgId, Integer month) {
        PlanAgentMonth planAgentMonth=this.baseMapper.queryAgentPlanByShopId(orgId,month);
        return planAgentMonth;
    }

    @Override
    public PlanAgentMonth queryAgentPlanByCompanyId(Long orgId, Integer month) {
        return this.baseMapper.queryAgentPlanByCompanyId(orgId,month);
    }

    /**
     * 变更时设置新的计划数据
     * @param planDTO
     * @param planAgentMonth
     */
    private void transPlanDto2Plan(AgentMonthPlanDTO planDTO, PlanAgentMonth planAgentMonth) {
        if (planDTO.getPlanConsultCount() != null) {
            planAgentMonth.setPlanConsultCount(planDTO.getPlanConsultCount());
        }
        if (planDTO.getPlanStyleCount() != null) {
            planAgentMonth.setPlanStyleCount(planDTO.getPlanStyleCount());
        }
        if (planDTO.getPlanFuncaCount() != null) {
            planAgentMonth.setPlanFuncaCount(planDTO.getPlanFuncaCount());
        }
        if (planDTO.getPlanFuncbCount() != null) {
            planAgentMonth.setPlanFuncbCount(planDTO.getPlanFuncbCount());
        }
        if (planDTO.getPlanFunccCount() != null) {
            planAgentMonth.setPlanFunccCount(planDTO.getPlanFunccCount());
        }
        if (planDTO.getPlanCitycabinCount() != null) {
            planAgentMonth.setPlanCitycabinCount(planDTO.getPlanCitycabinCount());
        }
        if (planDTO.getPlanMeasureCount() != null) {
            planAgentMonth.setPlanMeasureCount(planDTO.getPlanMeasureCount());
        }
        if (planDTO.getPlanBindDesignCount() != null) {
            planAgentMonth.setPlanBindDesignCount(planDTO.getPlanBindDesignCount());
        }
        if (planDTO.getPlanBindAgentCount() != null) {
            planAgentMonth.setPlanBindAgentCount(planDTO.getPlanBindAgentCount());
        }
    }

    /**
     * 构造计划中的shopId和companyId数据
     *
     * @param planAgentMonth
     * @param orgId
     */
    private void buildShopAndCompanyData(PlanAgentMonth planAgentMonth, Long orgId) {
        if (orgId == null) {
            return;
        }
        OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
        planAgentMonth.setShopId(orgDO.getBelongShop().getOrgId());
        planAgentMonth.setCompanyId(orgDO.getBelongCompany().getOrgId());
    }
}