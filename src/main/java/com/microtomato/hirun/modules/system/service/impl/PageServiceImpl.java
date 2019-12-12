package com.microtomato.hirun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.modules.system.entity.po.Page;
import com.microtomato.hirun.modules.system.mapper.PageMapper;
import com.microtomato.hirun.modules.system.service.IPageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 不用挂菜单的页面 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-11-15
 */
@Slf4j
@Service
public class PageServiceImpl extends ServiceImpl<PageMapper, Page> implements IPageService {

    @Cacheable(value = "page::listAllPages")
    @Override
    public Map<Long, Page> listAllPages() {

        List<Page> pageList = this.list(Wrappers.<Page>lambdaQuery().select(Page::getUrl, Page::getMenuId));

        Map<Long, Page> pageMap = new HashMap<>(pageList.size());
        pageList.forEach(
            page -> pageMap.put(page.getMenuId(), page)
        );
        return pageMap;
    }

}
