package com.microtomato.hirun.modules.bss.order.mapper;

import com.microtomato.hirun.modules.bss.order.entity.dto.OrderFeeDTO;
import com.microtomato.hirun.modules.bss.order.entity.po.Decorator;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 装修工人表 Mapper 接口
 * </p>
 *
 * @author sunxin
 * @since 2020-03-04
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface DecoratorMapper extends BaseMapper<Decorator> {

}
