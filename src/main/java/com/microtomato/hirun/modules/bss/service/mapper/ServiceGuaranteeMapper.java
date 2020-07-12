package com.microtomato.hirun.modules.bss.service.mapper;

import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.modules.bss.service.entity.po.ServiceGuarantee;

/**
 * (ServiceGuarantee)表数据库访问层
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-12 18:46:24
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface ServiceGuaranteeMapper extends BaseMapper<ServiceGuarantee> {

}