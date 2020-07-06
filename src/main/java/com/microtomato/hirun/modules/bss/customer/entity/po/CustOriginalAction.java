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
@TableName("ins_cust_original_action")
public class CustOriginalAction extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "action_id", type = IdType.AUTO)
    private Long actionId;

    @TableField("cust_id")
    private Long custId;

    @TableField("action_code")
    private String actionCode;

    @TableField("finish_time")
    private LocalDateTime finishTime;

    @TableField("employee_id")
    private Long employeeId;

    @TableField("out_id")
    private Long outId;

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
