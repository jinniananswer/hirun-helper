package com.microtomato.hirun.modules.system.entity.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author Steven
 * @date 2019-12-06
 */
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FuncDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long funcId;
    private String funcCode;
    private String funcDesc;
    private Boolean checked;
}
