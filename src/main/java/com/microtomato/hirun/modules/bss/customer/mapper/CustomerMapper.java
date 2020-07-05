package com.microtomato.hirun.modules.bss.customer.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustQueryCondDTO;
import com.microtomato.hirun.modules.bss.customer.entity.dto.CustomerInfoDetailDTO;
import com.microtomato.hirun.modules.bss.customer.entity.po.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 客户信息 Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2020-04-20
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface CustomerMapper extends BaseMapper<Customer> {
    /**
     * sss
     * @param page
     * @param wrapper
     * @return
     */
    @Select("select a.cust_name as customer_name,a.mobile_no,a.wx_nick,a.identify_code as open_id,a.house_id,a.house_mode,a.house_area,a.house_counselor_id,a.cust_id,a.house_detail " +
            " from ins_customer a " +
            " ${ew.customSqlSegment}"
    )
    IPage<CustomerInfoDetailDTO> queryCustomerDetailInfo(Page<CustQueryCondDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
