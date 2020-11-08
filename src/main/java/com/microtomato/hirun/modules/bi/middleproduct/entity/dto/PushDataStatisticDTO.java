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

    private Long pushNum=0L;

    private Long openNum=0L;

    private String openRate="0.00%";

    private Long employeeId;

    private Long orgId;
}
