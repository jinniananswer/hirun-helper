package com.microtomato.hirun.modules.user.service;

import com.microtomato.hirun.modules.user.entity.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-09-05
 */
public interface IUserService extends IService<User> {

    User login(String username, String password);

    boolean changeStaffPassword(Integer userId,String oldPassword,String newPassword);
}
