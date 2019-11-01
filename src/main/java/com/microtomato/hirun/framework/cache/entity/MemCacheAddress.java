package com.microtomato.hirun.framework.cache.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * MemCache 地址对象
 *
 * @author Steven
 * @date 2019-10-15
 */
@Getter
@Setter
@EqualsAndHashCode
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
