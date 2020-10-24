package com.microtomato.hirun.modules.bi.middleproduct.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: hirun-helper
 * @description:
 * @author: jinnian
 * @create: 2020-10-24 14:31
 **/
@Getter
@Setter
public class PushDataStatisticDTO {

    private String name;

    private Long pushNum;

    private Long openNum;

    private Double openRate;
}
