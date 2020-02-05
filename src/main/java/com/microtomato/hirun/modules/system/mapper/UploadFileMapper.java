package com.microtomato.hirun.modules.system.mapper;

import com.microtomato.hirun.modules.system.entity.po.UploadFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;

/**
 * <p>
 * 上传文件 Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2020-02-05
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface UploadFileMapper extends BaseMapper<UploadFile> {

}
