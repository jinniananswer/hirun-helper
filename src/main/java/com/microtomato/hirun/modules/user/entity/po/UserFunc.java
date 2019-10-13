package com.microtomato.hirun.modules.user.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * @since 2019-09-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_user_func")
public class UserFunc extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_func_id", type = IdType.AUTO)
    private Long userFuncId;

    @TableField("user_id")
    private Long userId;

    @TableField("func_id")
    private Long funcId;

    @TableField("start_date")
    private LocalDateTime startDate;

    @TableField("end_date")
    private LocalDateTime endDate;

    @TableField("status")
    private String status;

    @TableField("remark")
    private String remark;

    @TableField("create_user_id")
    private Long createUserId;

    @TableField("create_date")
    private LocalDateTime createDate;

    @TableField("update_user_id")
    private Long updateUserId;

    @TableField("update_time")
    private LocalDateTime updateTime;


}
