package com.microtomato.hirun.modules.demo.service.impl;

import com.microtomato.hirun.modules.demo.service.IDemoService;
import com.microtomato.hirun.modules.demo.entity.po.Zhoulin;
import com.microtomato.hirun.modules.demo.mapper.ZhoulinMapper;
import com.microtomato.hirun.modules.demo.service.IZhoulinService;
import com.microtomato.hirun.modules.demo.entity.po.Steven;
import com.microtomato.hirun.modules.demo.service.IStevenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author Steven
 * @date 2019-12-19
 */
@Slf4j
@Service
public class DemoServiceImpl implements IDemoService {

    @Autowired
    private IStevenService stevenServiceImpl;

    @Autowired
    private IZhoulinService zhoulinServiceImpl;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void testJTA() {

        Steven steven = null;
        Zhoulin zhoulin = null;
        for (int i = 0; i < 2; i++) {
            steven = Steven.builder().name(RandomStringUtils.random(8, "ABCDEFGH")).build();
            stevenServiceImpl.save(steven);
        }

        for (int i = 0; i < 2; i++) {
            zhoulin = Zhoulin.builder().name(RandomStringUtils.random(8, "1234567890")).build();
            //zhoulinServiceImpl.save(zhoulin);
            zhoulinMapper.insertRandom(zhoulin.getName(), LocalDateTime.now());
        }

        //int j = 1 / 0;

    }


    @Autowired
    private ZhoulinMapper zhoulinMapper;
}
