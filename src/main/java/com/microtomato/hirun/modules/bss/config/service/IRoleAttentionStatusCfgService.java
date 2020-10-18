package com.microtomato.hirun.modules.bss.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.bss.config.entity.po.RoleAttentionStatusCfg;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * <p>
 * 角色关注的订单状态配置表 服务类
 * </p>
 *
 * @author jinnian
 * @since 2020-02-11
 */
public interface IRoleAttentionStatusCfgService extends IService<RoleAttentionStatusCfg> {

    List<RoleAttentionStatusCfg> queryByRoleId(Long roleId);

    List<RoleAttentionStatusCfg> queryInRoleIds(List<Long> roleIds);

    @Cacheable(value = "roleattention-cfg-statusid")
    RoleAttentionStatusCfg getByStatusId(Long statusId);
}
