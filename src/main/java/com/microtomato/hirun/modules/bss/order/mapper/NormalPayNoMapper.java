package com.microtomato.hirun.modules.bss.order.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.bss.order.entity.dto.NonCollectFeeQueryDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.NormalPayNo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    @Select("select a.* from normal_pay_no a  " +
            "${ew.customSqlSegment}")
    IPage<NormalPayNo> queryPayNoInfo(IPage<NonCollectFeeQueryDTO> queryCondition, @Param(Constants.WRAPPER) Wrapper wrapper);

}
