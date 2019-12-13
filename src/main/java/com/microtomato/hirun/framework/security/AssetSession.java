package com.microtomato.hirun.framework.security;

import com.microtomato.hirun.framework.util.WebContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 会话相关的数据 <br>
 * <p>
 * 这类数据较大，不方便放入会话对象中。
 *
 * @author Steven
 * @date 2019-12-13
 */
@Slf4j
@Component
public abstract class AssetSession {

    protected String assetKey() {
        String key = "menus:" + WebContextUtils.getHttpSession().getId();
        return key;
    }

    /**
     * 设置拥有的菜单
     *
     * @param menuUrls
     */
    public abstract void setMenuUrls(Set<String> menuUrls);

    /**
     * 判断是否有对应的菜单
     *
     * @param url
     * @return
     */
    public abstract boolean hasMenuUrl(String url);

}
