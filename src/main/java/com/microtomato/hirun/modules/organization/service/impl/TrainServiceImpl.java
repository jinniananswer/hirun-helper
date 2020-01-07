package com.microtomato.hirun.modules.organization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.dto.TrainDTO;
import com.microtomato.hirun.modules.organization.entity.po.Train;
import com.microtomato.hirun.modules.organization.mapper.TrainMapper;
import com.microtomato.hirun.modules.organization.service.ITrainService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
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

    @Autowired
    private IStaticDataService staticDataService;

    @Override
    public List<TrainDTO> queryByEmployeeId(Long employeeId) {
        List<TrainDTO> trains = this.trainMapper.queryByEmployeeId(employeeId);
        if (ArrayUtils.isNotEmpty(trains)) {
            for (TrainDTO train : trains) {
                train.setTypeName(this.staticDataService.getCodeName("TRAIN_TYPE", train.getType()));
            }
        }
        return trains;
    }
}
