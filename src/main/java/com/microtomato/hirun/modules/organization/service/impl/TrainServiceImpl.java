package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.organization.entity.dto.TrainDTO;
import com.microtomato.hirun.modules.organization.entity.po.Train;
import com.microtomato.hirun.modules.organization.mapper.TrainMapper;
import com.microtomato.hirun.modules.organization.service.ITrainService;
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
public class TrainServiceImpl extends ServiceImpl<TrainMapper, Train> implements ITrainService {

    @Autowired
    private TrainMapper trainMapper;

    @Override
    public List<TrainDTO> queryByEmployeeId(Long employeeId) {
        List<TrainDTO> trains = this.trainMapper.queryByEmployeeId(employeeId);
        return trains;
    }
}
