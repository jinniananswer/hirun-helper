package com.microtomato.hirun.modules.bi.middleproduct.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 中间产品推送统计查询条件
 * @author: jinnian
 * @create: 2020-10-24 12:49
 **/
@Getter
@Setter
public class QueryPushDataStatisticDTO {

    /**
     * 查询类型
     */
    private String queryType;

    /**
     * 员工姓名
     */
    private String employeeName;

    /**
     * 所选部门
     */
    private String orgIds;

    /**
     * 推送时间
     */
    private LocalDateTime startTime;

    /**
     * 推送时间
     */
    private LocalDateTime endTime;

    /**
     * 产品类型
     */
    private String productType;
    /**
     * 门店
     */
    private Long shopId;

}
