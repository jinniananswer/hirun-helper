package com.microtomato.hirun.modules.organization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import com.microtomato.hirun.modules.organization.entity.dto.AreaOrgNumDTO;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jinnian
 * @since 2019-09-16
 */
@Storage
@DS("ins")
public interface OrgMapper extends BaseMapper<Org> {

    @Select("SELECT city area, count(1) value from ins_org where type = '4' and status = '0' group by city")
    List<AreaOrgNumDTO> countShopNumByCity();

    @Select("SELECT province area, count(1) value from ins_org where type = '4' and status = '0' group by province")
    List<AreaOrgNumDTO> countShopNumByProvince();
}
