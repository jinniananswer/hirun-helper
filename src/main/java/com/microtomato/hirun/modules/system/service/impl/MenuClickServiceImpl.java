package com.microtomato.hirun.modules.system.service.impl;

import com.microtomato.hirun.modules.system.entity.po.MenuClick;
import com.microtomato.hirun.modules.system.mapper.MenuClickMapper;
import com.microtomato.hirun.modules.system.service.IMenuClickService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 菜单点击数统计 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-12-10
 */
@Slf4j
@Service
public class MenuClickServiceImpl extends ServiceImpl<MenuClickMapper, MenuClick> implements IMenuClickService {

}
