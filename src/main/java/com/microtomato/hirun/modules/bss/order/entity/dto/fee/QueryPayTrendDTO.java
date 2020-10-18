package com.microtomato.hirun.modules.bss.order.entity.dto.fee;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 主营收入趋势查询条件数据传输对象
 * @author: jinnian
 * @create: 2020-10-13 02:13
 **/
@Data
public class QueryPayTrendDTO {

    private String orgIds;

    private List<String> queryMonth;
}
