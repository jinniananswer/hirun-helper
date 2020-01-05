package com.microtomato.hirun.modules.organization.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.dto.OrgHrRelInfoDTO;
import com.microtomato.hirun.modules.organization.entity.po.OrgHrRel;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.microtomato.hirun.modules.organization.service.IOrgHrRelService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2019-11-27
 */
@RestController
@Slf4j
@RequestMapping("/api/organization/org-hr-rel")
public class OrgHrRelController {

    @Autowired
    private IOrgHrRelService orgHrRelServiceImpl;

    @GetMapping("/queryOrgHrRelList")
    @RestResult
    public IPage<OrgHrRelInfoDTO> queryOrgHrRelList(Long employeeId, Long orgSet, Integer page, Integer limit) {
        Page<OrgHrRel> orgHrRelPage = new Page<OrgHrRel>(page, limit);
        return orgHrRelServiceImpl.queryOrgHrRelList(employeeId,orgSet,orgHrRelPage);
    }

    @PostMapping("/updateOrgHrRel")
    @RestResult
    public boolean updateOrgHrRel(String ids, Long archiveManagerEmployeeId,Long relationManagerEmployeeId) {
        return orgHrRelServiceImpl.updateOrgHrRel(ids,archiveManagerEmployeeId,relationManagerEmployeeId);
    }

}
