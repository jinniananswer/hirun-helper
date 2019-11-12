package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeExampleDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2019-09-24
 */
@Storage
@DS("ins")
public interface EmployeeMapper extends BaseMapper<Employee> {

    @Select("select a.employee_id,a.user_id,a.name,a.sex,a.identity_no,a.birthday_type,a.birthday,a.mobile_no,a.home_prov,a.home_city,a.home_region,a.home_address,a.native_prov,a.native_city,a.native_region,a.native_address,a.in_date,a.regular_date,a.destroy_date,a.destroy_way,a.destroy_reason,a.destroy_times,a.job_date,a.work_nature,a.workplace,a.education_level,a.first_education_level,a.major,a.school,a.school_type,a.tech_title,a.certificate_no,a.before_hirun_year,a.status,b.job_role_id,b.employee_id,b.job_role,b.discount_rate,b.is_main,b.job_role_nature,b.org_id,b.parent_employee_id,c.name orgName from ins_employee a, ins_employee_job_role b, ins_org c \n" +
            "where (a.name like concat('%',#{text},'%') or a.mobile_no like concat('%',#{text},'%'))\n" +
            " and a.status = '0' \n" +
            " and b.employee_id = a.employee_id\n" +
            " and (now() between b.start_date and b.end_date) \n" +
            " and c.org_id = b.org_id")
    List<EmployeeInfoDTO> searchByNameMobileNo(String text);


    /**
     * 测试（后期删除）
     *
     * @param page
     * @param wrapper
     * @return
     */
    @Select("SELECT a.name, a.employee_id, a.sex, a.mobile_no, a.identity_no, a.status, date_format( a.in_date, '%Y-%m-%d' ) in_date, b.job_role, c.org_id, c.name org_name\n" +
        "FROM ins_employee a, ins_employee_job_role b, ins_org c\n" +
        "${ew.customSqlSegment}")
    IPage<EmployeeExampleDTO> selectEmployeePageExample(Page<EmployeeExampleDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 查询员工档案信息
     * @param page
     * @param wrapper
     * @return
     */
    @Select("select a.name,a.employee_id,a.sex,a.mobile_no ,a.identity_no,a.status employee_status, date_format(a.in_date,'%Y-%m-%d') in_date," +
            " b.job_role,b.org_id, c.name org_name from " +
            " ins_employee a, ins_employee_job_role b, ins_org c \n" +
            " ${ew.customSqlSegment}"
    )
    IPage<EmployeeInfoDTO> selectEmployeePage(Page<EmployeeInfoDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 查询员工的下级员工
     */
    @Select("select a.* from ins_employee a, ins_employee_job_role b " +
            " where a.employee_id=b.employee_id" +
            " and a.status='0'" +
            " and (now() between b.start_date and b.end_date)" +
            " and b.parent_employee_id=#{parentEmployeeId}")
    List<Employee> queryEmployeeByParentEmployeeId(Long parentEmployeeId);
}
