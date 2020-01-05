package com.microtomato.hirun.modules.system.mapper;

import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.modules.system.entity.po.Menu;
import com.microtomato.hirun.modules.system.entity.po.MenuClick;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microtomato.hirun.framework.annotation.Storage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 菜单点击数统计 Mapper 接口
 * </p>
 *
 * @author Steven
 * @since 2019-12-10
 */
@Storage
@DataSource(DataSourceKey.SYS)
public interface MenuClickMapper extends BaseMapper<MenuClick> {

    @Update("UPDATE sys_menu_click SET clicks = clicks + #{clicks}, update_time = now() WHERE user_id = #{userId} AND menu_id = #{menuId}")
    int updateClicks(@Param("userId") Long userId, @Param("menuId") Long menuId, @Param("clicks") Long clicks);

    @Select("select m.menu_id, m.title, m.menu_url, m.iconfont from sys_menu m, sys_menu_click c where m.menu_id = c.menu_id and c.user_id = #{userId} order by c.clicks desc")
    List<Menu> hostMenus(@Param("userId") Long userId);
}
