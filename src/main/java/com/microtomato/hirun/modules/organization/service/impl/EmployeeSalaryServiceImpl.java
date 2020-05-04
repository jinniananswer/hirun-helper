package com.microtomato.hirun.modules.organization.service.impl;

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
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeSalaryDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeSalaryQueryDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeSalary;
import com.microtomato.hirun.modules.organization.mapper.EmployeeSalaryMapper;
import com.microtomato.hirun.modules.organization.service.IEmployeeSalaryService;
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
 * 员工固定工资表(EmployeeSalary)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-02 00:25:10
 */
@Service
@DataSource(DataSourceKey.INS)
@Slf4j
public class EmployeeSalaryServiceImpl extends ServiceImpl<EmployeeSalaryMapper, EmployeeSalary> implements IEmployeeSalaryService {

    @Autowired
    private EmployeeSalaryMapper employeeSalaryMapper;

    @Autowired
    private IStaticDataService staticDataService;

    /**
     * 查询员工月工资
     * @param param
     * @return
     */
    @Override
    public List<EmployeeSalaryDTO> queryEmployeeSalaries(EmployeeSalaryQueryDTO param) {
        QueryWrapper<EmployeeSalaryQueryDTO> wrapper = new QueryWrapper<>();
        wrapper.apply("c.org_id = b.org_id ")
                .apply("(a.destroy_date is null or a.destroy_date >= date_sub(date_sub(date_format('"+param.getSalaryMonth()+"01','%y%m%d'),interval extract(day from date_format('"+param.getSalaryMonth()+"01','%y%m%d'))-1 day),interval 1 month)) ")
                .like(StringUtils.isNotBlank(param.getName()), "a.name", param.getName())
                .eq(StringUtils.isNotBlank(param.getMobileNo()), "a.mobile_no", param.getMobileNo())
                .in(ArrayUtils.isNotEmpty(param.getOrgIds()), "b.org_id", param.getOrgIds())
                .eq(StringUtils.isNotBlank(param.getType()), "a.type", param.getType())
                .eq(StringUtils.isNotBlank(param.getStatus()), "a.status", param.getStatus());

        List<EmployeeSalaryDTO> salaries = this.employeeSalaryMapper.queryEmployeeSalaries(wrapper, param.getSalaryMonth());
        String lastMonth = TimeUtils.addMonths(param.getSalaryMonth()+"01", TimeUtils.DATE_FMT_0, -1);
        lastMonth = lastMonth.substring(0, 6);

        List<EmployeeSalary> lastSalaries = this.queryByMonth(Integer.parseInt(lastMonth));

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

                if (salary.getId() == null) {
                    //如果没有查到数据，则自动将上月的工资数据带过来
                    EmployeeSalary lastSalary = this.findByEmployeeId(lastSalaries, salary.getEmployeeId());
                    if (lastSalary != null) {
                        this.copyLastMonthSalary(salary, lastSalary);
                    }
                }
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
    public List<EmployeeSalary> queryByMonth(Integer salaryMonth) {
        LocalDateTime now = RequestTimeHolder.getRequestTime();
        return this.list(new QueryWrapper<EmployeeSalary>().lambda()
                .eq(EmployeeSalary::getSalaryMonth, salaryMonth)
                .ge(EmployeeSalary::getEndTime, now));
    }

    /**
     * 保存员工月工资
     * @param salaries
     */
    @Override
    public void saveSalaries(List<EmployeeSalaryDTO> salaries) {
        if (ArrayUtils.isEmpty(salaries)) {
            return;
        }

        Integer salaryMonth = salaries.get(0).getSalaryMonth();
        List<EmployeeSalary> employeeSalaries = this.queryByMonth(salaryMonth);

        List<EmployeeSalary> createEmployeeSalaries = new ArrayList<>();
        List<EmployeeSalary> modifyEmployeeSalaries = new ArrayList<>();
        LocalDateTime now = RequestTimeHolder.getRequestTime();

        salaries.forEach((salary) -> {
            if (salary.getId() == null) {
                //ID为空，表示为新创建的数据
                if (this.isEmpty(salary)) {
                    return;
                }
                EmployeeSalary employeeSalary = this.fillNewSalary(salary);
                createEmployeeSalaries.add(employeeSalary);
            } else {
                //ID不为空，表示为修改后的数据
                EmployeeSalary employeeSalary = this.findById(employeeSalaries, salary.getId());
                if (employeeSalary == null) {
                    return;
                }

                if (this.equals(salary, employeeSalary)) {
                    //金额没有发生变化，则不做处理
                    return;
                }

                String auditStatus = employeeSalary.getAuditStatus();
                if (StringUtils.equals("0", auditStatus)) {
                    //如果还未提交审核的数据，则保存的话直接修改原记录
                    this.moneyUnitTransfer(salary, employeeSalary);
                    modifyEmployeeSalaries.add(employeeSalary);
                } else if (StringUtils.equals("3", auditStatus)) {
                    //审核不通过后的修改，则终止原来的记录，保存轨迹数据
                    employeeSalary.setEndTime(now);
                    modifyEmployeeSalaries.add(employeeSalary);
                    salary.setId(null);
                    EmployeeSalary newEmployeeSalary = this.fillNewSalary(salary);
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
     * 根据给定的ID在列表中查找符合的数据
     * @param salaries
     * @param id
     * @return
     */
    private EmployeeSalary findById(List<EmployeeSalary> salaries, Long id) {
        if (ArrayUtils.isEmpty(salaries)) {
            return null;
        }

        for (EmployeeSalary salary : salaries) {
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
    private EmployeeSalary findByEmployeeId(List<EmployeeSalary> salaries, Long employeeId) {
        if (ArrayUtils.isEmpty(salaries)) {
            return null;
        }

        for (EmployeeSalary salary : salaries) {
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
    private EmployeeSalary fillNewSalary(EmployeeSalaryDTO salary) {
        UserContext userContext = WebContextUtils.getUserContext();
        Long employeeId = userContext.getEmployeeId();
        LocalDateTime now = RequestTimeHolder.getRequestTime();

        EmployeeSalary employeeSalary = new EmployeeSalary();
        BeanUtils.copyProperties(salary, employeeSalary);
        employeeSalary.setStartTime(now);
        employeeSalary.setEndTime(TimeUtils.getForeverTime());
        employeeSalary.setCreateEmployeeId(employeeId);
        employeeSalary.setAuditStatus("0");
        this.moneyUnitTransfer(salary, employeeSalary);
        return employeeSalary;
    }

    /**
     * 各项费用的double到Long型的转换
     * @param salary
     * @param dto
     */
    private void moneyUnitTransfer(EmployeeSalaryDTO dto, EmployeeSalary salary) {
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

        if (dto.getRoyalties() != null) {
            salary.setRoyalties(new Long(Math.round(dto.getRoyalties() * 100)));
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
    private boolean isEmpty(EmployeeSalaryDTO dto) {
        if (dto.getBasic() == null &&
            dto.getRank() == null &&
            dto.getPerformance() == null &&
            dto.getDuty() == null &&
            dto.getOvertime() == null &&
            dto.getFloatAward() == null &&
            dto.getOther() == null &&
            dto.getBackPay() == null &&
            dto.getRoyalties() == null &&
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
    private boolean equals(EmployeeSalaryDTO dto, EmployeeSalary salary) {
        if (salary.getId() != null && !salary.getId().equals(dto.getId())) {
            return false;
        }

        if (!salary.getEmployeeId().equals(dto.getEmployeeId())) {
            return false;
        }
        EmployeeSalary tempSalary = new EmployeeSalary();
        this.moneyUnitTransfer(dto, tempSalary);
        this.setDefaultZero(tempSalary);

        EmployeeSalary copySalary = new EmployeeSalary();
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
                !copySalary.getRoyalties().equals(tempSalary.getRoyalties()) ||
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
    private void setDefaultZero(EmployeeSalary salary) {
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

        if (salary.getRoyalties() == null) {
            salary.setRoyalties(0L);
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
    public void copyLastMonthSalary(EmployeeSalaryDTO now, EmployeeSalary last) {
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