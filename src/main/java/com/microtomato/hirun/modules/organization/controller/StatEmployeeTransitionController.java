package com.microtomato.hirun.modules.organization.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import com.microtomato.hirun.modules.organization.service.IStatEmployeeTransitionService;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuhui
 * @since 2019-12-22
 */
@RestController
@Slf4j
@RequestMapping("/api/organization/stat-employee-transition")
public class StatEmployeeTransitionController {

    @Autowired
    private IStatEmployeeTransitionService statEmployeeTransitionServiceImpl;

    @GetMapping("/queryEmployeeTransitionStat")
    @RestResult
    public List<Map<String,String>> queryEmployeeTransitionStat(String queryTime, String orgId){
        return statEmployeeTransitionServiceImpl.queryTransitionList(orgId,queryTime);
    }

}
