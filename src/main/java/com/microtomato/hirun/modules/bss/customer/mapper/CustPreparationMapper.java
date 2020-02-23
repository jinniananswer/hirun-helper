package com.microtomato.hirun.modules.bss.customer.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustInfoDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustPreparationDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustQueryCondDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.CustPreparation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2020-02-01
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface CustPreparationMapper extends BaseMapper<CustPreparation> {
    /**
     * 根据报备号码查询报备历史记录
     * @param mobileNo
     * @return
     */
    @Select("select b.prepare_employee_id,b.prepare_time,a.consult_time,b.status as prepare_status,b.enter_employee_id,b.remark,b.house_id " +
            " from cust_base a, cust_preparation b\n" +
            " where a.cust_id=b.cust_id \n" +
            " and a.mobile_no=#{mobileNo}\n")
    List<CustPreparationDTO> loadPreparationHistory(String mobileNo);

    @Select("select  b.id, a.cust_id,a.cust_name,a.mobile_no,b.prepare_employee_id,b.prepare_time,b.cust_property,b.status,b.preparation_expire_time," +
            " b.ruling_employee_id,b.ruling_time,b.ruling_remark,b.referee_info, " +
            " b.enter_employee_id,b.enter_time" +
            " from cust_base a left join cust_preparation b on (a.cust_id=b.cust_id) " +
            " ${ew.customSqlSegment}"
    )
    List<CustPreparationDTO> queryCustomerPreparation(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select a.cust_id,a.cust_name,a.mobile_no,b.prepare_employee_id,b.prepare_time,c.house_mode,c.house_id,c.house_building,c.house_room_no" +
            " from cust_base a , cust_preparation b , ins_project c" +
            " where a.cust_id=b.cust_id and a.cust_id=c.party_id" +
            " and a.mobile_no=#{mobileNo}" +
            " and b.prepare_time > #{limitTime}"
    )
    List<CustPreparationDTO> queryPrepareByTime(@Param("mobileNo")String mobileNo, @Param("limitTime") LocalDateTime limitTime);

    @Select("select  b.id, a.cust_id,a.cust_name,a.mobile_no,b.prepare_employee_id,b.prepare_time,b.cust_property,b.status as prepare_status,b.preparation_expire_time," +
            " b.ruling_employee_id,b.ruling_time,b.ruling_remark,b.referee_info,c.house_id,c.house_building,c.house_room_no,c.house_area, c.house_mode," +
            " b.enter_employee_id,b.enter_time" +
            " from cust_base a,cust_preparation b,ins_project c  " +
            " where a.cust_id=b.cust_id and a.cust_id=c.party_id " +
            " and a.cust_id=#{custId}" +
            " and b.status=#{status}" +
            " order by b.update_time desc"
    )
    List<CustPreparationDTO> queryPrepareByCustIdAndStatus(@Param("custId")Long custId, @Param("status") String status);
}
