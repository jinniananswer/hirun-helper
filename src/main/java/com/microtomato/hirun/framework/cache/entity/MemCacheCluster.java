package com.microtomato.hirun.framework.cache.entity;

import lombok.Data;

import java.util.TreeSet;

/**
 * Memcached 集群
 *
 * @author Steven
 * @date 2019-10-15
 */
@Data
public class MemCacheCluster {

	/**
	 * 集群名
	 */
	private String name = null;

	/**
	 * 心跳间隔，单位(秒)
	 */
	private int heartbeatSecond = 5;

	/**
	 * 默认连接池大小
	 */
	private int poolSize = 5;

	/**
	 * 是否启用 NIO
	 */
	private boolean useNio = false;

	/**
	 * 集群实例地址集合
	 */
	private TreeSet<MemCacheAddress> address = null;

}
