package com.microtomato.hirun.modules.bss.salary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.threadlocal.RequestTimeHolder;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.bss.salary.entity.dto.CreateEmployeeRedebitDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.EmployeeRedebitDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.QueryEmployeeRedebitDTO;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryRedebit;
import com.microtomato.hirun.modules.bss.salary.mapper.SalaryRedebitMapper;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryMonthlyService;
import com.microtomato.hirun.modules.bss.salary.service.ISalaryRedebitService;
import com.microtomato.hirun.modules.organization.entity.domain.OrgDO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
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
 * 员工补扣款信息表(SalaryRedebit)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-08-30 20:34:51
 */
@Service
@Slf4j
@DataSource(DataSourceKey.INS)
public class SalaryRedebitServiceImpl extends ServiceImpl<SalaryRedebitMapper, SalaryRedebit> implements ISalaryRedebitService {

    @Autowired
    private SalaryRedebitMapper salaryRedebitMapper;

    @Autowired
    private IStaticDataService staticDataService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private ISalaryMonthlyService salaryMonthlyService;

    /**
     * 查询员工补录款信息
     * @param request
     * @return
     */
    @Override
    public IPage<EmployeeRedebitDTO> queryEmployeeRedebits(QueryEmployeeRedebitDTO request) {
        List<Long> orgs = new ArrayList<>();
        if (StringUtils.isNotBlank(request.getOrgIds())) {
            String[] orgIdArray = request.getOrgIds().split(",");
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

        IPage<QueryEmployeeRedebitDTO> condition = new Page<>(request.getPage(), request.getLimit());
        QueryWrapper<QueryEmployeeRedebitDTO> wrapper = new QueryWrapper<>();
        wrapper.apply(" b.employee_id = a.employee_id ");
        wrapper.apply("a.end_time > now() ");
        wrapper.eq(StringUtils.isNotBlank(request.getSalaryItem()), "a.salary_item", request.getSalaryItem());
        wrapper.eq(StringUtils.isNotBlank(request.getRedebitItem()), "a.redebit_item", request.getRedebitItem());
        wrapper.eq(StringUtils.isNotBlank(request.getAuditStatus()), "a.audit_status", request.getAuditStatus());
        wrapper.eq(request.getSalaryMonth() != null, "a.salary_month", request.getSalaryMonth());
        wrapper.in(ArrayUtils.isNotEmpty(orgs), "c.org_id", orgs);
        wrapper.eq(request.getEmployeeId() != null, "a.employee_id", request.getEmployeeId());

        IPage<EmployeeRedebitDTO> result = this.salaryRedebitMapper.queryEmployeeRedebits(condition, wrapper);
        List<EmployeeRedebitDTO> redebits = result.getRecords();
        if (ArrayUtils.isEmpty(redebits)) {
            return result;
        }

        redebits.forEach(redebit -> {
            Long orgId = redebit.getOrgId();
            if (orgId != null) {
                OrgDO orgDO = SpringContextUtils.getBean(OrgDO.class, orgId);
                redebit.setOrgPath(orgDO.getCompanyLinePath());
            }
            redebit.setAuditStatusName(this.staticDataService.getCodeName("SALARY_AUDIT_STATUS", redebit.getAuditStatus()));
            redebit.setRedebitItemName(this.staticDataService.getCodeName("REDEBIT_ITEM", redebit.getRedebitItem()));
            redebit.setSalaryItemName(this.staticDataService.getCodeName("SALARY_ITEM", redebit.getRedebitItem()));
            Long inEmployeeId =redebit.getInEmployeeId();
            if (inEmployeeId != null) {
                Employee employee = this.employeeService.getById(inEmployeeId);
                if (employee != null) {
                    redebit.setInEmployeeName(employee.getName());
                }
            }

            Long auditEmployeeId =redebit.getAuditEmployeeId();
            if (auditEmployeeId != null) {
                Employee employee = this.employeeService.getById(auditEmployeeId);
                if (employee != null) {
                    redebit.setAuditEmployeeName(employee.getName());
                }
            }
        });

        return result;
    }

    /**
     * 新建员工补录款信息
     * @param employeeRedebit
     */
    @Override
    public void createRedebit(CreateEmployeeRedebitDTO employeeRedebit) {
        SalaryRedebit redebit = new SalaryRedebit();
        BeanUtils.copyProperties(employeeRedebit, redebit);

        if (StringUtils.isBlank(redebit.getAuditStatus())) {
            redebit.setAuditStatus("1");
        }

        Double money = employeeRedebit.getMoney();
        if (money != null) {
            redebit.setMoney(new Long(Math.round(money * 100)));
        } else {
            redebit.setMoney(0L);
        }

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        redebit.setStartTime(now);
        redebit.setEndTime(TimeUtils.getForeverTime());
        redebit.setInEmployeeId(WebContextUtils.getUserContext().getEmployeeId());
        this.save(redebit);
    }

    @Override
    public void deleteRedebits(List<EmployeeRedebitDTO> redebits) {
        if (ArrayUtils.isEmpty(redebits)) {
            return;
        }

        LocalDateTime now = RequestTimeHolder.getRequestTime();

        List<SalaryRedebit> salaryRedebits = new ArrayList<>();
        redebits.forEach(redebit -> {
            SalaryRedebit salaryRedebit = new SalaryRedebit();
            salaryRedebit.setId(redebit.getId());
            salaryRedebit.setEndTime(now);
            salaryRedebits.add(salaryRedebit);
        });

        if (ArrayUtils.isNotEmpty(salaryRedebits)) {
            this.updateBatchById(salaryRedebits);
        }
    }

    /**
     * 审核员工补录款信息
     * @param redebits
     * @param pass
     */
    @Override
    public void auditRedebit(List<EmployeeRedebitDTO> redebits, boolean pass) {
        if (ArrayUtils.isEmpty(redebits)) {
            return;
        }

        LocalDateTime now = RequestTimeHolder.getRequestTime();
        Long employeeId = WebContextUtils.getUserContext().getEmployeeId();

        List<SalaryRedebit> salaryRedebits = new ArrayList<>();
        redebits.forEach(redebit -> {
            SalaryRedebit salaryRedebit = new SalaryRedebit();
            salaryRedebit.setId(redebit.getId());
            if (pass) {
                salaryRedebit.setAuditStatus("2");
            } else {
                salaryRedebit.setAuditStatus("3");
            }

            String redebitItem = redebit.getRedebitItem();
            Long money = new Long(Math.round(redebit.getMoney() * 100));

            if (StringUtils.equals(redebitItem, "2")) {
                money = money * -1;
            }
            salaryRedebit.setAuditTime(now);
            salaryRedebit.setAuditEmployeeId(employeeId);
            salaryRedebits.add(salaryRedebit);

            this.salaryMonthlyService.updateEmployeeMonthly(redebit.getEmployeeId(), redebit.getName(), redebit.getSalaryItem(), money, redebit.getSalaryMonth());
        });

        if (ArrayUtils.isNotEmpty(salaryRedebits)) {
            this.updateBatchById(salaryRedebits);
        }
    }
}