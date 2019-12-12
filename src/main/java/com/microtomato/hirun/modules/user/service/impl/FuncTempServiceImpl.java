package com.microtomato.hirun.modules.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.user.entity.po.FuncTemp;
import com.microtomato.hirun.modules.user.mapper.FuncTempMapper;
import com.microtomato.hirun.modules.user.service.IFuncTempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-11-14
 */
@Slf4j
@Service
public class FuncTempServiceImpl extends ServiceImpl<FuncTempMapper, FuncTemp> implements IFuncTempService {

    /**
     * 根据用户Id，查临时操作权限
     *
     * @param userId
     * @return
     */
    @Override
    @Cacheable(value = "func_temp::queryFuncTemp", key = "#userId")
    public List<FuncTemp> queryFuncTemp(Long userId) {
        return list(
            Wrappers.<FuncTemp>lambdaQuery()
                .select(FuncTemp::getFuncId)
                .eq(FuncTemp::getUserId, userId)
                .gt(FuncTemp::getExpireDate, LocalDateTime.now())
        );
    }

}
