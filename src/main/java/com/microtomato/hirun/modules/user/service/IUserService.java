package com.microtomato.hirun.modules.user.service;

import com.microtomato.hirun.modules.user.entity.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.user.entity.po.dto.UserDTO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jinnian
 * @author Steven
 * @since 2019-09-05
 */
public interface IUserService extends IService<User> {

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password);

    /**
     * 修改员工密码
     *
     * @param userId 用户ID
     * @param oldPassword 老密码
     * @param newPassword 新密码
     * @return 密码修改成功与否
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 根据 userId 查 用户关联信息
     *
     * @param userId 用户 ID
     * @return orgId
     */
    UserDTO queryRelatInfoByUserId(Long userId);
}
