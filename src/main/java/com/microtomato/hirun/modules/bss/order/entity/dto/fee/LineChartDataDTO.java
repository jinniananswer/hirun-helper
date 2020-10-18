package com.microtomato.hirun.modules.bss.order.entity.dto.fee;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 折线图数据承载
 * @author: jinnian
 * @create: 2020-10-13 02:09
 **/
@Data
public class LineChartDataDTO {

    private Long id;

    private String name;

    private String type;

    private String stack;

    private List<Double> data;
}
