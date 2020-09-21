package com.microtomato.hirun.modules.bss.customer.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustQueryCondDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.CustBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeExampleDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeInfoDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQueryConditionDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 客户信息 Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2020-02-01
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface CustBaseMapper extends BaseMapper<CustBase> {

    @Select("select a.cust_id,a.cust_name,a.mobile_no,a.cust_status,a.consult_time,a.cust_type,b.prepare_employee_id,b.prepare_time,b.cust_property,b.status as prepare_status,b.preparation_expire_time," +
            " b.ruling_employee_id,b.ruling_time,b.ruling_remark,c.house_id,c.house_mode,c.house_building,c.house_room_no," +
            " c.house_area,c.information_source,a.ploy_type,d.status as order_status,a.ploy_name,a.ploy_time,a.tel_consult_time," +
            " f.cust_service_employee_id,f.design_employee_id, d.order_id " +
            " from cust_base a left join cust_preparation b on (a.prepare_id=b.id), ins_project c,order_base d LEFT JOIN order_consult f on(d.order_id=f.order_id) " +
            " ${ew.customSqlSegment}"
    )
    IPage<CustInfoDTO> queryCustomerInfo(Page<CustQueryCondDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select a.cust_id,a.cust_name,a.mobile_no,a.cust_status,a.consult_time,a.cust_type,a.cust_no," +
            " b.prepare_employee_id,b.prepare_time,b.cust_property,b.status,b.preparation_expire_time,b.status,a.prepare_id,c.house_mode,c.house_area, " +
            " b.ruling_employee_id,b.ruling_time,b.ruling_remark,c.house_id,c.house_building,c.house_room_no,b.enter_employee_id,b.enter_time " +
            " from cust_base a left join cust_preparation b on (a.prepare_id=b.id) ," +
            " ins_project c " +
            " where a.cust_id=c.party_id and a.mobile_no=#{mobileNo}"
    )
    List<CustInfoDTO> queryCustomerInfoByMobile(@Param("mobileNo") String mobileNo);

    @Select("select c.cust_no,cust_name,b.decorate_address as house_address,b.order_id " +
            " from order_worker a,order_base b ,cust_base c " +
            " where a.order_id=b.order_id and a.role_id='15' and now() between a.start_date and a.end_date and b.cust_id=c.cust_id " +
            " and b.status='1' and c.cust_status='0'  " +
            " and a.employee_id=#{employeeId}")
    List<CustInfoDTO> queryCustomer4TransOrder(@Param("employeeId") Long employeeId);
}
