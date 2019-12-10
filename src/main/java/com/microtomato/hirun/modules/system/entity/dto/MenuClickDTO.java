package com.microtomato.hirun.modules.system.entity.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 菜单点击次数
 *
 * @author Steven
 * @date 2019-12-10
 */
@Data
@Builder
public class MenuClickDTO {
    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 菜单Id
     */
    private Long menuId;

    /**
     * 点击次数
     */
    private Long clicks;
}
