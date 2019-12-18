package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.dto.TrainDTO;
import com.microtomato.hirun.modules.organization.entity.po.Train;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-12-17
 */
public interface ITrainService extends IService<Train> {

    List<TrainDTO> queryByEmployeeId(Long employeeId);
}
