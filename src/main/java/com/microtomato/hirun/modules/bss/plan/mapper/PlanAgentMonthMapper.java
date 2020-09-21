package com.microtomato.hirun.modules.bss.plan.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.plan.entity.dto.AgentMonthAcutalDTO;
import com.microtomato.hirun.modules.bss.plan.entity.dto.AgentMonthPlanDTO;
import com.microtomato.hirun.modules.bss.plan.entity.po.PlanAgentMonth;
import com.microtomato.hirun.modules.bss.salary.entity.dto.SalaryMonthlyDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
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
            " sum(t.plan_funcb_count) as plan_funcb_count,SUM(t.plan_funcc_count) as plan_funcc_count, " +
            " SUM(t.plan_citycabin_count) as plan_citycabin_count,SUM(t.plan_measure_count) as  plan_measure_count, " +
            " SUM(t.plan_bind_design_count) as plan_bind_design_count" +
            " from plan_agent_month t "+
            " where month = ${month} and shop_id = ${shopId}"
    )
    PlanAgentMonth queryAgentPlanByShopId(@Param("shopId")Long shopId, @Param("month")Integer month);

    @Select(" select sum(t.plan_consult_count) as plan_consult_count,sum(t.plan_bind_agent_count) as plan_bind_agent_count," +
            " sum(t.plan_style_count) as plan_style_count,SUM(t.plan_funca_count) as plan_funca_count, " +
            " sum(t.plan_funcb_count) as plan_funcb_count,SUM(t.plan_funcc_count) as plan_funcc_count, " +
            " SUM(t.plan_citycabin_count) as plan_citycabin_count,SUM(t.plan_measure_count) as  plan_measure_count, " +
            " SUM(t.plan_bind_design_count) as plan_bind_design_count" +
            " from plan_agent_month t "+
            " where month = ${month} and company_id = ${companyId}"
    )
    PlanAgentMonth queryAgentPlanByCompanyId(@Param("companyId")Long companyId, @Param("month")Integer month);

    @Select(" select sum(t.plan_consult_count) as plan_consult_count,sum(t.plan_bind_agent_count) as plan_bind_agent_count," +
            " sum(t.plan_style_count) as plan_style_count,SUM(t.plan_funca_count) as plan_funca_count, " +
            " sum(t.plan_funcb_count) as plan_funcb_count,SUM(t.plan_funcc_count) as plan_funcc_count, " +
            " SUM(t.plan_citycabin_count) as plan_citycabin_count,SUM(t.plan_measure_count) as  plan_measure_count, " +
            " SUM(t.plan_bind_design_count) as plan_bind_design_count" +
            " from plan_agent_month t "+
            " where month = ${month}"
    )
    PlanAgentMonth queryAgentPlanByBu(@Param("month")Integer month);

    /**
     * 查询实际完成情况
     * @param employeeId
     * @param startTime
     * @param endTime
     * @return
     */
    @Select(" select count(1) as acutal_consult_count,sum(v.sm_count) as acutal_bind_agent_count, " +
            " SUM(city_Count) as acutal_citycabin_count,SUM(v.func_count) as acutal_funca_count," +
            " SUM(style_count) as acutal_style_count, SUM(design_count) as acutal_bind_design_count, " +
            " SUM(funcb_count) as acutal_funcb_count, SUM(funcc_count) as acutal_funcc_count" +
            " from ( " +
            " select link_employee_id as employee_id, " +
            " case WHEN EXISTS (select 1 from ins_project_original_action d where  d.`status`='1' and d.party_id=a.party_id and d.action_code='SMJRLC' " +
            "       and  d.finish_time BETWEEN #{startTime} and #{endTime} ) then '1' else 0 " +
            "    end as sm_count, "+
            " case WHEN EXISTS (select 1 from ins_project_original_action u where  u.`status`='1' and u.party_id=a.party_id and u.action_code='APSJS' " +
            "       and  u.finish_time BETWEEN #{startTime} and #{endTime} ) then '1' else 0 " +
            "    end as design_count, "+
            " case WHEN EXISTS (select 1 from ins_scan_citycabin x where b.project_id=x.project_id and x.employee_id=c.link_employee_id " +
            "      and x.experience_Time BETWEEN #{startTime} and #{endTime} ) then '1' else 0 " +
            "    end as city_count, " +
            " case WHEN EXISTS (SELECT 1 FROM ins_blueprint_action m where m.open_id=a.open_id and c.link_employee_id=m.rel_employee_id" +
            "      and m.funcprint_create_time BETWEEN #{startTime} and #{endTime} ) then '1' else 0" +
            "    end as func_count," +
            " case WHEN EXISTS (SELECT 1 FROM ins_blueprint_action p where p.open_id=a.open_id and c.link_employee_id=p.rel_employee_id" +
            "      and p.b_create_time BETWEEN #{startTime} and #{endTime} ) then '1' else 0" +
            "    end as funcb_count," +
            " case WHEN EXISTS (SELECT 1 FROM ins_blueprint_action q where q.open_id=a.open_id and c.link_employee_id=q.rel_employee_id" +
            "      and q.c_create_time BETWEEN #{startTime} and #{endTime} ) then '1' else 0" +
            "    end as funcc_count," +
            " case WHEN EXISTS (SELECT 1 FROM ins_blueprint_action n where n.open_id=a.open_id and c.link_employee_id=n.rel_employee_id" +
            "      and n.styleprint_create_time BETWEEN #{startTime} and #{endTime} ) then '1' else 0 " +
            "   end as style_count " +
            " from ins_party a,ins_project b,ins_project_linkman c " +
            "  where a.party_id=b.party_id and b.project_id=c.project_id and a.party_status='0' " +
            "   and c.ROLE_TYPE = 'CUSTOMERSERVICE' " +
            "  and a.consult_time BETWEEN #{startTime} and #{endTime} " +
            "  and c.link_employee_id in ( ${employeeId} )" +
            "  ) v "
    )
    AgentMonthAcutalDTO queryAgentAcutalByEmployeeId(@Param("employeeId")String employeeId, @Param("startTime") String startTime, @Param("endTime")String endTime);
}