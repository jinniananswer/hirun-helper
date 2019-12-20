package com.microtomato.hirun.modules.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.organization.entity.dto.TrainScoreDTO;
import com.microtomato.hirun.modules.organization.entity.po.TrainExamScore;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-12-17
 */
public interface ITrainExamScoreService extends IService<TrainExamScore> {

    List<TrainScoreDTO> queryScoreByEmployeeId(Long employeeId);
}
