package com.microtomato.hirun.modules.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.modules.demo.entity.po.Zhoulin;
import com.microtomato.hirun.modules.demo.mapper.ZhoulinMapper;
import com.microtomato.hirun.modules.demo.service.IZhoulinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-12-19
 */
@Slf4j
@Service
@DataSource(DataSourceKey.INS)
public class ZhoulinServiceImpl extends ServiceImpl<ZhoulinMapper, Zhoulin> implements IZhoulinService {

}
