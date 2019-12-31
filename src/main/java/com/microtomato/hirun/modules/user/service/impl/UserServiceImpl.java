package com.microtomato.hirun.modules.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.exception.ErrorKind;
import com.microtomato.hirun.framework.exception.cases.NotFoundException;
import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.modules.organization.entity.po.Employee;
import com.microtomato.hirun.modules.organization.mapper.EmployeeMapper;
import com.microtomato.hirun.modules.user.entity.consts.UserConst;
import com.microtomato.hirun.modules.user.entity.domain.UserDO;
import com.microtomato.hirun.modules.user.entity.dto.UserDTO;
import com.microtomato.hirun.modules.user.entity.po.User;
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
    private EmployeeMapper employeeMapper;

    @Override
    public User login(String username, String password) {
        UserDO userDO = SpringContextUtils.getBean(UserDO.class, username);
        User user = userDO.getUser();

        boolean result = userDO.login(password);
        if (result) {
            return user;
        }

        return null;
    }

    @Override
    public User queryUser(String username) {
        return getOne(
            new QueryWrapper<User>().lambda()
                .select(User::getUserId, User::getUsername, User::getPassword, User::getMobileNo, User::getStatus)
                .eq(User::getUsername, username)
                .eq(User::getStatus, UserConst.STATUS_NORMAL)
        );
    }

    /**
     * 变更用户密码
     */
    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        UserDO userDO = SpringContextUtils.getBean(UserDO.class, userId);
        boolean result = userDO.changePassword(oldPassword, newPassword);
        return result;
    }

    /**
     * 根据 userId 查 orgId
     */
    @Override
    public UserDTO queryRelatInfoByUserId(Long userId) {
        QueryWrapper<UserDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("a.user_id = b.user_id AND b.employee_id = c.employee_id and c.start_date > NOW() AND c.end_date > NOW() and c.is_main='1' ");
        queryWrapper.eq("a.user_id", userId);
        return userMapper.queryRelatInfoByUserId(queryWrapper);
    }

    @Override
    public User queryByUsername(String username) {
        User user = this.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username).eq(User::getStatus, UserConst.STATUS_NORMAL));
        return user;
    }

    @Override
    public boolean resetPassword(Long employeeId) {
        Employee employee = employeeMapper.selectById(employeeId);
        if (null == employee) {
            throw new NotFoundException("根据ID找不到员工信息，请确认ID是否正确", ErrorKind.NOT_FOUND.getCode());
        }
        UserDO userDO = SpringContextUtils.getBean(UserDO.class, employee.getUserId());
        return userDO.resetPassword();
    }

    /**
     * 校验老密码
     *
     * @param userId
     * @param originalPassword
     * @return
     */
    private boolean verifyOldPassword(Long userId, String originalPassword) {
        UserDO userDO = SpringContextUtils.getBean(UserDO.class, userId);
        return userDO.verifyOriginalPassword(originalPassword);
    }
}