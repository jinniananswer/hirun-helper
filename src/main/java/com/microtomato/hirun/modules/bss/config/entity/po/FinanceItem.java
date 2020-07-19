package com.microtomato.hirun.modules.bss.config.entity.po;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 财务科目表(FinanceItem)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-19 22:26:54
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("finance_item")
public class FinanceItem extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 财务科目 对应老系统id，将 */
    @TableField(value = "finance_item_id")
    private String financeItemId;

    /** 财务科目名称 */
    @TableField(value = "name")
    private String name;

    /** 财务科目类型 */
    @TableField(value = "type")
    private String type;

    /** 财务科目上级科目 */
    @TableField(value = "parent_finance_item_id")
    private String parentFinanceItemId;

    /** 0-借 1-贷 */
    @TableField(value = "direction")
    private Boolean direction;


    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** U-表示有效 */
    @TableField(value = "status")
    private String status;


    @TableField(value = "remark")
    private String remark;

}