package com.microtomato.hirun.modules.bss.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bss.config.entity.po.RoleAttentionStatusCfg;
import com.microtomato.hirun.modules.bss.config.mapper.RoleAttentionStatusCfgMapper;
import com.microtomato.hirun.modules.bss.config.service.IRoleAttentionStatusCfgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色关注的订单状态配置表 服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-11
 */
@Slf4j
@Service
public class RoleAttentionStatusCfgServiceImpl extends ServiceImpl<RoleAttentionStatusCfgMapper, RoleAttentionStatusCfg> implements IRoleAttentionStatusCfgService {

    @Override
    @Cacheable(value = "roleattention-cfg-roleId")
    public List<RoleAttentionStatusCfg> queryByRoleId(Long roleId) {
        return this.list(new QueryWrapper<RoleAttentionStatusCfg>().lambda().eq(RoleAttentionStatusCfg::getRoleId, roleId));
    }

    @Override
    @Cacheable(value = "roleattention-cfg-roleIds")
    public List<RoleAttentionStatusCfg> queryInRoleIds(List<Long> roleIds) {
        LambdaQueryWrapper<RoleAttentionStatusCfg> wrapper = new QueryWrapper<RoleAttentionStatusCfg>().lambda();
        wrapper.in(RoleAttentionStatusCfg::getRoleId, roleIds);
        return this.list(wrapper);
    }
}
