package com.microtomato.hirun.modules.user.entity.dto;

import lombok.*;

/**
 * @author Steven
 * @date 2019-10-21
 */
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long orgId;
    private Long employeeId;
    private String name;
}
