package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeInfoDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeePenaltyDTO;
import com.microtomato.hirun.modules.organization.entity.po.EmployeePenalty;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2019-11-14
 */
@Storage
@DS("ins")
public interface EmployeePenaltyMapper extends BaseMapper<EmployeePenalty> {
    /**
     * 查询员工奖惩信息
     *
     * @param page
     * @param wrapper
     * @return
     */
    @Select("select a.name employee_name,a.employee_id,d.penalty_type,d.penalty_time," +
            " b.job_role,b.org_id, c.name org_name , d.penalty_score, d.penalty_content, d.remark, d.id, d.penalty_time from " +
            " ins_employee a, ins_employee_job_role b, ins_org c ,ins_employee_penalty d \n" +
            " ${ew.customSqlSegment}"
    )
    IPage<EmployeePenaltyDTO> selectEmployeePenaltyPage(Page<EmployeePenaltyDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
