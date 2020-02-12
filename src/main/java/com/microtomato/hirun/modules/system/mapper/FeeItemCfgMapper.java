package com.microtomato.hirun.modules.system.mapper;

import com.microtomato.hirun.modules.system.entity.po.FeeItemCfg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 费用项配置表 Mapper 接口
 * </p>
 *
 * @author sunxin
 * @since 2020-02-06
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface FeeItemCfgMapper extends BaseMapper<FeeItemCfg> {

    /**
     * 查询对应feeItemId的name
     */
    @Select("select a.employee_id,b.org_id from sys_fee_item_cfg a " +
            " where a.status='0'" +
            " and id=#{feeItemId}")
    FeeItemCfg selectById(Long feeItemId);
}
