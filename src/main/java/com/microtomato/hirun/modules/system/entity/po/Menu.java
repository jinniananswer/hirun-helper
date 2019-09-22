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
 * @author jinnian
 * @since 2019-09-05
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
     * 员工ID，唯一主键
     */
    @TableId(value = "MENU_ID", type = IdType.AUTO)
    private Integer menuId;

    /**
     * 员工工号
     */
    @TableField("TITLE")
    private String title;

    @TableField("MENU_DESC")
    private String menuDesc;

    @TableField("MENU_URL")
    private String menuUrl;

    @TableField("DOMAIN_ID")
    private Integer domainId;

    @TableField("MENU_LEVEL")
    private Integer menuLevel;

    @TableField("PARENT_MENU_ID")
    private Integer parentMenuId;

    @TableField("ICO_URL")
    private String icoUrl;

    @TableField("IS_COMMON_USE")
    private String isCommonUse;

}
