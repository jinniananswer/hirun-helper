package com.microtomato.hirun.modules.system.mapper;

import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.modules.system.entity.po.ExcelImportError;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;

/**
 * <p>
 * 导入异常数据 Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2019-11-22
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface ExcelImportErrorMapper extends BaseMapper<ExcelImportError> {

}
