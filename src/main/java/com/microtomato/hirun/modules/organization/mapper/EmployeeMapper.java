package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustQueryCondDTO;
import com.microtomato.hirun.modules.bss.plan.entity.dto.AgentMonthPlanDTO;
import com.microtomato.hirun.modules.organization.entity.dto.*;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
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
@DataSource(DataSourceKey.INS)
public interface EmployeeMapper extends BaseMapper<Employee> {

    @Select("select a.employee_id,a.user_id,a.name,a.sex,a.identity_no,a.birthday_type,a.birthday,a.mobile_no,a.home_prov,a.home_city,a.home_region,a.home_address,a.native_prov,a.native_city,a.native_region,a.native_address,a.in_date,a.regular_date,a.destroy_date,a.destroy_way,a.destroy_reason,a.destroy_times,a.job_date,a.work_nature,a.workplace,a.education_level,a.first_education_level,a.major,a.school,a.school_type,a.tech_title,a.certificate_no,a.before_hirun_year,a.status,b.job_role_id,b.employee_id,b.job_role,b.discount_rate,b.is_main,b.job_role_nature,b.org_id,b.parent_employee_id,c.name orgName from ins_employee a, ins_employee_job_role b, ins_org c \n" +
            "where (a.name like concat('%',#{text},'%') or a.mobile_no like concat('%',#{text},'%'))\n" +
            " and a.status = '0' \n" +
            " and b.employee_id = a.employee_id\n" +
            " and (now() between b.start_date and b.end_date) \n" +
            " and b.is_main = '1' \n" +
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
            " b.job_role,b.org_id, c.name org_name,a.type,a.job_date,b.parent_employee_id,b.job_role_nature,x.age, x.company_age,a.birthday,x.job_age,b.discount_rate from " +
            " ins_org c, ins_employee a " +
            " LEFT JOIN ( select * from ins_employee_job_role k where k.job_role_id in(select max(i.job_role_id) from (select * from ins_employee_job_role h where is_main= '1' order by h.start_date desc) i\n" +
            " group by i.employee_id)) b on (a.employee_id=b.employee_id) " +
            " LEFT JOIN (SELECT y.employee_id,TIMESTAMPDIFF(YEAR,y.birthday,NOW()) as age,TIMESTAMPDIFF(YEAR,y.in_date,NOW()) as company_age, TIMESTAMPDIFF(YEAR,y.job_date,NOW()) as job_age from ins_employee y ) x\n" +
            " on (a.employee_id=x.employee_id)" +
            " ${ew.customSqlSegment}"
    )
    IPage<EmployeeInfoDTO> selectEmployeePage(Page<EmployeeQueryConditionDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 查询员工的下级员工
     */
    @Select("select a.employee_id,b.org_id from ins_employee a, ins_employee_job_role b " +
            " where a.employee_id=b.employee_id" +
            " and a.status='0'" +
            " and (now() between b.start_date and b.end_date)" +
            " and b.parent_employee_id=#{parentEmployeeId}")
    List<EmployeeInfoDTO> queryEmployeeByParentEmployeeId(Long parentEmployeeId);

    /**
     * 导出员工档案信息
     * @param wrapper
     * @return
     */
    @Select("select a.name,a.employee_id,a.sex,a.mobile_no ,a.identity_no,a.status employee_status, date_format(a.in_date,'%Y-%m-%d') in_date," +
            " b.job_role,b.org_id, c.name org_name,a.type,a.job_date,b.discount_rate,a.education_level,a.first_education_level,a.school_type," +
            " a.tech_title,a.native_prov,a.native_city,a.native_region,a.native_address,a.home_prov,a.home_city,a.home_region," +
            " a.home_address,b.parent_employee_id,b.job_role_nature,x.age, x.company_age,a.birthday,x.job_age," +
            " a.destroy_date,a.regular_date,a.destroy_reason,a.destroy_way,a.social_security_end from " +
            " ins_org c, ins_employee a " +
            " LEFT JOIN ( select * from ins_employee_job_role k where k.job_role_id in(select max(i.job_role_id) from (select * from ins_employee_job_role h where is_main='1' order by h.start_date desc) i\n" +
            " group by i.employee_id)) b on (a.employee_id=b.employee_id) " +
            " LEFT JOIN (SELECT y.employee_id,TIMESTAMPDIFF(YEAR,y.birthday,NOW()) as age,TIMESTAMPDIFF(YEAR,y.in_date,NOW()) as company_age, TIMESTAMPDIFF(YEAR,y.job_date,NOW()) as job_age from ins_employee y ) x\n" +
            " on (a.employee_id=x.employee_id)\n" +
            " ${ew.customSqlSegment}"
    )
    List<EmployeeInfoDTO> queryEmployeeList(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 按性别统计员工信息
     * @return
     */
    @Select("select sex name, count(1) num from ins_employee a, ins_employee_job_role b where b.employee_id = a.employee_id and a.status = '0' and (now() between b.start_date and b.end_date) and b.is_main = '1' and b.org_id in (${orgId}) group by sex ")
    List<EmployeePieStatisticDTO> countBySex(@Param("orgId")String orgId);

    /**
     * 按年龄段统计员工信息
     * @return
     */
    @Select("select age name, count(*) num from (" +
            "select case when TIMESTAMPDIFF(YEAR,birthday,NOW()) between 0 and 20 then '0-20' " +
            "            when TIMESTAMPDIFF(YEAR,birthday,NOW()) between 20.01 and 30 then '21-30' " +
            "            when TIMESTAMPDIFF(YEAR,birthday,NOW()) between 30.01 and 40 then '31-40' " +
            "            when TIMESTAMPDIFF(YEAR,birthday,NOW()) between 40.01 and 50 then '41-50' " +
            "            when TIMESTAMPDIFF(YEAR,birthday,NOW()) between 50.01 and 60 then '51-60' " +
            "            when TIMESTAMPDIFF(YEAR,birthday,NOW()) > 60 then '60+' " +
            "            else '无年龄信息' " +
            "       end as age " +
            "from ins_employee a, ins_employee_job_role b " +
            "where b.employee_id = a.employee_id and a.status = '0' and (now() between b.start_date and b.end_date) and is_main='1' and b.org_id in (${orgId})) temp_employee " +
            "group by age " +
            "order by age asc")
    List<EmployeePieStatisticDTO> countByAge(@Param("orgId")String orgId);

    /**
     * 按岗位性质统计员工信息
     * @return
     */
    @Select("select b.job_role_nature name, count(1) num from ins_employee a, ins_employee_job_role b where b.employee_id = a.employee_id and a.status = '0' and (now() between b.start_date and b.end_date) and b.is_main='1' and b.org_id in (${orgId}) group by b.job_role_nature ")
    List<EmployeePieStatisticDTO> countByJobRoleNature(@Param("orgId")String orgId);

    /**
     * 按司龄统计员工信息
     * @return
     */
    @Select("select company_age name, count(*) num from (" +
            "select case when TIMESTAMPDIFF(YEAR,in_date,NOW()) between 0 and 0.9 then '小于1年' " +
            "            when TIMESTAMPDIFF(YEAR,in_date,NOW()) between 1 and 3 then '1-3年' " +
            "            when TIMESTAMPDIFF(YEAR,in_date,NOW()) between 3.1 and 5 then '3-5年' " +
            "            when TIMESTAMPDIFF(YEAR,in_date,NOW()) between 5.1 and 10 then '5-10年' " +
            "            when TIMESTAMPDIFF(YEAR,in_date,NOW()) > 10 then '10年+' " +
            "       end as company_age " +
            "from ins_employee a, ins_employee_job_role b " +
            "where b.employee_id = a.employee_id and (now() between b.start_date and end_date) and a.in_date is not null and a.status = '0' and b.is_main='1' and b.org_id in (${orgId}) ) temp_employee " +
            "group by company_age " +
            "order by company_age asc")
    List<EmployeePieStatisticDTO> countByCompanyAge(@Param("orgId")String orgId);

    /**
     * 按最高学历统计员工信息
     * @return
     */
    @Select("select education_level name, count(1) num from ins_employee a, ins_employee_job_role b where b.employee_id = a.employee_id and (now() between b.start_date and b.end_date) and b.is_main='1' and b.org_id in (${orgId}) and a.status = '0' group by education_level ")
    List<EmployeePieStatisticDTO> countByEducationLevel(@Param("orgId")String orgId);

    /**
     * 按员工在职类型统计员工信息
     * @return
     */
    @Select("select type name, count(1) num from ins_employee a, ins_employee_job_role b where b.employee_id = a.employee_id and (now() between b.start_date and b.end_date) and b.is_main='1' and b.org_id in (${orgId}) and a.status = '0' group by type ")
    List<EmployeePieStatisticDTO> countByType(@Param("orgId")String orgId);

    /**
     * 根据员工id查询员工有效信息
     * @param employeeId
     * @return
     */
    @Select(" select a.employee_id,a.user_id,a.name,a.sex,a.identity_no,a.birthday_type,a.birthday,a.mobile_no,a.home_prov,a.home_city,a.home_region,a.home_address,a.native_prov,a.native_city,a.native_region,a.native_address,a.in_date,a.regular_date,a.destroy_date," +
            " a.destroy_way,a.destroy_reason,a.destroy_times,a.job_date,a.work_nature,a.workplace," +
            " a.education_level,a.first_education_level,a.major,a.school,a.school_type,a.tech_title," +
            " a.certificate_no,a.before_hirun_year,a.status,b.job_role_id,b.employee_id,b.job_role,b.discount_rate," +
            " b.is_main,b.job_role_nature,b.org_id,b.parent_employee_id,c.name orgName,b.job_grade,c.nature as org_nature " +
            " from ins_employee a, ins_employee_job_role b, ins_org c \n" +
            " where a.employee_id =#{employeeId} \n" +
            " and a.status = '0' \n" +
            " and b.employee_id = a.employee_id\n" +
            " and (now() between b.start_date and b.end_date) \n" +
            " and b.is_main = '1' \n" +
            " and c.org_id = b.org_id")
    EmployeeInfoDTO queryEmployeeInfoByEmployeeId(Long employeeId);

    /**
     * 统计一年内员工的入职数
     * @return
     */
    @Select("select ifnull(b.num, 0) value from (\n" +
            "  SELECT date_format(date_add(now(), INTERVAL 0 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -1 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -2 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -3 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -4 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -5 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -6 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -7 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -8 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -9 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -10 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -11 MONTH), '%Y%m') AS month\n" +
            ") a left join (select date_format(a.in_date, '%Y%m') as indate, count(1) num from ins_employee a, ins_employee_job_role b where b.employee_id = a.employee_id and b.is_main = '1' and b.start_date = (select max(start_date) from ins_employee_job_role c where c.employee_id = a.employee_id and c.is_main = '1') and b.org_id in (${orgId}) and a.in_date >= concat(date_format(date_add(now(), interval -11 month), '%Y-%m'), '-01') group by indate) b\n" +
            "  on b.indate = a.month\n" +
            "order by month ")
    List<Integer> countInOneYear(@Param("orgId")String orgId);

    /**
     * 统计一年内员工的离职数
     * @return
     */
    @Select("select ifnull(b.num, 0) value from (\n" +
            "  SELECT date_format(date_add(now(), INTERVAL 0 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -1 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -2 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -3 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -4 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -5 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -6 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -7 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -8 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -9 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -10 MONTH), '%Y%m') AS month\n" +
            "  UNION SELECT date_format(date_add(now(), INTERVAL -11 MONTH), '%Y%m') AS month\n" +
            ") a left join (select date_format(a.destroy_date, '%Y%m') as destroydate, count(1) num from ins_employee a, ins_employee_job_role b where b.employee_id = a.employee_id and b.is_main = '1' and b.start_date = (select max(start_date) from ins_employee_job_role c where c.employee_id = a.employee_id and c.is_main = '1') and b.org_id in (${orgId}) and a.destroy_date >= concat(date_format(date_add(now(), interval -11 month), '%Y-%m'), '-01') group by destroydate) b\n" +
            "  on b.destroydate = a.month\n" +
            "order by month ")
    List<Integer> countDestroyOneYear(@Param("orgId")String orgId);

    /**
     * 按部门统计员工数量
     * @return
     */
    @Select("SELECT y.org_id,IFNULL(y.job_role, 9999) AS job_role,IFNULL(y.job_role_nature, 0) AS job_role_nature,IFNULL(y.nature, 0) AS org_nature," +
            " IFNULL(y.job_grade, 0) AS job_grade,IFNULL(x.less_month_num, 0) AS less_month_num,IFNULL(x.more_month_num, 0) AS more_month_num," +
            " IFNULL(x.employee_sum, 0) AS employee_num,y.city,y.type AS org_type" +
            " FROM (" +
            "  SELECT DISTINCT g.org_id,IFNULL(h.job_role_nature, 0) as job_role_nature," +
            "  IFNULL(g.nature, 0) as nature,g.city as city,g.type as type," +
            "  case when ISNULL(h.job_grade)=1 ||  LENGTH(trim(h.job_grade))<1  then 0 else h.job_grade " +
            "  end as job_grade," +
            "  case when ISNULL(h.job_role)=1 ||  LENGTH(trim(h.job_role))<1  then 9999 else h.job_role" +
            "  end as job_role" +
            " FROM ins_org g" +
            " LEFT JOIN ins_employee_job_role h ON (g.org_id = h.org_id) " +
            " ) y" +
            " LEFT JOIN (" +
            " SELECT k.org_id,IFNULL(k.job_role,9999) as job_role,IFNULL(k.job_role_nature,0) as job_role_nature," +
            " IFNULL(k.nature,0) as nature,sum(less_num) AS less_month_num,sum(more_num) AS more_month_num," +
            " SUM(employee_num) AS employee_sum,k.job_grade" +
            " FROM (" +
            " SELECT" +
            "    CASE WHEN TIMESTAMPDIFF(MONTH, in_date, NOW()) BETWEEN 0 AND 9 AND EXISTS (SELECT 1 FROM ins_employee_job_role c WHERE c.employee_id = b.employee_id AND now() BETWEEN c.start_date AND c.end_date" +
            "              GROUP BY c.employee_id HAVING count(1) > 1 ) THEN '0.5'" +
            "         WHEN TIMESTAMPDIFF(MONTH, in_date, NOW()) BETWEEN 0 AND 9 AND EXISTS (SELECT 1 FROM ins_employee_job_role c WHERE c.employee_id = b.employee_id AND now() BETWEEN c.start_date AND c.end_date" +
            "              GROUP BY c.employee_id HAVING count(1) <= 1 ) THEN '1'" +
            "         ELSE '0'" +
            "    END AS less_num," +
            "    CASE WHEN TIMESTAMPDIFF(MONTH, in_date, NOW()) > 9 AND EXISTS (SELECT 1 FROM ins_employee_job_role c WHERE c.employee_id = b.employee_id AND now() BETWEEN c.start_date AND c.end_date" +
            "              GROUP BY c.employee_id HAVING count(1) > 1 ) THEN '0.5'" +
            "         WHEN TIMESTAMPDIFF(MONTH, in_date, NOW()) > 9 AND EXISTS (SELECT 1 FROM ins_employee_job_role c WHERE c.employee_id = b.employee_id AND now() BETWEEN c.start_date AND c.end_date" +
            "              GROUP BY c.employee_id HAVING count(1) <= 1) THEN '1'" +
            "         ELSE '0'" +
            "    END AS more_num, " +
            "    CASE WHEN EXISTS (SELECT 1 FROM ins_employee_job_role c WHERE c.employee_id = b.employee_id AND now() BETWEEN c.start_date AND c.end_date" +
            "              GROUP BY c.employee_id HAVING count(1) > 1 ) THEN '0.5'" +
            "         ELSE '1'" +
            "    END AS employee_num," +
            " b.org_id,b.job_role_nature,a.nature," +
            "    case when ISNULL(b.job_grade)=1 ||  LENGTH(trim(b.job_grade))<1  then 0 else b.job_grade" +
            "    end as job_grade," +
            "    case when ISNULL(b.job_role)=1 ||  LENGTH(trim(b.job_role))<1  then 9999 else b.job_role" +
            "    end as job_role" +
            " FROM ins_employee c,ins_org a,ins_employee_job_role b" +
            " WHERE b.employee_id = c.employee_id AND b.org_id = a.org_id AND now() BETWEEN b.start_date AND b.end_date AND c.`status` = '0' AND a.`status` = '0') k" +
            " GROUP BY k.org_id,k.job_role,k.job_role_nature,k.nature,k.job_grade ) x" +
            " ON (x.org_id = y.org_id and x.job_role=y.job_role and x.job_role_nature=y.job_role_nature" +
            " and x.job_grade=y.job_grade and x.nature=y.nature)")
    List<EmployeeQuantityStatDTO> countEmployeeQuantityByOrgId();


    /**
     *
     * @param parentEmployeeIds
     * @return
     */
    @Select("select a.employee_id,b.org_id from ins_employee a, ins_employee_job_role b " +
            " where a.employee_id=b.employee_id" +
            " and a.status='0'" +
            " and (now() between b.start_date and b.end_date)" +
            " and b.parent_employee_id in (#{parentEmployeeId})")
    List<EmployeeInfoDTO> queryEmployeeByParentEmployeeIds(String parentEmployeeIds);

    /**
     * 查询员工档案信息
     * @param page
     * @param wrapper
     * @return
     */
    @Select("select a.name,a.employee_id, date_format(a.in_date,'%Y-%m-%d') in_date," +
            " b.job_role,b.org_id,b.job_role_id, c.name org_name,a.type,a.job_date,b.parent_employee_id,b.job_role_nature from " +
            " ins_org c, ins_employee a, ins_employee_job_role b " +
            " ${ew.customSqlSegment}"
    )
    IPage<EmployeeInfoDTO> queryEmployee4BatchChange(Page<EmployeeQueryConditionDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 查询岗前考试未报名的员工信息
     * @param orgId
     * @param inDate
     * @return
     */
    @Select("select a.name,a.employee_id, date_format(a.in_date,'%Y-%m-%d') in_date, date_format(a.regular_date,'%Y-%m-%d') regular_date," +
            " b.job_role,b.org_id,b.job_role_nature,c.archive_manager_employee_id hr_employee_id from " +
            " ins_employee a, ins_employee_job_role b , ins_org_hr_rel c " +
            " where a.employee_id=b.employee_id " +
            " and a.status='0' and b.is_main='1' " +
            " and now() between b.start_date and b.end_date " +
            " and b.org_id=c.org_id" +
            " and now() between c.start_time and c.end_time " +
            " and b.org_id in (${orgId})" +
            " and a.in_date>'${inDate}'" +
            " and not exists(select 1 from ins_train_sign f where f.employee_id = a.employee_id  and f.status = '0')"
    )
    List<EmployeeInfoDTO> queryEmployeeRegular(@Param("orgId")String orgId,@Param("inDate") LocalDate inDate);

    /**
     * 查询岗前考试已报名的员工信息
     * @param orgId
     * @param inDate
     * @return
     */
    @Select("select a.name,a.employee_id, date_format(a.in_date,'%Y-%m-%d') in_date, date_format(a.regular_date,'%Y-%m-%d') regular_date," +
            " b.job_role,b.org_id,b.job_role_nature,c.archive_manager_employee_id hr_employee_id from " +
            " ins_employee a, ins_employee_job_role b , ins_org_hr_rel c " +
            " where a.employee_id=b.employee_id " +
            " and a.status='0' and b.is_main='1' " +
            " and now() between b.start_date and b.end_date " +
            " and b.org_id=c.org_id" +
            " and now() between c.start_time and c.end_time " +
            " and b.org_id in (${orgId})" +
            " and a.in_date>'${inDate}'" +
            " and exists(select 1 from ins_train_sign f where f.employee_id = a.employee_id  and f.status = '0')"
    )
    List<EmployeeInfoDTO> queryEmployeeExistsExam(@Param("orgId")String orgId,@Param("inDate") LocalDate inDate);


    @Select("select a.employee_id,a.name, b.org_id, b.job_role from ins_employee a, ins_employee_job_role b, ins_user_role c " +
            " where a.employee_id=b.employee_id" +
            " and c.user_id = a.user_id " +
            " and (now() between c.start_date and c.end_date) " +
            " and a.status='0'" +
            " and (now() between b.start_date and b.end_date)" +
            " and b.org_id in (${orgId}) " +
            " and c.role_id = #{roleId}")
    List<SimpleEmployeeDTO> querySimpleEmployees(@Param("roleId")Long roleId, @Param("orgId")String orgId);

    @Select("select a.employee_id,a.name, b.org_id, b.job_role from " +
            " ins_employee a, ins_employee_job_role b " +
            " ${ew.customSqlSegment}")
    List<SimpleEmployeeDTO> queryEmployee4Select(@Param(Constants.WRAPPER) Wrapper wrapper);


    @Select("select a.employee_id,a.name, b.org_id, b.job_role from ins_employee a, ins_employee_job_role b " +
            " where a.employee_id=b.employee_id" +
            " and a.status='0'" +
            " and (now() between b.start_date and b.end_date)" +
            " and b.org_id in (${orgId}) ")
    List<SimpleEmployeeDTO> querySimpleEmployeesByOrgId(@Param("orgId")String orgId);

    @Select("select a.employee_id, b.org_id " +
            " from ins_org c, ins_user_role e ,ins_employee a " +
            " LEFT JOIN ( select * from ins_employee_job_role k where k.job_role_id in(select max(i.job_role_id) from (select * from ins_employee_job_role h where is_main= '1') i group by i.employee_id)) b" +
            " on (a.employee_id=b.employee_id) "+
            " ${ew.customSqlSegment}"
    )
    List<SimpleEmployeeDTO> queryEmployeeByRoleAndOrg(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select * from ins_employee  ${ew.customSqlSegment}")
    IPage<Employee> queryNewEmployeeByPage(Page<EmployeeQueryDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
