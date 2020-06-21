package com.microtomato.hirun.modules.bss.salary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.security.UserContext;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.salary.entity.domain.SalaryDO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryMonthlyDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryMonthlyQueryDTO;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryMonthly;
import com.microtomato.hirun.modules.bss.salary.mapper.SalaryMonthlyMapper;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryMonthlyService;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
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

    /**
     * 查询员工月工资
     * @param param
     * @return
     */
    @Override
    public List<SalaryMonthlyDTO> queryEmployeeSalaries(SalaryMonthlyQueryDTO param) {
        QueryWrapper<SalaryMonthlyQueryDTO> wrapper = new QueryWrapper<>();
        wrapper.apply("c.org_id = b.org_id ")
                .apply("(a.destroy_date is null or a.destroy_date >= date_sub(date_sub(date_format('"+param.getSalaryMonth()+"01','%y%m%d'),interval extract(day from date_format('"+param.getSalaryMonth()+"01','%y%m%d'))-1 day),interval 1 month)) ")
                .like(StringUtils.isNotBlank(param.getName()), "a.name", param.getName())
                .eq(StringUtils.isNotBlank(param.getMobileNo()), "a.mobile_no", param.getMobileNo())
                .in(ArrayUtils.isNotEmpty(param.getOrgIds()), "b.org_id", param.getOrgIds())
                .eq(StringUtils.isNotBlank(param.getType()), "a.type", param.getType())
                .eq(StringUtils.isNotBlank(param.getAuditStatus()), "d.audit_status", param.getAuditStatus())
                .eq(StringUtils.isNotBlank(param.getStatus()), "a.status", param.getStatus());

        List<SalaryMonthlyDTO> salaries = this.salaryMonthlyMapper.querySalaries(wrapper, param.getSalaryMonth());
        String lastMonth = TimeUtils.addMonths(param.getSalaryMonth()+"01", TimeUtils.DATE_FMT_0, -1);
        lastMonth = lastMonth.substring(0, 6);

        //todo 需要改成查询固定工资项目数据
        List<SalaryMonthly> lastSalaries = this.queryByMonth(Integer.parseInt(lastMonth));

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
                    SalaryMonthly lastSalary = this.findByEmployeeId(lastSalaries, salary.getEmployeeId());
                    if (lastSalary != null) {
                        this.copyLastMonthSalary(salary, lastSalary);
                    }
                }
            });
        }

        SalaryDO salaryDO = SpringContextUtils.getBean(SalaryDO.class);
        salaryDO.createRoyalties(54L, "10");
        return salaries;
    }

    /**
     * 查询员工某月工资总表审核数据
     * @param param
     * @return
     */
    @Override
    public List<SalaryMonthlyDTO> queryAuditEmployeeSalaries(SalaryMonthlyQueryDTO param) {
        QueryWrapper<SalaryMonthlyDTO> wrapper = new QueryWrapper<>();
        wrapper.apply("c.org_id = b.org_id ")
                .apply("d.employee_id = a.employee_id ")
                .apply("d.end_time > now() ")
                .apply("d.salary_month = " + param.getSalaryMonth())
                .apply("(a.destroy_date is null or a.destroy_date >= date_sub(date_sub(date_format('"+param.getSalaryMonth()+"01','%y%m%d'),interval extract(day from date_format('"+param.getSalaryMonth()+"01','%y%m%d'))-1 day),interval 1 month)) ")
                .like(StringUtils.isNotBlank(param.getName()), "a.name", param.getName())
                .eq(StringUtils.isNotBlank(param.getMobileNo()), "a.mobile_no", param.getMobileNo())
                .in(ArrayUtils.isNotEmpty(param.getOrgIds()), "b.org_id", param.getOrgIds())
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
    private SalaryMonthly findByEmployeeId(List<SalaryMonthly> salaries, Long employeeId) {
        if (ArrayUtils.isEmpty(salaries)) {
            return null;
        }

        for (SalaryMonthly salary : salaries) {
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

        if (dto.getTax() != null) {
            salary.setTax(new Long(Math.round(dto.getTax() * 100)));
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
                dto.getTax() == null) {
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
                !copySalary.getTax().equals(tempSalary.getTax())) {
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
    }

    /**
     * 将上月的工资复制过来做初始值，除了提成数据
     * @param now
     * @param last
     */
    public void copyLastMonthSalary(SalaryMonthlyDTO now, SalaryMonthly last) {
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
    }
}