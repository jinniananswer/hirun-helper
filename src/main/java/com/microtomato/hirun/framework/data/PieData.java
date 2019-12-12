package com.microtomato.hirun.framework.data;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: hirun-helper
 * @description: 饼图数据对象
 * @author: jinnian
 * @create: 2019-11-21 22:34
 **/
@Data
public class PieData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String value;
}
