package com.microtomato.hirun.modules.bss.plan.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.plan.entity.dto.AgentMonthPlanDTO;
import com.microtomato.hirun.modules.bss.plan.entity.po.PlanAgentMonth;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryMonthlyDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (PlanAgentMonth)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-06-30 00:13:37
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface PlanAgentMonthMapper extends BaseMapper<PlanAgentMonth> {
    @Select("select a.name, a.employee_id," +
            " b.job_role,b.org_id, c.name org_name ,d.plan_consult_count,d.plan_bind_agent_count,d.plan_style_count,d.plan_funca_count,d.plan_funcb_count,d.plan_funcc_count, " +
            " d.plan_citycabin_count,d.plan_measure_count,d.plan_bind_design_count,d.id " +
            " from ins_org c, ins_user_role e ,ins_employee a " +
            " LEFT JOIN ( select * from ins_employee_job_role k where k.job_role_id in(select max(i.job_role_id) from (select * from ins_employee_job_role h where is_main= '1') i group by i.employee_id)) b" +
            " on (a.employee_id=b.employee_id) "+
            " left join (select * from plan_agent_month where month = ${month}) d on (d.employee_id = a.employee_id)"+
            " ${ew.customSqlSegment}"
    )
    List<AgentMonthPlanDTO> queryAgentPlan(@Param(Constants.WRAPPER) Wrapper wrapper, @Param("month")Integer month);

    @Select(" select sum(t.plan_consult_count) as plan_consult_count,sum(t.plan_bind_agent_count) as plan_bind_agent_count," +
            " sum(t.plan_style_count) as plan_style_count,SUM(t.plan_funca_count) as plan_funca_count, " +
            " sum(t.plan_funcb_count) as plan_funcb_count,SUM(t.plan_funcc_count) as plan_funcb_count, " +
            " SUM(t.plan_citycabin_count) as plan_citycabin_count,SUM(t.plan_measure_count) as  plan_measure_count, " +
            " SUM(t.plan_bind_design_count) as plan_bind_design_count" +
            " from plan_agent_month t "+
            " where month = ${month} and shop_id = ${shopId}"
    )
    PlanAgentMonth queryAgentPlanByShopId(@Param("shopId")Long shopId, @Param("month")Integer month);

    @Select(" select sum(t.plan_consult_count) as plan_consult_count,sum(t.plan_bind_agent_count) as plan_bind_agent_count," +
            " sum(t.plan_style_count) as plan_style_count,SUM(t.plan_funca_count) as plan_funca_count, " +
            " sum(t.plan_funcb_count) as plan_funcb_count,SUM(t.plan_funcc_count) as plan_funcb_count, " +
            " SUM(t.plan_citycabin_count) as plan_citycabin_count,SUM(t.plan_measure_count) as  plan_measure_count, " +
            " SUM(t.plan_bind_design_count) as plan_bind_design_count" +
            " from plan_agent_month t "+
            " where month = ${month} and company_id = ${companyId}"
    )
    PlanAgentMonth queryAgentPlanByCompanyId(@Param("companyId")Long companyId, @Param("month")Integer month);
}