package com.microtomato.hirun.modules.system.entity.dto;

import lombok.*;

import java.util.List;

/**
 * @author Steven
 * @date 2019-12-05
 */
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MenuNode {

    private Long id;
    private Long pid;
    private String title;
    private String field;
    private Boolean checked;
    private Boolean spread;
    List<MenuNode> children;

}
