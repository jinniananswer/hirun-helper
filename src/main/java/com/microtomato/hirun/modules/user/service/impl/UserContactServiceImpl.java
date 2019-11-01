package com.microtomato.hirun.modules.user.service.impl;

import com.microtomato.hirun.modules.user.entity.po.UserContact;
import com.microtomato.hirun.modules.user.mapper.UserContactMapper;
import com.microtomato.hirun.modules.user.service.IUserContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-11-02
 */
@Slf4j
@Service
public class UserContactServiceImpl extends ServiceImpl<UserContactMapper, UserContact> implements IUserContactService {

}
