package com.microtomato.hirun.modules.bss.salary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryFixDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryFixQueryDTO;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryFix;
import com.microtomato.hirun.modules.bss.salary.mapper.SalaryFixMapper;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryFixService;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 员工固定工资表(SalaryFix)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 00:26:29
 */
@Service
@DataSource(DataSourceKey.INS)
@Slf4j
public class SalaryFixServiceImpl extends ServiceImpl<SalaryFixMapper, SalaryFix> implements ISalaryFixService {

    @Autowired
    private SalaryFixMapper salaryFixMapper;

    @Autowired
    private IStaticDataService staticDataService;

    /**
     * 查询员工月工资
     * @param param
     * @return
     */
    @Override
    public List<SalaryFixDTO> queryEmployeeSalaries(SalaryFixQueryDTO param) {
        List<Long> orgs = new ArrayList<>();
        if (StringUtils.isNotBlank(param.getOrgIds())) {
            String[] orgIdArray = param.getOrgIds().split(",");
            for (String s : orgIdArray) {
                orgs.add(Long.parseLong(s));
            }
        } else {
            Long orgId = WebContextUtils.getUserContext().getOrgId();
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
            Org company = orgDO.getBelongCompany();
            if (company != null) {
                orgDO = SpringContextUtils.getBean(OrgDO.class, company.getOrgId());
                orgs = orgDO.getChildrenIds(true);
            }
        }

        QueryWrapper<SalaryFixDTO> wrapper = new QueryWrapper<>();
        wrapper.apply("c.org_id = b.org_id ")
                .like(StringUtils.isNotBlank(param.getName()), "a.name", param.getName())
                .eq(StringUtils.isNotBlank(param.getMobileNo()), "a.mobile_no", param.getMobileNo())
                .in(ArrayUtils.isNotEmpty(orgs), "b.org_id", orgs)
                .eq(StringUtils.isNotBlank(param.getType()), "a.type", param.getType())
                .eq(StringUtils.isNotBlank(param.getAuditStatus()), "d.audit_status", param.getAuditStatus())
                .eq(StringUtils.isNotBlank(param.getStatus()), "a.status", param.getStatus());

        List<SalaryFixDTO> salaries = this.salaryFixMapper.queryFixSalaries(wrapper);

        if (ArrayUtils.isNotEmpty(salaries)) {
            salaries.forEach((salary) -> {
                if (salary.getOrgId() != null) {
                    OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, salary.getOrgId());
                    salary.setOrgPath(orgDO.getCompanyLinePath());
                }
                salary.setTypeName(this.staticDataService.getCodeName("EMPLOYEE_TYPE", salary.getType()));
                salary.setJobRoleName(this.staticDataService.getCodeName("JOB_ROLE", salary.getJobRole()));
                salary.setStatusName(this.staticDataService.getCodeName("EMPLOYEE_STATUS", salary.getStatus()));
                salary.setAuditStatusName(this.staticDataService.getCodeName("SALARY_AUDIT_STATUS", salary.getAuditStatus()));
            });
        }
        return salaries;
    }

    /**
     * 查询员工固定工资项目审核的数据
     * @param param
     * @return
     */
    @Override
    public List<SalaryFixDTO> queryAuditEmployeeSalaries(SalaryFixQueryDTO param) {
        List<Long> orgs = new ArrayList<>();
        if (StringUtils.isNotBlank(param.getOrgIds())) {
            String[] orgIdArray = param.getOrgIds().split(",");
            for (String s : orgIdArray) {
                orgs.add(Long.parseLong(s));
            }
        } else {
            Long orgId = WebContextUtils.getUserContext().getOrgId();
            OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
            Org company = orgDO.getBelongCompany();
            if (company != null) {
                orgDO = SpringContextUtils.getBean(OrgDO.class, company.getOrgId());
                orgs = orgDO.getChildrenIds(true);
            }
        }

        QueryWrapper<SalaryFixDTO> wrapper = new QueryWrapper<>();
        wrapper.apply("c.org_id = b.org_id ")
                .apply("d.employee_id = a.employee_id ")
                .apply("d.end_time > now() ")
                .like(StringUtils.isNotBlank(param.getName()), "a.name", param.getName())
                .eq(StringUtils.isNotBlank(param.getMobileNo()), "a.mobile_no", param.getMobileNo())
                .in(ArrayUtils.isNotEmpty(orgs), "b.org_id", orgs)
                .eq(StringUtils.isNotBlank(param.getType()), "a.type", param.getType())
                .eq(StringUtils.isNotBlank(param.getAuditStatus()), "d.audit_status", param.getAuditStatus())
                .eq(StringUtils.isNotBlank(param.getStatus()), "a.status", param.getStatus());

        List<SalaryFixDTO> salaries = this.salaryFixMapper.queryAuditFixSalaries(wrapper);

        if (ArrayUtils.isNotEmpty(salaries)) {
            salaries.forEach((salary) -> {
                if (salary.getOrgId() != null) {
                    OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, salary.getOrgId());
                    salary.setOrgPath(orgDO.getCompanyLinePath());
                }
                salary.setTypeName(this.staticDataService.getCodeName("EMPLOYEE_TYPE", salary.getType()));
                salary.setJobRoleName(this.staticDataService.getCodeName("JOB_ROLE", salary.getJobRole()));
                salary.setStatusName(this.staticDataService.getCodeName("EMPLOYEE_STATUS", salary.getStatus()));
                salary.setAuditStatusName(this.staticDataService.getCodeName("SALARY_AUDIT_STATUS", salary.getAuditStatus()));
            });
        }
        return salaries;
    }

    /**
     * 查询所有有效的员工固定工资项目
     * @return
     */
    @Override
    public List<SalaryFix> queryAllValid() {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(new QueryWrapper<SalaryFix>().lambda()
                .ge(SalaryFix::getEndTime, now));
    }

    /**
     * 查询所有有效的员工固定工资项目
     * @return
     */
    @Override
    public List<SalaryFix> queryAllValidAudit() {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(new QueryWrapper<SalaryFix>().lambda()
                .ge(SalaryFix::getEndTime, now)
                .eq(SalaryFix::getAuditStatus, "2"));
    }

    /**
     * 保存员工月工资固定项目
     * @param salaries
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void saveSalaries(List<SalaryFixDTO> salaries, boolean isAudit) {
        if (ArrayUtils.isEmpty(salaries)) {
            return;
        }

        List<SalaryFix> employeeSalaries = this.queryAllValid();

        List<SalaryFix> createEmployeeSalaries = new ArrayList<>();
        List<SalaryFix> modifyEmployeeSalaries = new ArrayList<>();
        LocalDateTime now = RequestTimeHolder.getRequestTime();

        salaries.forEach((salary) -> {
            if (salary.getId() == null) {
                //ID为空，表示为新创建的数据
                if (this.isEmpty(salary)) {
                    return;
                }
                SalaryFix employeeSalary = this.fillNewSalary(salary, isAudit);
                createEmployeeSalaries.add(employeeSalary);
            } else {
                //ID不为空，表示为修改后的数据
                SalaryFix employeeSalary = this.findById(employeeSalaries, salary.getId());
                if (employeeSalary == null) {
                    return;
                }

                if (this.equals(salary, employeeSalary)) {
                    //没有发生变化，则不做处理
                    return;
                }

                String auditStatus = employeeSalary.getAuditStatus();
                if (StringUtils.equals("0", auditStatus)) {
                    //如果还未提交审核的数据，则保存的话直接修改原记录
                    this.moneyUnitTransfer(salary, employeeSalary);
                    if (isAudit) {
                        //状态修改为待审核
                        employeeSalary.setAuditStatus("1");
                    }
                    modifyEmployeeSalaries.add(employeeSalary);
                } else if (StringUtils.equals("3", auditStatus)) {
                    //审核不通过后的修改，则终止原来的记录，保存轨迹数据
                    employeeSalary.setEndTime(now);
                    modifyEmployeeSalaries.add(employeeSalary);
                    salary.setId(null);
                    SalaryFix newEmployeeSalary = this.fillNewSalary(salary, isAudit);
                    createEmployeeSalaries.add(newEmployeeSalary);
                }
            }
        });

        if (ArrayUtils.isNotEmpty(createEmployeeSalaries)) {
            this.saveBatch(createEmployeeSalaries);
        }

        if (ArrayUtils.isNotEmpty(modifyEmployeeSalaries)) {
            this.updateBatchById(modifyEmployeeSalaries);
        }
    }

    /**
     * 审核员工工资固定项目
     * @param employeeSalaries
     * @param isPass
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void audit(List<SalaryFixDTO> employeeSalaries, boolean isPass) {
        if (ArrayUtils.isEmpty(employeeSalaries)) {
            return;
        }
        List<SalaryFix> modifySalaryFixes = new ArrayList<>();
        List<SalaryFix> all = this.queryAllValid();

        employeeSalaries.forEach((employeeSalary -> {
            Long id = employeeSalary.getId();
            if (id != null) {
                SalaryFix SalaryFix = this.findById(all, id);
                if (SalaryFix != null) {
                    if (isPass) {
                        SalaryFix.setAuditStatus("2");
                    } else {
                        SalaryFix.setAuditStatus("3");
                        SalaryFix.setAuditRemark(employeeSalary.getAuditRemark());
                    }

                    modifySalaryFixes.add(SalaryFix);
                }
            }
        }));

        if (ArrayUtils.isNotEmpty(modifySalaryFixes)) {
            this.updateBatchById(modifySalaryFixes);
        }
    }

    /**
     * 根据给定的ID在列表中查找符合的数据
     * @param salaries
     * @param id
     * @return
     */
    private SalaryFix findById(List<SalaryFix> salaries, Long id) {
        if (ArrayUtils.isEmpty(salaries)) {
            return null;
        }

        for (SalaryFix salary : salaries) {
            if (id.equals(salary.getId())) {
                return salary;
            }
        }
        return null;
    }

    /**
     * 填充新创建的员工工资数据
     * @param salary
     * @return
     */
    private SalaryFix fillNewSalary(SalaryFixDTO salary, boolean isAudit) {
        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId = userContext.getEmployeeId();
        LocalDateTime now = RequestTimeHolder.getRequestTime();

        SalaryFix employeeSalary = new SalaryFix();
        BeanUtils.copyProperties(salary, employeeSalary);
        employeeSalary.setStartTime(now);
        employeeSalary.setEndTime(TimeUtils.getForeverTime());
        employeeSalary.setCreateEmployeeId(employeeId);
        if (isAudit) {
            employeeSalary.setAuditStatus("1");
        } else {
            employeeSalary.setAuditStatus("0");
        }
        this.moneyUnitTransfer(salary, employeeSalary);
        return employeeSalary;
    }

    /**
     * 各项费用的double到Long型的转换
     * @param salary
     * @param dto
     */
    private void moneyUnitTransfer(SalaryFixDTO dto, SalaryFix salary) {
        if (dto.getBasic() != null) {
            salary.setBasic(new Long(Math.round(dto.getBasic() * 100)));
        }

        if (dto.getRank() != null) {
            salary.setRank(new Long(Math.round(dto.getRank() * 100)));
        }

        if (dto.getPerformance() != null) {
            salary.setPerformance(new Long(Math.round(dto.getPerformance() * 100)));
        }

        if (dto.getDuty() != null) {
            salary.setDuty(new Long(Math.round(dto.getDuty() * 100)));
        }

        if (dto.getOvertime() != null) {
            salary.setOvertime(new Long(Math.round(dto.getOvertime() * 100)));
        }

        if (dto.getFloatAward() != null) {
            salary.setFloatAward(new Long(Math.round(dto.getFloatAward() * 100)));
        }

        if (dto.getOther() != null) {
            salary.setOther(new Long(Math.round(dto.getOther() * 100)));
        }

        if (dto.getBackPay() != null) {
            salary.setBackPay(new Long(Math.round(dto.getBackPay() * 100)));
        }

        if (dto.getMedical() != null) {
            salary.setMedical(new Long(Math.round(dto.getMedical() * 100)));
        }

        if (dto.getOverage() != null) {
            salary.setOverage(new Long(Math.round(dto.getOverage() * 100)));
        }

        if (dto.getUnemployment() != null) {
            salary.setUnemployment(new Long(Math.round(dto.getUnemployment() * 100)));
        }

        if (dto.getSeriousIll() != null) {
            salary.setSeriousIll(new Long(Math.round(dto.getSeriousIll() * 100)));
        }

        if (dto.getTax() != null) {
            salary.setTax(new Long(Math.round(dto.getTax() * 100)));
        }
    }

    /**
     * 是否没有填入数据，没有填入任何金额的数据是不需要入库的
     * @param dto
     * @return
     */
    private boolean isEmpty(SalaryFixDTO dto) {
        if (dto.getBasic() == null &&
                dto.getRank() == null &&
                dto.getPerformance() == null &&
                dto.getDuty() == null &&
                dto.getOvertime() == null &&
                dto.getFloatAward() == null &&
                dto.getOther() == null &&
                dto.getBackPay() == null &&
                dto.getMedical() == null &&
                dto.getOverage() == null &&
                dto.getUnemployment() == null &&
                dto.getSeriousIll() == null &&
                dto.getTax() == null &&
                dto.getBankAcctOne() == null &&
                dto.getBankAcctTwo() == null &&
                dto.getBankAcctThree() == null) {
            return true;
        }

        return false;
    }

    /**
     * 检查工资数据的关键信息是否有变动
     * @param dto
     * @param salary
     * @return
     */
    private boolean equals(SalaryFixDTO dto, SalaryFix salary) {
        if (salary.getId() != null && !salary.getId().equals(dto.getId())) {
            return false;
        }

        if (!salary.getEmployeeId().equals(dto.getEmployeeId())) {
            return false;
        }
        SalaryFix tempSalary = new SalaryFix();
        BeanUtils.copyProperties(dto, tempSalary);
        this.moneyUnitTransfer(dto, tempSalary);
        this.setDefaultValue(tempSalary);

        SalaryFix copySalary = new SalaryFix();
        BeanUtils.copyProperties(salary, copySalary);
        this.setDefaultValue(copySalary);

        if (!copySalary.getBasic().equals(tempSalary.getBasic()) ||
                !copySalary.getRank().equals(tempSalary.getRank()) ||
                !copySalary.getPerformance().equals(tempSalary.getPerformance()) ||
                !copySalary.getDuty().equals(tempSalary.getDuty()) ||
                !copySalary.getOvertime().equals(tempSalary.getOvertime()) ||
                !copySalary.getFloatAward().equals(tempSalary.getFloatAward()) ||
                !copySalary.getOther().equals(tempSalary.getOther()) ||
                !copySalary.getBackPay().equals(tempSalary.getBackPay()) ||
                !copySalary.getMedical().equals(tempSalary.getMedical()) ||
                !copySalary.getOverage().equals(tempSalary.getOverage()) ||
                !copySalary.getUnemployment().equals(tempSalary.getUnemployment()) ||
                !copySalary.getSeriousIll().equals(tempSalary.getSeriousIll()) ||
                !copySalary.getTax().equals(tempSalary.getTax()) ||
                !copySalary.getBankAcctOne().equals(tempSalary.getBankAcctOne()) ||
                !copySalary.getBankAcctTwo().equals(tempSalary.getBankAcctTwo()) ||
                !copySalary.getBankAcctThree().equals(tempSalary.getBankAcctThree())) {
            return false;
        }

        return true;
    }

    /**
     * 将为Null值的设置为0
     * @param salary
     */
    private void setDefaultValue(SalaryFix salary) {
        if (salary.getBasic() == null) {
            salary.setBasic(0L);
        }

        if (salary.getRank() == null) {
            salary.setRank(0L);
        }

        if (salary.getPerformance() == null) {
            salary.setPerformance(0L);
        }

        if (salary.getDuty() == null) {
            salary.setDuty(0L);
        }

        if (salary.getOvertime() == null) {
            salary.setOvertime(0L);
        }

        if (salary.getFloatAward() == null) {
            salary.setFloatAward(0L);
        }

        if (salary.getOther() == null) {
            salary.setOther(0L);
        }

        if (salary.getBackPay() == null) {
            salary.setBackPay(0L);
        }

        if (salary.getMedical() == null) {
            salary.setMedical(0L);
        }

        if (salary.getOverage() == null) {
            salary.setOverage(0L);
        }

        if (salary.getUnemployment() == null) {
            salary.setUnemployment(0L);
        }

        if (salary.getSeriousIll() == null) {
            salary.setSeriousIll(0L);
        }

        if (salary.getTax() == null) {
            salary.setTax(0L);
        }

        if (salary.getBankAcctOne() == null) {
            salary.setBankAcctOne("");
        }

        if (salary.getBankAcctTwo() == null) {
            salary.setBankAcctTwo("");
        }

        if (salary.getBankAcctThree() == null) {
            salary.setBankAcctThree("");
        }
    }

    @Override
    public SalaryFix getByEmployeeId(Long employeeId) {
        return this.getOne(Wrappers.<SalaryFix>lambdaQuery()
                .eq(SalaryFix::getEmployeeId, employeeId)
                .ge(SalaryFix::getEndTime, RequestTimeHolder.getRequestTime())
                .eq(SalaryFix::getAuditStatus, "2"), false);
    }
}