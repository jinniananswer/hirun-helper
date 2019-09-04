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
 * @since 2019-07-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("ins_user")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "USER_ID", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户名
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 用户密码
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 手机号码
     */
    @TableField("MOBILE_NO")
    private String mobileNo;

    @TableField("STATUS")
    private String status;

    @TableField("CREATE_DATE")
    private LocalDateTime createDate;

    @TableField("REMOVE_DATE")
    private LocalDateTime removeDate;

    @TableField("CREATE_USER_ID")
    private Integer createUserId;

    @TableField("UPDATE_USER_ID")
    private Integer updateUserId;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;


}
