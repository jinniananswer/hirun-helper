package com.microtomato.hirun.modules.bss.customer.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * <p>
 * 客户原始动作记录
 * </p>
 *
 * @author liuhui
 * @since 2020-04-30
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_project_original_action")
public class ProjectOriginalAction extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "action_id", type = IdType.AUTO)
    private Long actionId;

    @TableField("project_id")
    private Long projectId;

    @TableField("party_id")
    private Long partyId;

    @TableField("action_code")
    private String actionCode;

    /**
     * 状态字段 0未完成，1完成
     */
    @TableField("status")
    private String status;

    @TableField("finish_time")
    private LocalDateTime finishTime;

    @TableField("out_id")
    private Long outId;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
