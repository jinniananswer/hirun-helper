package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeSalaryDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeeSalary;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 员工固定工资表(EmployeeSalary)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-02 00:25:10
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface EmployeeSalaryMapper extends BaseMapper<EmployeeSalary> {

    @Select("select a.name, a.employee_id, a.status, date_format(a.in_date,'%Y-%m-%d') in_date," +
            " b.job_role,b.org_id, c.name org_name,a.type, d.id, d.salary_month, d.basic/100 basic, d.rank/100 rank,d.performance/100 performance,d.duty/100 duty,d.overtime/100 overtime,d.float_award/100 float_award,d.other/100 other,d.back_pay/100 back_pay,d.royalties/100 royalties,d.medical/100 medical,d.overage/100 overage,d.unemployment/100 unemployment, d.serious_ill/100 serious_ill, d.tax/100 tax,d.remark,d.audit_status,d.audit_remark, d.is_modified " +
            " from ins_org c, ins_employee a " +
            " LEFT JOIN ( select * from ins_employee_job_role k where k.job_role_id in(select max(i.job_role_id) from (select * from ins_employee_job_role h where is_main= '1') i group by i.employee_id)) b" +
            " on (a.employee_id=b.employee_id) "+
            " left join (select * from employee_salary where salary_month = ${salaryMonth}) d on (d.employee_id = a.employee_id and d.end_time > now())"+
            " ${ew.customSqlSegment}"
    )
    List<EmployeeSalaryDTO> queryEmployeeSalaries(@Param(Constants.WRAPPER) Wrapper wrapper, @Param("salaryMonth")Integer salaryMonth);
}