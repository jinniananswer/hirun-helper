package com.microtomato.hirun.modules.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.cases.NotFoundException;
import com.microtomato.hirun.framework.utils.MD5Util;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.exception.PasswordException;
import com.microtomato.hirun.modules.user.mapper.UserMapper;
import com.microtomato.hirun.modules.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-09-05
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        User user = this.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
        if (user == null) {
            throw new NotFoundException("根据输入的用户名找不到用户信息，请确认用户名是否正确", ErrorKind.NOT_FOUND.getCode());
        }

        boolean result = user.login(password);
        if (result) {
            return user;
        }

        return null;
    }

    /**
     * 变更用户密码
     */
    @Override

    public boolean changeStaffPassword(Integer userId,String oldPassword,String newPassword){
        boolean reslut=false;
        //校验老密码
        boolean verifyResult=verifyOldPassword(userId,oldPassword);
        if(verifyResult){
            User user=new User();
            user.setUserId(userId);
            user.setPassword(MD5Util.hexdigest(newPassword));
            user.setUpdateUserId(userId);
            user.setUpdateTime(LocalDateTime.now());

            int rows=userMapper.updateById(user);
            if(rows>0){
                reslut=true;
            }
        }
        return reslut;
    }


    /**
     * 校验老密码
     * @param userId
     * @param oldpassword
     * @return
     */
    public  boolean verifyOldPassword(Integer userId,String oldpassword){
        User user=this.getById(userId);
        String encryptPassword = MD5Util.hexdigest(oldpassword);
        if (StringUtils.equals(user.getPassword(), encryptPassword)) {
            return true;
        }
        else {
            throw new PasswordException("原始密码不正确!");
        }
    }
}