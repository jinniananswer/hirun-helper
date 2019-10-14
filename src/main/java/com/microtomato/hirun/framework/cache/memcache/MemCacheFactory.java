package com.microtomato.hirun.framework.cache.memcache;

import com.microtomato.hirun.framework.cache.entity.DataCenter;
import com.microtomato.hirun.framework.cache.entity.Mapping;
import com.microtomato.hirun.framework.cache.entity.MemCacheAddress;
import com.microtomato.hirun.framework.cache.entity.MemCacheCluster;
import com.microtomato.hirun.framework.cache.memcache.client.TextClient;
import com.microtomato.hirun.framework.cache.memcache.interfaces.IMemCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright: Copyright (c) 2013 Asiainfo
 *
 * @className: MemCacheFactory
 * @description: Memcache工厂类
 * @version: v1.0.0
 * @author: zhoulin2
 * @date: 2013-3-18
 */
@Slf4j
public final class MemCacheFactory {

    private static final Map<String, IMemCache> caches = new HashMap<>();

    /**
     * 不可能的值，为默认值而设计，防止缓存被击穿。
     */
    public static final String IMPOSSIBLE_VALUE = "28f8139f-7e51-44ae-b5db-700844b04250";

    public void init(String defaultConnect, List<Mapping> mappings, List<DataCenter> dataCenters) {

        String connect = defaultConnect;

        String serverName = System.getProperty("server.name", "app-node01-srv01");
        for (Mapping mapping : mappings) {
            String prefix = StringUtils.stripEnd(mapping.getName(), "*");

            // 找到当前server所对应的数据中心名
            if (serverName.startsWith(prefix)) {
                connect = mapping.getConnect();
                break;
            }
        }

        for (DataCenter dataCenter : dataCenters) {

            // 找到实例所在的中心配置，并初始化。
            if (dataCenter.getDcname().equals(connect)) {
                List<MemCacheCluster> cluster = dataCenter.getCluster();

                for (MemCacheCluster memCacheCluster : cluster) {
                    String clusterName = memCacheCluster.getName();
                    MemCacheAddress[] address = memCacheCluster.getAddress().toArray(new MemCacheAddress[0]);
                    int poolSize = memCacheCluster.getPoolSize();
                    int heartbeatSecond = memCacheCluster.getHeartbeatSecond();
                    boolean isUseNIO = memCacheCluster.isUseNIO();

                    IMemCache cache = new TextClient(address, poolSize, heartbeatSecond, isUseNIO);
                    caches.put(clusterName, cache);

                    if (log.isInfoEnabled()) {
                        log.info("------ memcached连接池初始化成功! ------");
                        log.info("分组组名: " + clusterName);
                        log.info("地址集合:");
                        for (MemCacheAddress addr : address) {
                            if (null != addr.getSlave()) {
                                log.info("  master " + addr.getMaster() + " -> slave " + addr.getSlave());
                            } else {
                                log.info("  " + addr.getMaster());
                            }
                        }
                        log.info("连接数量: " + poolSize);
                        log.info("心跳周期: " + heartbeatSecond);

                        log.info("IO模式: " + (isUseNIO ? "NIO" : "BIO") + "\n");
                    }
                }

                break;
            }
        }

    }

    /**
     * 根据名称获取对应的Cache
     *
     * @param cacheName
     * @return
     */
    public static IMemCache getCache(String cacheName) {

        IMemCache cache = caches.get(cacheName);

        if (null == cache) {
            throw new IllegalArgumentException(cacheName + "连接池中没有可用的连接，请确认缓存地址是否配置正确、缓存是否开启！");
        }

        return cache;

    }

}
