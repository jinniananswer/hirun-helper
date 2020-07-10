package com.microtomato.hirun.modules.bss.customer.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: hirun-helper
 * @description: 蓝图指导书信息传输对象
 * @author: liuhui
 **/
@Data
public class LTZDSInfoDTO {

    private LocalDateTime commitTime;

    private String commitEmployeeName;

}
