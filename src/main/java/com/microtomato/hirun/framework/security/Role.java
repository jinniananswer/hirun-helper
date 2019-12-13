package com.microtomato.hirun.framework.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色
 *
 * @author Steven
 * @date 2019-09-10
 */
@Data
@AllArgsConstructor
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

}
