package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.organization.entity.dto.TrainScoreDTO;
import com.microtomato.hirun.modules.organization.entity.po.TrainExamScore;
import com.microtomato.hirun.modules.organization.mapper.TrainExamScoreMapper;
import com.microtomato.hirun.modules.organization.service.ITrainExamScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-12-17
 */
@Slf4j
@Service
public class TrainExamScoreServiceImpl extends ServiceImpl<TrainExamScoreMapper, TrainExamScore> implements ITrainExamScoreService {

    @Autowired
    private TrainExamScoreMapper trainExamScoreMapper;

    @Override
    public List<TrainScoreDTO> queryScoreByEmployeeId(Long employeeId) {
        return this.trainExamScoreMapper.queryScoreByEmployeeId(employeeId);
    }
}
