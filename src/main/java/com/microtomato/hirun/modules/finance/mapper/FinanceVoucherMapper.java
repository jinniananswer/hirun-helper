package com.microtomato.hirun.modules.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.supply.entity.dto.QuerySupplyOrderDTO;
import com.microtomato.hirun.modules.bss.supply.entity.dto.SupplyOrderDTO;
import com.microtomato.hirun.modules.finance.entity.dto.FinanceVoucherDTO;
import com.microtomato.hirun.modules.finance.entity.dto.QueryVoucherAuditDTO;
import com.microtomato.hirun.modules.finance.entity.po.FinanceVoucher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 财务领款单表(FinanceVoucher)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-25 21:25:21
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface FinanceVoucherMapper extends BaseMapper<FinanceVoucher> {

    @Select("select a.total_money,b.supplier_id,b.project_id,a.id  " +
            "from finance_voucher a,finance_voucher_item b where  a.id=b.voucher_id and a.voucher_type =  #{voucherType} and a.audit_status =#{status}" )
    IPage<FinanceVoucherDTO> queryVoucherInfo(IPage<QueryVoucherAuditDTO> queryCondtion, @Param("voucherType") String voucherType, @Param("status")String status);


}