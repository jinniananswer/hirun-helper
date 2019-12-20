package com.microtomato.hirun.modules.organization.controller;

import com.microtomato.hirun.framework.annotation.RestResult;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.organization.entity.dto.TrainDTO;
import com.microtomato.hirun.modules.organization.service.ITrainDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2019-12-17
 */
@RestController
@Slf4j
@RequestMapping("/api/organization/train")
public class TrainController {

    @Autowired
    private ITrainDomainService trainDomainServiceImpl;

    @RequestMapping("/loadMyTrains")
    @RestResult
    public List<TrainDTO> loadMyTrains(Long employeeId) {
        if (employeeId == null) {
            employeeId = WebContextUtils.getUserContext().getEmployeeId();
        }

        return this.trainDomainServiceImpl.queryMyTrains(employeeId);
    }

}
