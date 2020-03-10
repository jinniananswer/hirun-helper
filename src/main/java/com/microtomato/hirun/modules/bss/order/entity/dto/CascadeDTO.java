package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: hirun-helper
 * @description: 级联通用DTO
 * @author: jinnian
 * @create: 2020-02-23 12:57
 **/
@Data
public class CascadeDTO<T> implements Serializable {

    private String label;

    private String value;

    private List<CascadeDTO<T>> children;

    private T self;
}
