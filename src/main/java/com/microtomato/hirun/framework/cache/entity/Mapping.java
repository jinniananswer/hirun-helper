package com.microtomato.hirun.framework.cache.entity;

import lombok.Data;

/**
 * 实例与集群的映射关系
 *
 * @author Steven
 * @date 2019-10-14
 */
@Data
public class Mapping {

    /**
     * 实例名
     */
    private String name;

    /**
     * 指定连接的集群
     */
    private String connect;

}
