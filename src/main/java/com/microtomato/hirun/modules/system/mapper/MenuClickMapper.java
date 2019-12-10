package com.microtomato.hirun.modules.system.mapper;

import com.microtomato.hirun.modules.system.entity.po.MenuClick;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 菜单点击数统计 Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2019-12-10
 */
@Storage
public interface MenuClickMapper extends BaseMapper<MenuClick> {

    @Update("UPDATE sys_menu_click SET clicks = clicks + #{clicks}, update_time = now() WHERE user_id = #{userId} AND menu_id = #{menuId}")
    int updateClicks(@Param("userId") Long userId, @Param("menuId") Long menuId, @Param("clicks") Long clicks);
}
