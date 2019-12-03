package com.microtomato.hirun.modules.system.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.dto.AreaOrgNumDTO;
import com.microtomato.hirun.modules.organization.service.IOrgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 控制台控制器
 * @author: jinnian
 * @create: 2019-12-01 16:27
 **/

@RestController
@Slf4j
@RequestMapping("api/system/map")
public class MapController {

    @Autowired
    private IOrgService orgService;

    @RequestMapping("/mapByShopNum")
    @RestResult
    public List<AreaOrgNumDTO> searchEmployee(String areaType) {
        return this.orgService.countShopNum(areaType);
    }
}
