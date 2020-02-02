package com.microtomato.hirun.modules.bss.customer.mapper;

import com.microtomato.hirun.modules.bss.customer.entity.dto.CustPreparationDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.CustPreparation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import org.apache.ibatis.annotations.Select;

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
    @Select("select b.prepare_employee_id,b.prepare_time,a.consult_time,b.status,b.enter_employee_id,b.remark" +
            " from cust_base a, cust_preparation b\n" +
            " where a.cust_id=b.cust_id \n" +
            " and a.mobile_no=#{mobileNo}\n")
    List<CustPreparationDTO> loadPreparationHistory(String mobileNo);
}
