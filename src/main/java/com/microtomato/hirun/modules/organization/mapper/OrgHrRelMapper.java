package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.organization.entity.dto.OrgHrRelInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.OrgHrRel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuhui
 * @since 2019-11-27
 */
@Storage
@DataSource(DataSourceKey.INS)
public interface OrgHrRelMapper extends BaseMapper<OrgHrRel> {
    @Select("select * from ins_org_hr_rel a \n" +
            " ${ew.customSqlSegment}"
    )
    IPage<OrgHrRelInfoDTO> queryOrgHrRelPage(Page<OrgHrRel> page, @Param(Constants.WRAPPER) Wrapper wrapper);

}
