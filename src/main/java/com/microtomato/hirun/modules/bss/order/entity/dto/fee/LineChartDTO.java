package com.microtomato.hirun.modules.bss.order.entity.dto.fee;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 折线图
 * @author: jinnian
 * @create: 2020-10-13 02:05
 **/
@Data
public class LineChartDTO {

    private String title;

    private List<String> legends;

    private List<String> xAxises;

    private List<LineChartDataDTO> datas;
}
