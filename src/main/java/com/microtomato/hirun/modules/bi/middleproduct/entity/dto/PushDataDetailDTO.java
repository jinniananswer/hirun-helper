package com.microtomato.hirun.modules.bi.middleproduct.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 推送明细数据
 * @author: jinnian
 * @create: 2020-10-24 15:16
 **/
@Getter
@Setter
public class PushDataDetailDTO {

    /**
     * 客户姓名
     */
    private String custName;

    /**
     * 楼盘名称
     */
    private String houseName;

    /**
     * 推送员工
     */
    private String pushEmployeeName;

    /**
     * 推送时间
     */
    private LocalDateTime pushTime;

    /**
     * 推送内容
     */
    private String pushContent;

    /**
     * 分享时间
     */
    private LocalDateTime shareTime;

    /**
     * 分享类型
     */
    private String shareTypeName;
}
