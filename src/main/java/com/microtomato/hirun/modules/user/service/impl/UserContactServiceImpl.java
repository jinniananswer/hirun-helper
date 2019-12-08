package com.microtomato.hirun.modules.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.user.entity.consts.UserConst;
import com.microtomato.hirun.modules.user.entity.po.UserContact;
import com.microtomato.hirun.modules.user.mapper.UserContactMapper;
import com.microtomato.hirun.modules.user.service.IUserContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-11-02
 */
@Slf4j
@Service
public class UserContactServiceImpl extends ServiceImpl<UserContactMapper, UserContact> implements IUserContactService {

    @Override
    public List<UserContact> queryByUserIdType(Long userId) {
        return this.list(
            Wrappers.<UserContact>lambdaQuery()
                .eq(UserContact::getUserId, userId)
                .eq(UserContact::getContactType, UserConst.CONTACT_TYPE_MOBILE_PHONE)
        );
    }
}
