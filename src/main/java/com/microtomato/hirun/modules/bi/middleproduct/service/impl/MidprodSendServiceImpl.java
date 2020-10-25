package com.microtomato.hirun.modules.bi.middleproduct.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.bi.middleproduct.entity.po.MidprodSend;
import com.microtomato.hirun.modules.bi.middleproduct.mapper.MidprodSendMapper;
import com.microtomato.hirun.modules.bi.middleproduct.service.IMidprodSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (MidprodSend)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-26 00:48:47
 */
@Service("midprodSendService")
public class MidprodSendServiceImpl extends ServiceImpl<MidprodSendMapper, MidprodSend> implements IMidprodSendService {

    @Autowired
    private MidprodSendMapper midprodSendMapper;


}