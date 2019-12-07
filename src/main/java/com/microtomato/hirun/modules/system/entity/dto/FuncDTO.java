package com.microtomato.hirun.modules.system.entity.dto;

import lombok.Data;

/**
 * @author Steven
 * @date 2019-12-06
 */
@Data
public class FuncDTO {
    private Long funcId;
    private String funcCode;
    private String funcDesc;
    private Boolean checked;
}
