package com.microtomato.hirun.modules.bi.middleproduct.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: hirun-helper
 * @description: 推送明细查询条件
 * @author: jinnian
 * @create: 2020-10-24 15:14
 **/
@Getter
@Setter
public class QueryPushDataDetailDTO {

    private String custName;

    private String wechatNickname;

    private Long housesId;

    private String orgIds;

    private String employeeName;

    private String filterType;

    private String[] pushTime;
}
