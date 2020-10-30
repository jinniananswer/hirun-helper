package com.microtomato.hirun.modules.bi.middleproduct.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bi.middleproduct.entity.po.MidprodOpen;
import com.microtomato.hirun.modules.bi.middleproduct.mapper.MidprodOpenMapper;
import com.microtomato.hirun.modules.bi.middleproduct.service.IMidprodOpenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (MidprodOpen)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-26 00:48:46
 */
@Service("midprodOpenService")
public class MidprodOpenServiceImpl extends ServiceImpl<MidprodOpenMapper, MidprodOpen> implements IMidprodOpenService {

    @Autowired
    private MidprodOpenMapper midprodOpenMapper;


}