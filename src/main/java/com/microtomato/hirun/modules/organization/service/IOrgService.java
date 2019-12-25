package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.modules.organization.entity.dto.AreaOrgNumDTO;
import com.microtomato.hirun.modules.organization.entity.po.Org;

import java.util.List;
import java.util.Map;

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

    List<TreeNode> listWithTree();

    void buildMap(List<TreeNode> nodeList, Map<String, TreeNode> nodeMap);

    List<AreaOrgNumDTO> countShopNum(String areaType);

}
