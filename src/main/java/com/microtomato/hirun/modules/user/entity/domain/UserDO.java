package com.microtomato.hirun.modules.user.entity.domain;

import com.microtomato.hirun.framework.util.EncryptUtils;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.exception.PasswordException;
import com.microtomato.hirun.modules.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: hirun-helper
 * @description: 用户领域对象
 * @author: jinnian
 * @create: 2019-10-16 19:45
 **/
@Component
@Slf4j
public class UserDO {

    @Autowired
    private UserMapper userMapper;

    public Long create(String mobileNo, String password) {
        if (StringUtils.isBlank(password)) {
            password = EncryptUtils.passwordEncode("123456");
        }

        User user = new User();
        user.setUsername(mobileNo);
        user.setMobileNo(mobileNo);
        user.setPassword(password);
        user.setStatus("0");

        this.userMapper.insert(user);
        return user.getUserId();
    }

    /**
     * 用户登陆
     * @param user
     * @param password
     * @return
     */
    public boolean login(User user, String password) {
        String encryptPassword = EncryptUtils.passwordEncode(password);
        if (StringUtils.equals(user.getPassword(), encryptPassword)) {
            return true;
        }
        else {
            throw new PasswordException("密码不正确!");
        }
    }

    /**
     * 检验用户原密码
     * @param originalPassword
     * @return
     */
    public boolean verifyOriginalPassword(User user, String originalPassword) {
        String encryptPassword = EncryptUtils.passwordEncode(originalPassword);
        if (StringUtils.equals(user.getPassword(), encryptPassword)) {
            return true;
        } else {
            throw new PasswordException("原始密码不正确!");
        }
    }

    /**
     * 修改用户密码
     * @param newPassword
     */
    public boolean changePassword(User user, String originalPassword, String newPassword) {
        // 校验老密码
        boolean result = this.verifyOriginalPassword(user, originalPassword);
        if (result) {
            user.setPassword(EncryptUtils.passwordEncode(newPassword));
            int rows = userMapper.updateById(user);
            if (rows <= 0) {
                result = false;
            }
        }
        return result;
    }
}
