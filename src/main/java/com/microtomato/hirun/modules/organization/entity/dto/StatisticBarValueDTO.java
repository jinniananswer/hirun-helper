package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 统计图表柱状图值数据对象
 * @author: jinnian
 * @create: 2019-12-02 10:56
 **/
@Data
public class StatisticBarValueDTO {

    private String name;

    private List<Integer> data;
}
