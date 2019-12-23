package com.microtomato.hirun.modules.organization.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.modules.organization.entity.dto.EmployeeTransitionStatDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import com.microtomato.hirun.modules.organization.service.IStatEmployeeTransitionService;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

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
    public List<EmployeeTransitionStatDTO> queryEmployeeTransitionStat(String queryTime, Long orgId){
        return statEmployeeTransitionServiceImpl.queryTransitionList(orgId,queryTime);
    }

}
