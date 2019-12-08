package com.microtomato.hirun.modules.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author Steven
 * @since 2019-09-29
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_menu")
public class Menu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID，唯一主键
     */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    /**
     * 菜单标题
     */
    @TableField("title")
    private String title;

    /**
     * 菜单描述
     */
    @TableField("menu_desc")
    private String menuDesc;

    /**
     * 菜单地址
     */
    @TableField("menu_url")
    private String menuUrl;

    /**
     * 类型：（P:Pc, M:Mobile）
     */
    @TableField("type")
    private String type;

    /**
     * 菜单域
     */
    @TableField("domain_id")
    private Integer domainId;

    /**
     * 菜单层级
     */
    @TableField("menu_level")
    private Integer menuLevel;

    /**
     * 父菜单ID
     */
    @TableField("parent_menu_id")
    private Long parentMenuId;

    /**
     * 菜单图标
     */
    @TableField("ico_url")
    private String icoUrl;

    @TableField("is_common_use")
    private String isCommonUse;

}
