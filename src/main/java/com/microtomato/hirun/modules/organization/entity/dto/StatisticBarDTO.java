package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 统计图表（柱状图）数据对象
 * @author: jinnian
 * @create: 2019-12-02 10:50
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StatisticBarDTO {

    private String title;

    private String subtitle;

    private List<String> legend;

    private List<String> xAxis;

    private List<StatisticBarValueDTO> yAxis;
}
