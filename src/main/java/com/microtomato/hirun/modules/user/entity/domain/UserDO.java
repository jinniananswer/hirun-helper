package com.microtomato.hirun.modules.user.entity.domain;

import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.cases.NotFoundException;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.EncryptUtils;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.user.entity.consts.UserConst;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.entity.po.UserContact;
import com.microtomato.hirun.modules.user.exception.PasswordException;
import com.microtomato.hirun.modules.user.service.IUserContactService;
import com.microtomato.hirun.modules.user.service.IUserService;
import com.microtomato.hirun.modules.user.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 用户领域对象
 * @author: jinnian
 * @create: 2019-10-16 19:45
 **/
@Component
@Scope("prototype")
@Slf4j
public class UserDO {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserContactService userContactService;

    private User user;

    /**
     * 默认构造函数
     */
    public UserDO() {

    }

    /**
     * 以user数据对象为输入的构造函数
     * @param user
     */
    public UserDO(User user) {
        this.user = user;
    }

    /**
     * 以用户ID为输入的构造函数
     * @param userId
     */
    public UserDO(Long userId) {
        IUserService service = SpringContextUtils.getBean(UserServiceImpl.class);
        this.user = service.getById(userId);
        if (this.user == null) {
            throw new NotFoundException("根据用户ID找不到用户信息，请确认用户ID是否正确", ErrorKind.NOT_FOUND.getCode());
        }
    }

    /**
     * 以用户名为输入的构造函数
     * @param username
     */
    public UserDO(String username) {
        IUserService service = SpringContextUtils.getBean(UserServiceImpl.class);
        this.user = service.queryByUsername(username);
        if (this.user == null) {
            throw new NotFoundException("根据输入的用户名找不到用户信息，请确认用户名是否正确", ErrorKind.NOT_FOUND.getCode());
        }
    }

    /**
     * 获取用户数据对象
     * @return
     */
    public User getUser() {
        return this.user;
    }

    /**
     * 创建用户信息
     * @param mobileNo
     * @param password
     * @return
     */
    public Long create(String mobileNo, String password) {
        if (StringUtils.isBlank(password)) {
            password = EncryptUtils.passwordEncode(UserConst.INIT_PASSWORD);
        }

        this.user = new User();
        this.user.setUsername(mobileNo);
        this.user.setMobileNo(mobileNo);
        this.user.setPassword(password);
        this.user.setStatus(UserConst.STATUS_NORMAL);
        this.userService.save(user);

        UserContact userContact = new UserContact();
        userContact.setUserId(user.getUserId());
        userContact.setContactType(UserConst.CONTACT_TYPE_MOBILE_PHONE);
        userContact.setContactNo(mobileNo);
        this.userContactService.save(userContact);

        return user.getUserId();
    }

    public void modify(String username, String password) {
        String originalUsername = this.user.getUsername();
        this.user.setUsername(username);
        this.user.setStatus(UserConst.STATUS_NORMAL);

        if (StringUtils.isNotBlank(password)) {
            this.user.setPassword(EncryptUtils.passwordEncode(password));
        }
        this.userService.updateById(user);

        if (!StringUtils.equals(username, originalUsername)) {
            //不相等，修改联系方式
            List<UserContact> userContacts = this.userContactService.queryByUserIdType(this.user.getUserId());
            if (ArrayUtils.isEmpty(userContacts)) {
                return;
            } else {
                UserContact userContact = userContacts.get(0);
                userContact.setContactNo(username);
                this.userContactService.updateById(userContact);
            }
        }
    }

    /**
     * 用户登陆
     * @param password
     * @return
     */
    public boolean login(String password) {
        String encryptPassword = EncryptUtils.passwordEncode(password);
        if (StringUtils.equals(this.user.getPassword(), encryptPassword)) {
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
    public boolean verifyOriginalPassword(String originalPassword) {
        String encryptPassword = EncryptUtils.passwordEncode(originalPassword);
        if (StringUtils.equals(this.user.getPassword(), encryptPassword)) {
            return true;
        } else {
            throw new PasswordException("原始密码不正确!");
        }
    }

    /**
     * 修改用户密码
     * @param newPassword
     */
    public boolean changePassword(String originalPassword, String newPassword) {
        // 校验老密码
        boolean result = this.verifyOriginalPassword(originalPassword);
        if (result) {
            user.setPassword(EncryptUtils.passwordEncode(newPassword));
            result = userService.updateById(user);
        }
        return result;
    }
}
