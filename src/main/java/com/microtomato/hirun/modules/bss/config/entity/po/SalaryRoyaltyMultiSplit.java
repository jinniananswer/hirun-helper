package com.microtomato.hirun.modules.bss.config.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.microtomato.hirun.framework.data.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 员工工资提成多人拆分比例配置(SalaryRoyaltyMultiSplit)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-05-26 20:56:16
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("salary_royalty_multi_split")
public class SalaryRoyaltyMultiSplit extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 提成类型：大类 1-设计提成 2-经营提成 见sys_static_data表 royalties_type */
    @TableField(value = "type")
    private String type;

    /** 提成细项 见sys_static_data表 royalties_item */
    @TableField(value = "item")
    private String item;

    /** 人数 */
    @TableField(value = "num")
    private Integer num;

    /** 拆分值，与角色相关的拆分比例 */
    @TableField(value = "value")
    private String value;

    /** 状态 U表示有效 */
    @TableField(value = "status")
    private String status;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}