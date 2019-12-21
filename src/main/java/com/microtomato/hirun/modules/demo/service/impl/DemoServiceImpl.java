package com.microtomato.hirun.modules.demo.service.impl;

import com.microtomato.hirun.modules.demo.entity.po.Steven;
import com.microtomato.hirun.modules.demo.entity.po.Zhoulin;
import com.microtomato.hirun.modules.demo.service.IDemoService;
import com.microtomato.hirun.modules.demo.service.IStevenService;
import com.microtomato.hirun.modules.demo.service.IZhoulinService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public void testSave() {
        List<Steven> stevenList = new ArrayList<>();
        List<Zhoulin> zhoulinList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            stevenList.add(Steven.builder().name(RandomStringUtils.random(8, "ABCDEFGH")).build());
        }
        stevenServiceImpl.saveBatch(stevenList);
        for (int i = 0; i < 2; i++) {
            zhoulinList.add(Zhoulin.builder().name(RandomStringUtils.random(8, "1234567890")).build());
        }
        zhoulinServiceImpl.saveBatch(zhoulinList);
        //int j = 1 / 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void testUpdate() {
        List<Steven> stevenList = new ArrayList<>();
        List<Zhoulin> zhoulinList = new ArrayList<>();

        stevenList.add(Steven.builder().name("STEVEN-1").id(1L).build());
        stevenList.add(Steven.builder().name("STEVEN-2").id(2L).build());
        stevenServiceImpl.updateBatchById(stevenList);

        zhoulinList.add(Zhoulin.builder().name("1234567-1").id(1L).build());
        zhoulinList.add(Zhoulin.builder().name("1234567-2").id(2L).build());
        zhoulinServiceImpl.updateBatchById(zhoulinList);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void testSaveOrUpdate() {
        List<Steven> stevenList = new ArrayList<>();
        List<Zhoulin> zhoulinList = new ArrayList<>();

        stevenList.add(Steven.builder().name("STEVEN-1").id(1L).build());
        stevenList.add(Steven.builder().name("STEVEN-2").id(2L).build());
        stevenList.add(Steven.builder().name("QWEQWEQWE").build());
        stevenServiceImpl.saveOrUpdateBatch(stevenList);

        zhoulinList.add(Zhoulin.builder().name("1234567-1").id(1L).build());
        zhoulinList.add(Zhoulin.builder().name("1234567-2").id(2L).build());
        zhoulinList.add(Zhoulin.builder().name("99999999").build());
        zhoulinServiceImpl.saveOrUpdateBatch(zhoulinList);
    }

}
