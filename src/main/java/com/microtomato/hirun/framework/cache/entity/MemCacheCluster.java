package com.microtomato.hirun.framework.cache.entity;

import lombok.Data;

import java.util.TreeSet;

@Data
public class MemCacheCluster {
	
	private String name = null;
	private int heartbeatSecond = 5;
	private int poolSize = 5;
	private boolean useNIO = false;
	private TreeSet<MemCacheAddress> address = null;

}
