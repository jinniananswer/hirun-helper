package com.microtomato.hirun.modules;

import com.microtomato.hirun.framework.util.SpringContextUtils;
import com.microtomato.hirun.framework.util.WebContextUtils;
import com.microtomato.hirun.modules.system.entity.dto.MenuClickDTO;
import com.microtomato.hirun.modules.system.entity.po.MenuClick;
import com.microtomato.hirun.modules.system.service.IMenuClickService;
import com.microtomato.hirun.modules.system.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 菜单导航控制器
 *
 * @author: Steven
 * @create: 2019-07-28 18:27
 **/
@Slf4j
@Controller
public class MainController {

    /**
     * 本地有界队列，作用是后台采集菜单点击日志，不影响前台业务继续运行。
     */
    private static final LinkedBlockingQueue<MenuClickDTO> BLOCKING_QUEUE = new LinkedBlockingQueue<>(10000);

    /**
     * 本地菜单点击次数统计，单线程操作。
     */
    private static final Map<Long, Map<Long, AtomicLong>> LOCAL_MENU_CLICK = new ConcurrentHashMap<>(1000);

    private static final String OPEN_URL = "openUrl?url=";
    private static final String NO_RIGHT_PAGE = "/error/no-right";

    @Autowired
    private IMenuService menuServiceImpl;

    static {
        Aggregation sender = new Aggregation();
        sender.setDaemon(true);
        sender.start();
    }

    static {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("menuclick-schedule-pool-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(() -> saveMenuClickToDatabase(), 30, 30, TimeUnit.SECONDS);
    }


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/theme")
    public String theme() {
        return "theme";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/console")
    public String console() {
        return "modules/system/console";
    }

    @GetMapping("/openUrl")
    public String openUrl(HttpServletRequest request) {

        String url = request.getParameter("url");
        collectMenuClickOnce(url);

        // 添加 URL 校验，防止手工拼菜单地址。
        Set<String> menuUrls = WebContextUtils.getUserContext().getMenuUrls();
        if (menuUrls.contains(OPEN_URL + url)) {
            return url;
        } else {
            return url;
//            return NO_RIGHT_PAGE;
        }
    }

    /**
     * 采集菜单点击信息
     *
     * @param url
     */
    private void collectMenuClickOnce(String url) {
        Long userId = WebContextUtils.getUserContext().getUserId();
        String menuUrl = OPEN_URL + url;
        Long menuId = menuServiceImpl.getMenuId(menuUrl);
        MenuClickDTO menuClickDTO = MenuClickDTO.builder().userId(userId).menuId(menuId).clicks(1L).build();
        try {
            BLOCKING_QUEUE.offer(menuClickDTO, 1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 聚合
     */
    private static class Aggregation extends Thread {
        private Aggregation() {
        }

        @Override
        public void run() {
            while (true) {
                try {
                    MenuClickDTO menuClickDTO = BLOCKING_QUEUE.take();
                    Long userId = menuClickDTO.getUserId();
                    Long menuId = menuClickDTO.getMenuId();
                    Long clicks = menuClickDTO.getClicks();

                    Map<Long, AtomicLong> menuClickMap = LOCAL_MENU_CLICK.get(userId);
                    if (null == menuClickMap) {
                        menuClickMap = new HashMap(100);
                        LOCAL_MENU_CLICK.put(userId, menuClickMap);
                    }

                    AtomicLong cnt = menuClickMap.get(menuId);
                    if (null == cnt) {
                        cnt = new AtomicLong(0L);
                        menuClickMap.put(menuId, cnt);
                    }
                    menuClickMap.get(menuId).addAndGet(clicks);

                } catch (Exception ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    /**
     * 将内存中的累计的菜单点击次数保存到数据库中
     */
    private static void saveMenuClickToDatabase() {

        try {
            IMenuClickService menuClickServiceImpl = SpringContextUtils.getBean(IMenuClickService.class);

            Iterator<Long> iterator = LOCAL_MENU_CLICK.keySet().iterator();
            while (iterator.hasNext()) {
                Long userId = iterator.next();
                Map<Long, AtomicLong> menuClickMap = LOCAL_MENU_CLICK.get(userId);
                for (Long menuId : menuClickMap.keySet()) {
                    Long clicks = menuClickMap.get(menuId).longValue();

                    boolean success = menuClickServiceImpl.updateClicks(userId, menuId, clicks);
                    if (!success) {
                        MenuClick menuClick = MenuClick.builder().userId(userId).menuId(menuId).clicks(clicks).build();
                        menuClickServiceImpl.save(menuClick);
                    }
                }
                menuClickMap.clear();
                iterator.remove();
            }
        } catch (Exception e) {
            log.error("", e);
        }

    }

}
