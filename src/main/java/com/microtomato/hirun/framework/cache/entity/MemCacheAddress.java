package com.microtomato.hirun.framework.cache.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Copyright: Copyright (c) 2014 Asiainfo
 * 
 * @className: MemCacheAddress
 * @description: MemCache地址对象
 * 
 * @version: v1.0.0
 * @author: zhoulin2
 * @date: 2014-7-14
 */
@Getter
@Setter
public class MemCacheAddress implements Comparable<MemCacheAddress> {

	/**
	 * 主地址
	 */
	private String master;
	
	/**
	 * 备地址
	 */
	private String slave;

	/**
	 * 仅按master排序
	 * 
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(MemCacheAddress o) {
		String anotherMaster = o.getMaster();
		return this.master.compareTo(anotherMaster);
	}

	@Override
	public String toString() {
		return this.master + " -> " + this.slave;
	}

}
