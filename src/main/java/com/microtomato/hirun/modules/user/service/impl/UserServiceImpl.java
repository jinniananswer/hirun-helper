package com.microtomato.hirun.modules.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.cases.NotFoundException;
import com.microtomato.hirun.modules.user.entity.domain.UserDO;
import com.microtomato.hirun.modules.user.entity.po.User;
import com.microtomato.hirun.modules.user.entity.po.dto.UserDTO;
import com.microtomato.hirun.modules.user.mapper.UserMapper;
import com.microtomato.hirun.modules.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jinnian
 * @author Steven
 * @since 2019-09-05
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDO userDO;

    @Override
    public User login(String username, String password) {
        User user = this.queryByUsername(username);
        if (user == null) {
            throw new NotFoundException("根据输入的用户名找不到用户信息，请确认用户名是否正确", ErrorKind.NOT_FOUND.getCode());
        }

        boolean result = this.userDO.login(user, password);
        if (result) {
            return user;
        }

        return null;
    }

    /**
     * 变更用户密码
     */
    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = this.getById(userId);
        boolean result = this.userDO.changePassword(user, oldPassword, newPassword);
        return result;
    }

    /**
     * 根据 userId 查 orgId
     */
    @Override
    public UserDTO queryRelatInfoByUserId(Long userId) {
        QueryWrapper<UserDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("a.user_id = b.user_id AND b.employee_id = c.employee_id AND c.end_date > NOW()");
        queryWrapper.eq("a.user_id", userId);
        return userMapper.queryRelatInfoByUserId(queryWrapper);
    }

    @Override
    public User queryByUsername(String username) {
        User user = this.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username).eq(User::getStatus, "0"));
        return user;
    }

    /**
     * 校验老密码
     * @param userId
     * @param originalPassword
     * @return
     */
    private boolean verifyOldPassword(Long userId, String originalPassword) {
        User user = this.getById(userId);
        return this.userDO.verifyOriginalPassword(user, originalPassword);
    }
}