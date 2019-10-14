package com.microtomato.hirun.framework.cache.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
public class DataCenter {

    /**
     * 中心名
     */
    private String dcname;

    private List<MemCacheCluster> cluster;
}
