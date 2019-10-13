package com.microtomato.hirun.modules.user.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.microtomato.hirun.framework.data.BaseEntity;
import com.microtomato.hirun.framework.util.EncryptUtils;
import com.microtomato.hirun.modules.user.exception.PasswordException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

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
@TableName("ins_user")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 用户密码
     */
    @TableField("password")
    private String password;

    /**
     * 手机号码
     */
    @TableField("mobile_no")
    private String mobileNo;

    @TableField("status")
    private String status;

    @TableField("create_date")
    private LocalDateTime createDate;

    @TableField("remove_date")
    private LocalDateTime removeDate;

    @TableField("create_user_id")
    private Long createUserId;

    @TableField("update_user_id")
    private Long updateUserId;

    @TableField("update_time")
    private LocalDateTime updateTime;

    public boolean login(String password) {
        String encryptPassword = EncryptUtils.passwordEncode(password);
        if (StringUtils.equals(this.password, encryptPassword)) {
            return true;
        }
        else {
            throw new PasswordException("密码不正确!");
        }
    }

}