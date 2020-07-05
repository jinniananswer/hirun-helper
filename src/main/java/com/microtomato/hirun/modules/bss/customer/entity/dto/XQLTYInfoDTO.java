package com.microtomato.hirun.modules.bss.customer.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 需求蓝图一信息传输对象
 * @author: liuhui
 **/
@Data
public class XQLTYInfoDTO {

    private String actionType;

    private String actionTypeName;

    private String modeTime;

    private String style;

    private String func;

    private String pushEmployeeName;


}
