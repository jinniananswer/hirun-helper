package com.microtomato.hirun.framework.security;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 角色
 *
 * @author Steven
 * @date 2019-09-10
 */
@Data
@AllArgsConstructor
public class Role {

    private Integer id;
    private String name;

}
