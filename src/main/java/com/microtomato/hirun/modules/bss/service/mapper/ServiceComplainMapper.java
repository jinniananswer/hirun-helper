package com.microtomato.hirun.modules.bss.service.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.service.entity.dto.ComplainOrderDTO;
import com.microtomato.hirun.modules.bss.service.entity.dto.ServicePendingOrderDTO;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceComplain;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeInfoDTO;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeQueryConditionDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (ServiceComplain)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface ServiceComplainMapper extends BaseMapper<ServiceComplain> {

    @Select("select c.cust_name,b.decorate_address,'2' as type,v.complain_no as code,v.`status`,b.order_id,b.cust_id " +
            " from order_base b,cust_base c," +
            " (select a.customer_id,a.complain_no,a.`status` from service_complain a" +
            "  where a.`status` in (${statuses}) " +
            "  and a.accept_employee_id=${employeeId} " +
            " group by a.customer_id,a.complain_no,a.`status` order by a.complain_time desc) v " +
            " where b.cust_id=c.cust_id and v.customer_id=c.cust_id "
    )
    List<ServicePendingOrderDTO> queryComplainPendingOrders(@Param("employeeId") Long employeeId, @Param("statuses")String statuses);


    @Select("select a.cust_name,a.cust_no,a.mobile_no,b.houses_id as house_id,c.complain_type,c.complain_way,c.complain_time, " +
            " c.complain_content,c.deal_date,c.deal_result,c.follow_trace,c.accept_employee_id,b.order_id,c.complain_no,c.status  " +
            " from cust_base a,order_base b,service_complain c " +
            " ${ew.customSqlSegment}"
    )
    List<ComplainOrderDTO> queryComplainAllRecord( @Param(Constants.WRAPPER) Wrapper wrapper);

}