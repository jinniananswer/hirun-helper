package com.microtomato.hirun.framework.cache.entity;

import lombok.Data;

import java.util.List;

/**
 * 数据中心
 *
 * @author Steven
 * @date 2019-10-15
 */
@Data
public class DataCenter {

    /**
     * 中心名
     */
    private String dcname;

    /**
     * Memcached 集群列表
     */
    private List<MemCacheCluster> cluster;
}
