package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.po.Org;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-09-16
 */
public interface IOrgService extends IService<Org> {

    /**
     * 获取所有组织
     *
     * @return 组织列表
     */
    List<Org> listAllOrgs();

    /**
     * 根据某组织获取其所属公司
     * @param orgId
     * @return
     */
    Org getBelongCompany(Long orgId);
}
