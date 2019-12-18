package com.microtomato.hirun.modules.organization.service.impl;

import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.organization.entity.dto.TrainDTO;
import com.microtomato.hirun.modules.organization.entity.dto.TrainScoreDTO;
import com.microtomato.hirun.modules.organization.service.ITrainDomainService;
import com.microtomato.hirun.modules.organization.service.ITrainExamScoreService;
import com.microtomato.hirun.modules.organization.service.ITrainService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 培训相关领域服务
 * @author: jinnian
 * @create: 2019-12-18 01:05
 **/
@Service
@Slf4j
public class TrainDomainServiceImpl implements ITrainDomainService {

    @Autowired
    private ITrainService trainService;

    @Autowired
    private ITrainExamScoreService trainExamScoreService;

    @Autowired
    private IStaticDataService staticDataService;

    @Override
    public List<TrainDTO> queryMyTrains(Long employeeId) {
        List<TrainDTO> trains = this.trainService.queryByEmployeeId(employeeId);
        if (ArrayUtils.isNotEmpty(trains)) {
            List<TrainScoreDTO> trainScores = this.trainExamScoreService.queryScoreByEmployeeId(employeeId);

            if (ArrayUtils.isNotEmpty(trainScores)) {
                for (TrainDTO train : trains) {
                    train.setTypeName(this.staticDataService.getCodeName("TRAIN_TYPE", train.getType()));

                    List<TrainScoreDTO> trainScoreList = new ArrayList<TrainScoreDTO>();
                    for (TrainScoreDTO trainScore : trainScores) {
                        if (train.getTrainId().equals(trainScore.getTrainId())) {
                            trainScoreList.add(trainScore);
                        }
                    }
                    train.setScores(trainScoreList);
                    boolean pass = true;
                    if (ArrayUtils.isEmpty(trainScoreList)) {
                        pass = false;
                        train.setPass(pass);
                        continue;
                    }
                    for (TrainScoreDTO trainScore : trainScoreList) {
                        String score = trainScore.getScore();
                        if (StringUtils.isBlank(score) || Double.parseDouble(score) < 80) {
                            pass = false;
                            break;
                        }
                    }
                    train.setPass(pass);
                }
            }
        }

        return trains;
    }
}
