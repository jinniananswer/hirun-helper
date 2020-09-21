package com.microtomato.hirun.modules.bss.order.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 订单文件展示及下载
 * @author: jinnian
 * @create: 2020-03-07 23:57
 **/
@Data
public class OrderFileDTO {

    private String stageName;

    private Long orderId;

    private Integer stage;

    private String fileName;
}
