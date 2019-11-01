package com.microtomato.hirun.modules.demo.service.impl;

import com.microtomato.hirun.modules.demo.entity.po.Steven;
import com.microtomato.hirun.modules.demo.mapper.StevenMapper;
import com.microtomato.hirun.modules.demo.service.IStevenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-10-30
 */
@Slf4j
@Service
public class StevenServiceImpl extends ServiceImpl<StevenMapper, Steven> implements IStevenService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<Steven> list) {
        log.info("自定义 saveBatch 函数...");
        int i = 0;
        for (Steven steven : list) {
            save(steven);
            i++;
            if (i == 3) {
                i = 1 / 0;
            }
        }
    }
}
