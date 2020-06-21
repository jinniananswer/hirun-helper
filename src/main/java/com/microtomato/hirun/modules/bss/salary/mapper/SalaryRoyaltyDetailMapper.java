package com.microtomato.hirun.modules.bss.salary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.salary.entity.dto.EmployeeSalaryRoyaltyDetailDTO;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryRoyaltyDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 员工工资提成明细(SalaryRoyaltyDetail)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-17 17:57:21
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface SalaryRoyaltyDetailMapper extends BaseMapper<SalaryRoyaltyDetail> {

    @Select("select a.employee_id, a.strategy_id, a.type, a.item, a.total_royalty, a.already_fetch, a.this_month_fetch, a.salary_month, a.audit_status, a.remark, a.audit_remark, b.name employee_name, b.status employee_status, c.org_id, c.job_role, c.job_grade\n" +
            "from salary_royalty_detail a, ins_employee b, ins_employee_job_role c\n" +
            "where b.employee_id = a.employee_id\n" +
            "and c.employee_id = a.employee_id\n" +
            "and c.is_main = '1'\n" +
            "and c.start_date = (select max(start_date) from ins_employee_job_role d where d.employee_id = c.employee_id)\n" +
            "and a.order_id = ${orderId}"
    )
    List<EmployeeSalaryRoyaltyDetailDTO> querySalaries(@Param("orderId")Long orderId);

}