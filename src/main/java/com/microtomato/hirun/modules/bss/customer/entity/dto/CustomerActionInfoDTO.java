package com.microtomato.hirun.modules.bss.customer.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 客户动作完成信息传输对象
 * @author: liuhui
 **/
@Data
public class CustomerActionInfoDTO {

    private String actionCode;

    private String actionName;

    private String status;

    private LocalDateTime finishDate;

    private String finishEmployeeName;

    private String color;

}
