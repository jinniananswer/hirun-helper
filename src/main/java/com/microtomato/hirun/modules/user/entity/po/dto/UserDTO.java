package com.microtomato.hirun.modules.user.entity.po.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author Steven
 * @date 2019-10-21
 */
@Data
@Builder
public class UserDTO {
    private Long orgId;
    private Long employeeId;
    private String name;
}
