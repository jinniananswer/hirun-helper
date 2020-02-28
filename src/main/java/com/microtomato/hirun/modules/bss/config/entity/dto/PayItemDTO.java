package com.microtomato.hirun.modules.bss.config.entity.dto;

import lombok.Data;

/**
 * @program: hirun-helper
 * @description: 收款项数据传输对象
 * @author: jinnian
 * @create: 2020-02-23 01:18
 **/
@Data
public class PayItemDTO {

    private Long payItemId;

    private Double money;
}
