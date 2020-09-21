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
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryMonthlyDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryMonthlyQueryDTO;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryFix;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryMonthly;
import com.microtomato.hirun.modules.bss.salary.exception.SalaryException;
import com.microtomato.hirun.modules.bss.salary.mapper.SalaryMonthlyMapper;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryFixService;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryMonthlyService;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeJobRole;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IEmployeeJobRoleService;
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
 * 员工月工资总表(SalaryMonthly)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 00:26:31
 */
@Service
@DataSource(DataSourceKey.INS)
@Slf4j
public class SalaryMonthlyServiceImpl extends ServiceImpl<SalaryMonthlyMapper, SalaryMonthly> implements ISalaryMonthlyService {

    @Autowired
    private SalaryMonthlyMapper salaryMonthlyMapper;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private ISalaryFixService salaryFixService;

    @Autowired
    private IEmployeeJobRoleService employeeJobRoleService;

    /**
     * 查询员工月工资
     * @param param
     * @return
     */
    @Override
    public List<SalaryMonthlyDTO> queryEmployeeSalaries(SalaryMonthlyQueryDTO param) {
        QueryWrapper<SalaryMonthlyQueryDTO> wrapper = new QueryWrapper<>();

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

        wrapper.apply("c.org_id = b.org_id ")
                .apply("(a.destroy_date is null or a.destroy_date >= date_sub(date_sub(date_format('"+param.getSalaryMonth()+"01','%y%m%d'),interval extract(day from date_format('"+param.getSalaryMonth()+"01','%y%m%d'))-1 day),interval 1 month)) ")
                .like(StringUtils.isNotBlank(param.getName()), "a.name", param.getName())
                .eq(StringUtils.isNotBlank(param.getMobileNo()), "a.mobile_no", param.getMobileNo())
                .in(ArrayUtils.isNotEmpty(orgs), "b.org_id", orgs)
                .eq(StringUtils.isNotBlank(param.getType()), "a.type", param.getType())
                .eq(StringUtils.isNotBlank(param.getAuditStatus()), "d.audit_status", param.getAuditStatus())
                .eq(StringUtils.isNotBlank(param.getStatus()), "a.status", param.getStatus());

        List<SalaryMonthlyDTO> salaries = this.salaryMonthlyMapper.querySalaries(wrapper, param.getSalaryMonth());
        String lastMonth = TimeUtils.addMonths(param.getSalaryMonth()+"01", TimeUtils.DATE_FMT_0, -1);
        lastMonth = lastMonth.substring(0, 6);

        //todo 需要改成查询固定工资项目数据
        List<SalaryFix> lastSalaries = this.salaryFixService.queryAllValidAudit();

        if (ArrayUtils.isNotEmpty(salaries)) {
            salaries.forEach((salary) -> {
                if (param.getSalaryMonth() != null) {
                    salary.setSalaryMonth(param.getSalaryMonth());
                }
                if (salary.getOrgId() != null) {
                    OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, salary.getOrgId());
                    salary.setOrgPath(orgDO.getCompanyLinePath());
                }
                salary.setTypeName(this.staticDataService.getCodeName("EMPLOYEE_TYPE", salary.getType()));
                salary.setJobRoleName(this.staticDataService.getCodeName("JOB_ROLE", salary.getJobRole()));
                salary.setStatusName(this.staticDataService.getCodeName("EMPLOYEE_STATUS", salary.getStatus()));
                salary.setAuditStatusName(this.staticDataService.getCodeName("SALARY_AUDIT_STATUS", salary.getAuditStatus()));

                if (salary.getId() == null) {
                    //如果没有查到数据，则自动将上月的工资数据带过来
                    SalaryFix lastSalary = this.findByEmployeeId(lastSalaries, salary.getEmployeeId());
                    if (lastSalary != null) {
                        this.copyLastMonthSalary(salary, lastSalary);
                    }
                }
            });
        }

