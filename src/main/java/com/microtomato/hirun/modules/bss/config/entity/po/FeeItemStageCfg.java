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
 * 费用项分期配置表(FeeItemStageCfg)表实体类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-03-08 00:43:22
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_fee_item_stage_cfg")
public class FeeItemStageCfg extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 费用项ID */
    @TableField(value = "fee_item_id")
    private Long feeItemId;

    /** 类型：H-家装订单 W-木制品订单 */
    @TableField(value = "type")
    private String type;

    /** 分期数 */
    @TableField(value = "periods")
    private Integer periods;

    /** 应付比例 */
    @TableField(value = "rate")
    private Integer rate;

    /** 0-不为准 1以此为准 */
    @TableField(value = "is_norm")
    private Boolean isNorm;


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

}