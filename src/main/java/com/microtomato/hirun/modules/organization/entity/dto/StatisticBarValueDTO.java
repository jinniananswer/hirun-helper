package com.microtomato.hirun.modules.organization.entity.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 统计图表柱状图值数据对象
 * @author: jinnian
 * @create: 2019-12-02 10:56
 **/
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StatisticBarValueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private List<Integer> data;
}
