package com.microtomato.hirun.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microtomato.hirun.modules.user.entity.po.UserContact;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jinnian
 * @since 2019-11-02
 */
public interface IUserContactService extends IService<UserContact> {

    public List<UserContact> queryByUserIdType(Long userId);
}
