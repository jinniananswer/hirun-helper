package com.microtomato.hirun.modules.user.entity.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author Steven
 * @date 2019-10-21
 */
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long orgId;
    private Long employeeId;
    private String name;
}