        return salaries;
    }

    /**
     * 查询员工某月工资总表审核数据
     * @param param
     * @return
     */
    @Override
    public List<SalaryMonthlyDTO> queryAuditEmployeeSalaries(SalaryMonthlyQueryDTO param) {

        List<Long> orgs = new ArrayList<>();
        if (StringUtils.isNotBlank(param.getOrgIds())) {
            if (StringUtils.isNotBlank(param.getOrgIds())) {
                String[] orgIdArray = param.getOrgIds().split(",");
                for (String s : orgIdArray) {
                    orgs.add(Long.parseLong(s));
                }
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

        QueryWrapper<SalaryMonthlyDTO> wrapper = new QueryWrapper<>();
        wrapper.apply("c.org_id = b.org_id ")
                .apply("d.employee_id = a.employee_id ")
                .apply("d.end_time > now() ")
                .apply("d.salary_month = " + param.getSalaryMonth())
                .apply("(a.destroy_date is null or a.destroy_date >= date_sub(date_sub(date_format('"+param.getSalaryMonth()+"01','%y%m%d'),interval extract(day from date_format('"+param.getSalaryMonth()+"01','%y%m%d'))-1 day),interval 1 month)) ")
                .like(StringUtils.isNotBlank(param.getName()), "a.name", param.getName())
                .eq(StringUtils.isNotBlank(param.getMobileNo()), "a.mobile_no", param.getMobileNo())
                .in(ArrayUtils.isNotEmpty(orgs), "b.org_id", orgs)
                .eq(StringUtils.isNotBlank(param.getType()), "a.type", param.getType())
                .eq(StringUtils.isNotBlank(param.getAuditStatus()), "d.audit_status", param.getAuditStatus())
                .eq(StringUtils.isNotBlank(param.getStatus()), "a.status", param.getStatus());

        List<SalaryMonthlyDTO> salaries = this.salaryMonthlyMapper.queryAuditSalaries(wrapper);

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
     * 根据发薪月份查询有效发薪信息
     * @param salaryMonth
     * @return
     */
    @Override
    public List<SalaryMonthly> queryByMonth(Integer salaryMonth) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(new QueryWrapper<SalaryMonthly>().lambda()
                .eq(SalaryMonthly::getSalaryMonth, salaryMonth)
                .ge(SalaryMonthly::getEndTime, now));
    }

    /**
     * 保存员工月工资
     * @param salaries
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void saveSalaries(List<SalaryMonthlyDTO> salaries, boolean isAudit) {
        if (ArrayUtils.isEmpty(salaries)) {
            return;
        }

        Integer salaryMonth = salaries.get(0).getSalaryMonth();
        List<SalaryMonthly> employeeSalaries = this.queryByMonth(salaryMonth);

        List<SalaryMonthly> createEmployeeSalaries = new ArrayList<>();
        List<SalaryMonthly> modifyEmployeeSalaries = new ArrayList<>();
        LocalDateTime now = RequestTimeHolder.getRequestTime();

        salaries.forEach((salary) -> {
            if (salary.getId() == null) {
                //ID为空，表示为新创建的数据
                if (this.isEmpty(salary)) {
                    return;
                }
                SalaryMonthly employeeSalary = this.fillNewSalary(salary, isAudit);
                createEmployeeSalaries.add(employeeSalary);
            } else {
                //ID不为空，表示为修改后的数据
                SalaryMonthly employeeSalary = this.findById(employeeSalaries, salary.getId());
                if (employeeSalary == null) {
                    return;
                }

                String auditStatus = employeeSalary.getAuditStatus();
                if (this.equals(salary, employeeSalary) && !isAudit) {
                    //金额没有发生变化，则不做处理
                    return;
                }

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
                    SalaryMonthly newEmployeeSalary = this.fillNewSalary(salary, isAudit);
                    //设置为1表示这条记录是修改原来的
                    newEmployeeSalary.setIsModified("1");
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
     * 审核员工某月工资总表数据
     * @param employeeSalaries
     * @param isPass
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void audit(List<SalaryMonthlyDTO> employeeSalaries, boolean isPass) {
        if (ArrayUtils.isEmpty(employeeSalaries)) {
            return;
        }

        Integer salaryMonth = employeeSalaries.get(0).getSalaryMonth();
        List<SalaryMonthly> modifyEmployeeSalaries = new ArrayList<>();
        List<SalaryMonthly> all = this.queryByMonth(salaryMonth);

        employeeSalaries.forEach((employeeSalary -> {
            Long id = employeeSalary.getId();
            if (id != null) {
                SalaryMonthly originalEmployeeSalary = this.findById(all, id);
                if (originalEmployeeSalary != null) {
                    if (isPass) {
                        originalEmployeeSalary.setAuditStatus("2");
                    } else {
                        originalEmployeeSalary.setAuditStatus("3");
                        originalEmployeeSalary.setAuditRemark(employeeSalary.getAuditRemark());
                    }

                    modifyEmployeeSalaries.add(originalEmployeeSalary);
                }
            }
        }));

        if (ArrayUtils.isNotEmpty(modifyEmployeeSalaries)) {
            this.updateBatchById(modifyEmployeeSalaries);
        }
    }

    /**
     * 根据给定的ID在列表中查找符合的数据
     * @param salaries
     * @param id
     * @return
     */
    private SalaryMonthly findById(List<SalaryMonthly> salaries, Long id) {
        if (ArrayUtils.isEmpty(salaries)) {
            return null;
        }

        for (SalaryMonthly salary : salaries) {
            if (id.equals(salary.getId())) {
                return salary;
            }
        }
        return null;
    }

    /**
     * 根据给定的ID在列表中查找符合的数据
     * @param salaries
     * @param employeeId
     * @return
     */
    private SalaryFix findByEmployeeId(List<SalaryFix> salaries, Long employeeId) {
        if (ArrayUtils.isEmpty(salaries)) {
            return null;
        }

        for (SalaryFix salary : salaries) {
            if (employeeId.equals(salary.getEmployeeId())) {
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
    private SalaryMonthly fillNewSalary(SalaryMonthlyDTO salary, boolean isAudit) {
        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId = userContext.getEmployeeId();
        LocalDateTime now = RequestTimeHolder.getRequestTime();

        SalaryMonthly employeeSalary = new SalaryMonthly();
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
    private void moneyUnitTransfer(SalaryMonthlyDTO dto, SalaryMonthly salary) {
        if (dto.getBasic() != null) {
            salary.setBasic(new Long(Math.round(dto.getBasic() * 100)));
        }

        if (dto.getRank() != null) {
            salary.setRank(new Long(Math.round(dto.getRank() * 100)));
        }

        if (dto.getJob() != null) {
            salary.setJob(new Long(Math.round(dto.getJob() * 100)));
        }

        if (dto.getPerformance() != null) {
            salary.setPerformance(new Long(Math.round(dto.getPerformance() * 100)));
        }

        if (dto.getFullTime() != null) {
            salary.setFullTime(new Long(Math.round(dto.getFullTime() * 100)));
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

        if (dto.getRoyalty() != null) {
            salary.setRoyalty(new Long(Math.round(dto.getRoyalty() * 100)));
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

        if (dto.getCompanyPart() != null) {
            salary.setCompanyPart(new Long(Math.round(dto.getCompanyPart() * 100)));
        }

        if (dto.getTax() != null) {
            salary.setTax(new Long(Math.round(dto.getTax() * 100)));
        }

        if (dto.getDebit() != null) {
            salary.setDebit(new Long(Math.round(dto.getDebit() * 100)));
        }

        if (dto.getVacation() != null) {
            salary.setVacation(new Long(Math.round(dto.getVacation() * 100)));
        }

        if (dto.getLate() != null) {
            salary.setLate(new Long(Math.round(dto.getLate() * 100)));
        }

        if (dto.getNotice() != null) {
            salary.setNotice(new Long(Math.round(dto.getNotice() * 100)));
        }
    }

    /**
     * 是否没有填入数据，没有填入任何金额的数据是不需要入库的
     * @param dto
     * @return
     */
    private boolean isEmpty(SalaryMonthlyDTO dto) {
        if (dto.getBasic() == null &&
                dto.getRank() == null &&
                dto.getPerformance() == null &&
                dto.getDuty() == null &&
                dto.getOvertime() == null &&
                dto.getFloatAward() == null &&
                dto.getOther() == null &&
                dto.getBackPay() == null &&
                dto.getRoyalty() == null &&
                dto.getMedical() == null &&
                dto.getOverage() == null &&
                dto.getUnemployment() == null &&
                dto.getSeriousIll() == null &&
                dto.getTax() == null &&
                dto.getJob() == null &&
                dto.getFullTime() == null &&
                dto.getCompanyPart() == null &&
                dto.getDebit() == null &&
                dto.getVacation() == null &&
                dto.getLate() == null &&
                dto.getNotice() == null &&
                dto.getPerformance() == null) {
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
    private boolean equals(SalaryMonthlyDTO dto, SalaryMonthly salary) {
        if (salary.getId() != null && !salary.getId().equals(dto.getId())) {
            return false;
        }

        if (!salary.getEmployeeId().equals(dto.getEmployeeId())) {
            return false;
        }
        SalaryMonthly tempSalary = new SalaryMonthly();
        this.moneyUnitTransfer(dto, tempSalary);
        this.setDefaultZero(tempSalary);

        SalaryMonthly copySalary = new SalaryMonthly();
        BeanUtils.copyProperties(salary, copySalary);
        this.setDefaultZero(copySalary);

        if (!copySalary.getBasic().equals(tempSalary.getBasic()) ||
                !copySalary.getRank().equals(tempSalary.getRank()) ||
                !copySalary.getPerformance().equals(tempSalary.getPerformance()) ||
                !copySalary.getDuty().equals(tempSalary.getDuty()) ||
                !copySalary.getOvertime().equals(tempSalary.getOvertime()) ||
                !copySalary.getFloatAward().equals(tempSalary.getFloatAward()) ||
                !copySalary.getOther().equals(tempSalary.getOther()) ||
                !copySalary.getBackPay().equals(tempSalary.getBackPay()) ||
                !copySalary.getRoyalty().equals(tempSalary.getRoyalty()) ||
                !copySalary.getMedical().equals(tempSalary.getMedical()) ||
                !copySalary.getOverage().equals(tempSalary.getOverage()) ||
                !copySalary.getUnemployment().equals(tempSalary.getUnemployment()) ||
                !copySalary.getSeriousIll().equals(tempSalary.getSeriousIll()) ||
                !copySalary.getTax().equals(tempSalary.getTax()) ||
                !copySalary.getJob().equals(tempSalary.getJob()) ||
                !copySalary.getFullTime().equals(tempSalary.getFullTime()) ||
                !copySalary.getCompanyPart().equals(tempSalary.getCompanyPart()) ||
                !copySalary.getDebit().equals(tempSalary.getDebit()) ||
                !copySalary.getVacation().equals(tempSalary.getVacation()) ||
                !copySalary.getLate().equals(tempSalary.getLate()) ||
                !copySalary.getNotice().equals(tempSalary.getNotice()) ||
                !copySalary.getPerformanceAssess().equals(tempSalary.getPerformanceAssess())) {
            return false;
        }

        return true;
    }

    /**
     * 将为Null值的设置为0
     * @param salary
     */
    private void setDefaultZero(SalaryMonthly salary) {
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

        if (salary.getPerformanceAssess() == null) {
            salary.setPerformanceAssess(0L);
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

        if (salary.getRoyalty() == null) {
            salary.setRoyalty(0L);
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

        if (salary.getJob() == null) {
            salary.setJob(0L);
        }

        if (salary.getFullTime() == null) {
            salary.setFullTime(0L);
        }

        if (salary.getCompanyPart() == null) {
            salary.setCompanyPart(0L);
        }

        if (salary.getDebit() == null) {
            salary.setDebit(0L);
        }

        if (salary.getVacation() == null) {
            salary.setVacation(0L);
        }

        if (salary.getLate() == null) {
            salary.setLate(0L);
        }

        if (salary.getNotice() == null) {
            salary.setNotice(0L);
        }
    }

    /**
     * 将上月的工资复制过来做初始值，除了提成数据
     * @param now
     * @param last
     */
    public void copyLastMonthSalary(SalaryMonthlyDTO now, SalaryFix last) {
        if (now.getBasic() == null && last.getBasic() != null) {
            now.setBasic(last.getBasic().doubleValue()/100);
        }

        if (now.getRank() == null && last.getRank() != null) {
            now.setRank(last.getRank().doubleValue()/100);
        }

        if (now.getPerformance() == null && last.getPerformance() != null) {
            now.setPerformance(last.getPerformance().doubleValue()/100);
        }

        if (now.getDuty() == null && last.getDuty() != null) {
            now.setDuty(last.getDuty().doubleValue()/100);
        }

        if (now.getOvertime() == null && last.getOvertime() != null) {
            now.setOvertime(last.getOvertime().doubleValue()/100);
        }

        if (now.getFloatAward() == null && last.getFloatAward() != null) {
            now.setFloatAward(last.getFloatAward().doubleValue()/100);
        }

        if (now.getOther() == null && last.getOther() != null) {
            now.setOther(last.getOther().doubleValue()/100);
        }

        if (now.getBackPay() == null && last.getBackPay() != null) {
            now.setBackPay(last.getBackPay().doubleValue()/100);
        }

        if (now.getMedical() == null && last.getMedical() != null) {
            now.setMedical(last.getMedical().doubleValue()/100);
        }

        if (now.getOverage() == null && last.getOverage() != null) {
            now.setOverage(last.getOverage().doubleValue()/100);
        }

        if (now.getUnemployment() == null && last.getUnemployment() != null) {
            now.setUnemployment(last.getUnemployment().doubleValue()/100);
        }

        if (now.getSeriousIll() == null && last.getSeriousIll() != null) {
            now.setSeriousIll(last.getSeriousIll().doubleValue()/100);
        }

        if (now.getTax() == null && last.getTax() != null) {
            now.setTax(last.getTax().doubleValue()/100);
        }

        if (now.getJob() == null && last.getJob() != null) {
            now.setJob(last.getJob().doubleValue()/100);
        }
    }

    /**
     * 根据员工ID与月份查询月工资信息
     * @param employeeId
     * @param salaryMonth
     * @return
     */
    @Override
    public SalaryMonthly getByEmployeeIdMonth(Long employeeId, Integer salaryMonth) {
        return this.getOne(Wrappers.<SalaryMonthly>lambdaQuery()
                .eq(SalaryMonthly::getEmployeeId, employeeId)
                .eq(SalaryMonthly::getSalaryMonth, salaryMonth)
                .ge(SalaryMonthly::getEndTime, RequestTimeHolder.getRequestTime()), false);
    }

    /**
     * 修改月工资中的提成信息
     * @param totalRoyalty
     * @param salaryMonth
     * @param employeeId
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateRoyalties(Long totalRoyalty, Integer salaryMonth, Long employeeId) {
        SalaryMonthly salaryMonthly = this.getByEmployeeIdMonth(employeeId, salaryMonth);
        if (salaryMonthly == null) {
            salaryMonthly = new SalaryMonthly();
            SalaryFix salaryFix = this.salaryFixService.getByEmployeeId(employeeId);
            if (salaryFix != null) {
                BeanUtils.copyProperties(salaryFix, salaryMonthly);
            }
            salaryMonthly.setEmployeeId(employeeId);
            salaryMonthly.setSalaryMonth(salaryMonth);
            salaryMonthly.setAuditStatus("0");
            salaryMonthly.setStartTime(RequestTimeHolder.getRequestTime());
            salaryMonthly.setEndTime(TimeUtils.getForeverTime());
        }
        salaryMonthly.setRoyalty(totalRoyalty);

        if (salaryMonthly.getId() == null) {
            this.save(salaryMonthly);
        } else {
            this.updateById(salaryMonthly);
        }
    }

    /**
     * 员工补扣款信息更新月工资
     * @param employeeId
     * @param employeeName
     * @param salaryItem
     * @param money
     * @param salaryMonth
     */
    @Override
    public void updateEmployeeMonthly(Long employeeId, String employeeName, String salaryItem, Long money, Integer salaryMonth) {
        SalaryMonthly salaryMonthly = this.getByEmployeeIdMonth(employeeId, salaryMonth);

        if (salaryMonthly != null && StringUtils.equals("4", salaryMonthly.getAuditStatus())) {
            throw new SalaryException(SalaryException.SalaryExceptionEnum.CANNOT_UPDATE_MONTHLY, employeeName, salaryMonth+"");
        }

        if (salaryMonthly == null) {
            salaryMonthly = new SalaryMonthly();
            SalaryFix salaryFix = this.salaryFixService.getByEmployeeId(employeeId);
            if (salaryFix != null) {
                BeanUtils.copyProperties(salaryFix, salaryMonthly);
            }
            salaryMonthly.setEmployeeId(employeeId);
            salaryMonthly.setSalaryMonth(salaryMonth);
            salaryMonthly.setAuditStatus("0");
            EmployeeJobRole employeeJobRole = this.employeeJobRoleService.queryLast(employeeId);
            if (employeeJobRole != null) {
                salaryMonthly.setOrgId(employeeJobRole.getOrgId());
                salaryMonthly.setJobRole(employeeJobRole.getJobRole());
            }
        }

        this.setDefaultZero(salaryMonthly);
        switch(salaryItem) {
            case "1" :
                salaryMonthly.setBasic(salaryMonthly.getBasic() + money);
                break;
            case "2" :
                salaryMonthly.setRank(salaryMonthly.getRank() + money);
                break;
            case "3" :
                salaryMonthly.setJob(salaryMonthly.getJob() + money);
                break;
            case "4" :
                salaryMonthly.setPerformance(salaryMonthly.getPerformance() + money);
                break;
            case "5" :
                salaryMonthly.setFullTime(salaryMonthly.getFullTime() + money);
                break;
            case "6" :
                salaryMonthly.setDuty(salaryMonthly.getDuty() + money);
                break;
            case "7" :
                salaryMonthly.setOvertime(salaryMonthly.getOvertime() + money);
                break;
            case "8" :
                salaryMonthly.setPerformanceAssess(salaryMonthly.getPerformanceAssess() + money);
                break;
            case "9" :
                salaryMonthly.setFloatAward(salaryMonthly.getFloatAward() + money);
                break;
            case "10" :
                salaryMonthly.setOther(salaryMonthly.getOther() + money);
                break;
            case "11" :
                salaryMonthly.setBackPay(salaryMonthly.getBackPay() + money);
                break;
            case "12" :
                salaryMonthly.setRoyalty(salaryMonthly.getRoyalty() + money);
                break;
            case "13" :
                salaryMonthly.setTax(salaryMonthly.getTax() + money);
                break;
            case "14" :
                salaryMonthly.setDebit(salaryMonthly.getDebit() + money);
                break;
            case "15" :
                salaryMonthly.setVacation(salaryMonthly.getVacation() + money);
                break;
            case "16" :
                salaryMonthly.setLate(salaryMonthly.getLate() + money);
                break;
            case "17" :
                salaryMonthly.setNotice(salaryMonthly.getNotice() + money);
                break;
        }

        if (salaryMonthly.getId() == null) {
            this.save(salaryMonthly);
        } else {
            this.updateById(salaryMonthly);
        }
    }
}