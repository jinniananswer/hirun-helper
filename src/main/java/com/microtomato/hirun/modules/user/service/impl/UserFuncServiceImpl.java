package com.microtomato.hirun.modules.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.user.entity.po.UserFunc;
import com.microtomato.hirun.modules.user.mapper.UserFuncMapper;
import com.microtomato.hirun.modules.user.service.IUserFuncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinnian
 * @since 2019-09-09
 */
@Slf4j
@Service
@DataSource(DataSourceKey.INS)
public class UserFuncServiceImpl extends ServiceImpl<UserFuncMapper, UserFunc> implements IUserFuncService {

}
