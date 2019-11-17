package com.microtomato.hirun.modules.system.service;

import com.microtomato.hirun.modules.system.entity.po.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.Cacheable;

import java.util.Map;

/**
 * <p>
 * 不用挂菜单的页面 服务类
 * </p>
 *
 * @author Steven
 * @since 2019-11-15
 */
public interface IPageService extends IService<Page> {

    @Cacheable(value = "all-pages")
    Map<Long, Page> listAllPages();
}
