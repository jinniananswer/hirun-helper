package com.microtomato.hirun.modules.bss.salary.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.salary.entity.dto.*;
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

    @Select("select a.id, a.order_id, a.employee_id, a.org_id, a.job_role, a.job_grade, a.strategy_id, a.order_status, a.fee_type, a.periods, a.type, a.item, a.value, a.total_royalty, a.already_fetch, a.this_month_fetch, a.salary_month, a.audit_status, a.remark, a.audit_remark, a.is_modified, b.name employee_name, b.status employee_status\n" +
            "from salary_royalty_detail a, ins_employee b\n" +
            "where b.employee_id = a.employee_id\n" +
            "and a.order_id = ${orderId}"
    )
    List<EmployeeSalaryRoyaltyDetailDTO> querySalaries(@Param("orderId")Long orderId);


    @Select("select c.cust_id, c.cust_name, c.cust_no, a.id, a.order_id, a.employee_id, a.org_id, a.job_role, a.job_grade, a.strategy_id, a.order_status,a.fee_type, a.periods, a.type, a.item, a.value, a.total_royalty, a.already_fetch, a.this_month_fetch, a.salary_month, a.audit_status, a.remark, a.audit_remark, a.is_modified, b.name employee_name, b.status employee_status\n" +
            "from salary_royalty_detail a, ins_employee b, cust_base c, order_base d\n" +
            "${ew.customSqlSegment}"
    )
    List<OrderSalaryRoyaltyDetailDTO> queryOrderSalaryRoyaltyDetails(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select c.cust_id, c.cust_name, c.cust_no, a.id, a.order_id, a.employee_id, a.org_id, a.job_role, a.job_grade, a.strategy_id, a.order_status, a.fee_type, a.periods, a.type, a.item, a.value, a.total_royalty, a.already_fetch, a.this_month_fetch, a.salary_month, a.audit_status, a.remark, a.audit_remark, a.is_modified, b.name employee_name, b.status employee_status\n" +
            "from salary_royalty_detail a, ins_employee b, cust_base c, order_base d\n" +
            "${ew.customSqlSegment}"
    )
    IPage<OrderSalaryRoyaltyDetailDTO> queryOrderSalaryRoyaltyDetailPages(IPage<QueryRoyaltyDetailDTO> queryCondition, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select b.cust_id, b.cust_name, b.cust_no, b.cust_status, a.order_id, a.decorate_address, a.house_layout, a.indoor_area, a.shop_id, a.type, a.status " +
            "from cust_base b, order_base a \n" +
            "${ew.customSqlSegment}"
    )
    IPage<StatRoyaltyDetailDTO> statByCustOrder(IPage<QueryStatRoyaltyDTO> condition, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select sum(total_royalty - already_fetch) from salary_royalty_detail where employee_id = ${employeeId} and salary_month = ${salaryMonth} and audit_status = '2' ")
    Long sumRoyaltyByEmployeeId(@Param("employeeId")Long employeeId, @Param("salaryMonth")Integer salaryMonth);
}