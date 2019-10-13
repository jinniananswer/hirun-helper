package com.microtomato.hirun.modules.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author Steven
 * @since 2019-09-29
 */
@Data
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
    @TableId(value = "MENU_ID", type = IdType.AUTO)
    private Long menuId;

    /**
     * 菜单标题
     */
    @TableField("TITLE")
    private String title;

    /**
     * 菜单描述
     */
    @TableField("MENU_DESC")
    private String menuDesc;

    /**
     * 菜单地址
     */
    @TableField("MENU_URL")
    private String menuUrl;

    /**
     * 菜单域
     */
    @TableField("DOMAIN_ID")
    private Integer domainId;

    /**
     * 菜单层级
     */
    @TableField("MENU_LEVEL")
    private Integer menuLevel;

    /**
     * 父菜单ID
     */
    @TableField("PARENT_MENU_ID")
    private Long parentMenuId;

    /**
     * 菜单图标
     */
    @TableField("ICO_URL")
    private String icoUrl;

    @TableField("IS_COMMON_USE")
    private String isCommonUse;

    /**
     * 是否为嵌入页面
     */
    @TableField("is_embed_page")
    private Boolean embedPage;


}
