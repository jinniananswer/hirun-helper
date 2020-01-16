package com.microtomato.hirun.modules.user.entity.dto;

import lombok.*;

import java.util.List;

/**
 * 员工角色分配与回收
 *
 * @author Steven
 * @date 2020-01-16
 */
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GrantUserRoleDTO {
    private List<Long> userIds;
    private List<Long> roleIds;
}
