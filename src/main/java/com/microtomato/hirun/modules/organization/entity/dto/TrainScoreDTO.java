package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 培训成绩数据传输对象
 * @author: jinnian
 * @create: 2019-12-17 20:49
 **/
@Data
public class TrainScoreDTO {

    private Long trainId;

    private String item;

    private String score;
}
