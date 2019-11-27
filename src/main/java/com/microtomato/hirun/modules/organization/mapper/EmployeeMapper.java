package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.modules.organization.entity.dto.*;
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
            " b.job_role,b.org_id, c.name org_name,a.type,a.job_date from " +
            " ins_org c, ins_employee a " +
            " LEFT JOIN ( select * from ins_employee_job_role k where k.job_role_id in(select max(i.job_role_id) from (select * from ins_employee_job_role h order by h.start_date desc) i\n" +
            " group by i.employee_id)) b on (a.employee_id=b.employee_id) \n" +
            " ${ew.customSqlSegment}"
    )
    IPage<EmployeeInfoDTO> selectEmployeePage(Page<EmployeeQueryConditionDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 查询员工的下级员工
     */
    @Select("select a.* from ins_employee a, ins_employee_job_role b " +
            " where a.employee_id=b.employee_id" +
            " and a.status='0'" +
            " and (now() between b.start_date and b.end_date)" +
            " and b.parent_employee_id=#{parentEmployeeId}")
    List<Employee> queryEmployeeByParentEmployeeId(Long parentEmployeeId);

    /**
     * 导出员工档案信息
     * @param wrapper
     * @return
     */
    @Select("select a.name,a.employee_id,a.sex,a.mobile_no ,a.identity_no,a.status employee_status, date_format(a.in_date,'%Y-%m-%d') in_date," +
            " b.job_role,b.org_id, c.name org_name,a.type,a.job_date from " +
            " ins_org c, ins_employee a " +
            " LEFT JOIN ( select * from ins_employee_job_role k where k.job_role_id in(select max(i.job_role_id) from (select * from ins_employee_job_role h order by h.start_date desc) i\n" +
            " group by i.employee_id)) b on (a.employee_id=b.employee_id) \n" +
            " ${ew.customSqlSegment}"
    )
    List<EmployeeInfoDTO> queryEmployeeList(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 按性别统计员工信息
     * @return
     */
    @Select("select sex name, count(1) num from ins_employee where status = '0' group by sex ")
    List<EmployeePieStatisticDTO> countBySex();

    /**
     * 按年龄段统计员工信息
     * @return
     */
    @Select("select age name, count(*) num from (" +
            "select case when TIMESTAMPDIFF(YEAR,birthday,NOW()) between 0 and 20 then '0-20' " +
            "            when TIMESTAMPDIFF(YEAR,birthday,NOW()) between 21 and 30 then '21-30' " +
            "            when TIMESTAMPDIFF(YEAR,birthday,NOW()) between 31 and 40 then '31-40' " +
            "            when TIMESTAMPDIFF(YEAR,birthday,NOW()) between 41 and 50 then '41-50' " +
            "            when TIMESTAMPDIFF(YEAR,birthday,NOW()) between 51 and 60 then '51-60' " +
            "            when TIMESTAMPDIFF(YEAR,birthday,NOW()) > 60 then '60+' " +
            "       end as age " +
            "from ins_employee " +
            "where birthday is not null and status = '0') temp_employee " +
            "group by age " +
            "order by age asc")
    List<EmployeePieStatisticDTO> countByAge();

    /**
     * 按岗位性质统计员工信息
     * @return
     */
    @Select("select b.job_role_nature name, count(1) num from ins_employee a, ins_employee_job_role b where b.employee_id = a.employee_id and a.status = '0' and now() between b.start_date and b.end_date group by b.job_role_nature ")
    List<EmployeePieStatisticDTO> countByJobRoleNature();

    /**
     * 按司龄统计员工信息
     * @return
     */
    @Select("select company_age name, count(*) num from (" +
            "select case when TIMESTAMPDIFF(YEAR,in_date,NOW()) between 0 and 1 then '小于1年' " +
            "            when TIMESTAMPDIFF(YEAR,in_date,NOW()) between 1 and 3 then '1-3年' " +
            "            when TIMESTAMPDIFF(YEAR,in_date,NOW()) between 3.1 and 5 then '3-5年' " +
            "            when TIMESTAMPDIFF(YEAR,in_date,NOW()) between 5.1 and 10 then '5-10年' " +
            "            when TIMESTAMPDIFF(YEAR,in_date,NOW()) > 10 then '10年+' " +
            "       end as company_age " +
            "from ins_employee " +
            "where in_date is not null and status = '0') temp_employee " +
            "group by company_age " +
            "order by company_age asc")
    List<EmployeePieStatisticDTO> countByCompanyAge();

    /**
     * 按最高学历统计员工信息
     * @return
     */
    @Select("select education_level name, count(1) num from ins_employee where status = '0' group by education_level ")
    List<EmployeePieStatisticDTO> countByEducationLevel();

    /**
     * 按员工在职类型统计员工信息
     * @return
     */
    @Select("select type name, count(1) num from ins_employee where status = '0' group by type ")
    List<EmployeePieStatisticDTO> countByType();
}
