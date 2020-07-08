package com.microtomato.hirun.modules.bss.supply.entity.dto;

import lombok.Data;

/**
 * 供应商信息查询条件传输对象
 * @author lijun17
 * @description
 * @date 2020-07-07
 */
@Data
public class SupplierQueryDTO {

    /**
     * 供应商编码
     */
    private Long id;

    /**
     * 供应商名称
     */
    private String name;

    /**
     * 第几页
     */
    private Integer page;

    /**
     * 每页最大数量
     */
    private Integer limit;

    /**
     * 总条数
     */
    private Integer count;
}
