package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.modules.organization.entity.po.Org;
import com.microtomato.hirun.modules.organization.mapper.OrgMapper;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-09-16
 */
@Slf4j
@Service
public class OrgServiceImpl extends ServiceImpl<OrgMapper, Org> implements IOrgService {

    @Override
    @Cacheable(value = "all-org")
    public List<Org> listAllOrgs() {
        List<Org> orgs = this.list(new QueryWrapper<Org>().lambda().eq(Org::getStatus, "0"));
        return orgs;
    }

    @Override
    public Org getBelongCompany(Long orgId) {
        return null;
    }

}
