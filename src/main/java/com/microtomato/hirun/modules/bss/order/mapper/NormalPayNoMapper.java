package com.microtomato.hirun.modules.bss.order.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.modules.bss.order.entity.po.NormalPayNo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 非主营支付流水表 Mapper 接口
 * </p>
 *
 * @author sunxin
 * @since 2020-03-26
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface NormalPayNoMapper extends BaseMapper<NormalPayNo> {

    @Select("select id, pay_no, pay_date, total_money, audit_status, audit_employee_id, audit_comment, start_date, end_date, remark, pay_employee_id, org_id, create_user_id, create_time, update_user_id, update_time, id, pay_no, pay_date, total_money, audit_status, audit_employee_id, audit_comment, start_date, end_date, remark, pay_employee_id, org_id, create_user_id, create_time, update_user_id, update_time from normal_pay_no a  " +
            "${ew.customSqlSegment}")
    List<NormalPayNo> queryPayNoInfo( @Param(Constants.WRAPPER) Wrapper wrapper);

}
