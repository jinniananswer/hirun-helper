package com.microtomato.hirun.modules.bss.config.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 非主营收款项配置表
 * </p>
 *
 * @author sunxin
 * @since 2020-03-09
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_collection_item_cfg")
public class CollectionItemCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 非主营收款项名称
     */
    @TableField("name")
    private String name;

    /**
     * 非主营收款项类型
     */
    @TableField("type")
    private String type;

    /**
     * 非主营上级费用项
     */
    @TableField("parent_collection_item_id")
    private Long parentCollectionItemId;

    /**
     * 0-加 1-减
     */
    @TableField("direction")
    private Integer direction;

    /**
     * 其它扩展
     */
    @TableField("extend")
    private String extend;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * U-表示有效
     */
    @TableField("status")
    private String status;


}
