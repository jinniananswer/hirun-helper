package com.microtomato.hirun.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.cases.NotFoundException;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.mapper.UserMapper;
import com.microtomato.hirun.modules.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

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
}