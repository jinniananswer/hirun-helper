package com.microtomato.hirun.framework.config;

import com.microtomato.hirun.framework.cache.entity.DataCenter;
import com.microtomato.hirun.framework.cache.entity.Mapping;
import com.microtomato.hirun.framework.cache.memcache.MemCacheFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steven
 * @date 2019-10-14
 */
@Slf4j
//@Configuration
//@PropertySource(value ={"classpath:cache.yml"})
//@ConfigurationProperties(prefix = "framework.cache.memcached")
public class MemcachedConfig {

    /**
     * 默认连接的中心
     */
    //@Value("${framework.cache.memcached.defaultConnect}")
    @Value("${defaultConnect}")
    private String defaultConnect;

    /**
     * 映射关系
     */
    @Value("${mapping}")
    private List<Mapping> mapping = new ArrayList<>();

    /**
     * 数据中心
     */
    @Value("${dcs}")
    private List<DataCenter> dcs = new ArrayList<>();

    public List<Mapping> getMapping() {
        return this.mapping;
    }

    public void setMapping(List<Mapping> mapping) {
        this.mapping = mapping;
    }

    public List<DataCenter> getDcs() {
        return this.dcs;
    }

    public void setDcs(List<DataCenter> dcs) {
        this.dcs = dcs;
    }

    @Bean
    public MemCacheFactory memCacheFactory() {
        log.info("+---------------------------------------------+");
        log.info("Initializing MemCacheFactory");
        log.info("this.defaultConnect: " + this.defaultConnect);
        log.info("this.mapping: " + this.mapping);
        log.info("this.dcs: " + this.dcs);
        log.info("+---------------------------------------------+");
        MemCacheFactory memCacheFactory = new MemCacheFactory();
        memCacheFactory.init(this.defaultConnect, this.mapping, this.dcs);
        return memCacheFactory;
    }

}
