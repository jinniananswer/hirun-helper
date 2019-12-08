package com.microtomato.hirun.modules.system.entity.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author Steven
 * @date 2019-12-06
 */
@Data
@Builder
public class FuncDTO {
    private Long funcId;
    private String funcCode;
    private String funcDesc;
    private Boolean checked;
}
