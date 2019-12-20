package com.microtomato.hirun.modules.organization.service;

import com.microtomato.hirun.modules.organization.entity.dto.TrainDTO;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 培训领域服务
 * @author: jinnian
 * @create: 2019-12-18 01:02
 **/
public interface ITrainDomainService {

    List<TrainDTO> queryMyTrains(Long employeeId);
}
