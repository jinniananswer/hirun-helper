package com.microtomato.hirun.modules.bss.salary.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.salary.entity.dto.EmployeeRedebitDTO;
import com.microtomato.hirun.modules.bss.salary.entity.dto.QueryEmployeeRedebitDTO;
import com.microtomato.hirun.modules.bss.salary.entity.po.SalaryRedebit;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 员工补扣款信息表(SalaryRedebit)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-08-30 20:34:51
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface SalaryRedebitMapper extends BaseMapper<SalaryRedebit> {

    @Select("select a.id, a.employee_id, a.redebit_item, a.salary_item, a.money/100 money, a.salary_month, a.in_employee_id, a.in_date, a.audit_employee_id, a.audit_status, a.audit_time, a.reason, b.name, c.org_id\n" +
            "from salary_redebit a, ins_employee b\n" +
            " LEFT JOIN ( select * from ins_employee_job_role k where k.job_role_id in(select max(i.job_role_id) from ins_employee_job_role i where i.is_main='1' group by i.employee_id)) c" +
            " on (b.employee_id=c.employee_id) "+
            "${ew.customSqlSegment}"
    )
    IPage<EmployeeRedebitDTO> queryEmployeeRedebits(IPage<QueryEmployeeRedebitDTO> queryCondition, @Param(Constants.WRAPPER) Wrapper wrapper);
}