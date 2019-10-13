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

    @TableId(value = "USER_FUNC_ID", type = IdType.AUTO)
    private Long userFuncId;

    @TableField("USER_ID")
    private Long userId;

    @TableField("FUNC_ID")
    private Long funcId;

    @TableField("START_DATE")
    private LocalDateTime startDate;

    @TableField("END_DATE")
    private LocalDateTime endDate;

    @TableField("STATUS")
    private String status;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_USER_ID")
    private Long createUserId;

    @TableField("CREATE_DATE")
    private LocalDateTime createDate;

    @TableField("UPDATE_USER_ID")
    private Long updateUserId;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;


}
