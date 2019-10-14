package com.microtomato.hirun.framework.cache.memcache.interfaces;

import java.util.Date;

/**
 * Copyright: Copyright (c) 2013 Asiainfo-Linkage
 * 
 * @className: IMemCache
 * @description: IMemCache接口
 * 
 * @version: v1.0.0
 * @author: zhoulin2
 * @date: 2013-3-18
 */
public interface IMemCache {
	
	/**
	 * 判断cacheKey是否存在
	 * 
	 * @param cacheKey
	 * @return 存在返回true，否则返回false
	 */
	boolean keyExists(String cacheKey);
	
	/**
	 * 往缓存里添加一个K-V键值对，只有当缓存中没有此K-V时才会添加成功，此操作为原子操作。
	 * 
	 * @param cacheKey
	 * @param value
	 * @return
	 */
	boolean add(String cacheKey, long value);
	
	/**
	 * 往缓存里添加一个K-V键值对，只有当缓存中没有此K-V时才会添加成功，此操作为原子操作。
	 * 
	 * @param cacheKey
	 * @param value
	 * @param secTTL
	 * @return
	 */
	boolean add(String cacheKey, long value, int secTTL);
	
	/**
	 * 往缓存指定KEY的VALUE后里添加数据
	 * 
	 * @param cacheKey
	 * @param bytes
	 * @return
	 */
	boolean append(String cacheKey, byte[] bytes);
	
	/**
	 * 返回cacheKey对应的缓存对象。
	 * 
	 * @param cacheKey
	 * @return 如果对应的缓存找不到，则返回null。
	 */
	Object get(String cacheKey);
		
	/**
	 * 在缓存中存放一个K-V键值对，同名键会被覆盖。
	 * 
	 * @param cacheKey
	 * @param value
	 * @return 如果数据被成功存储返回true
	 */
	boolean set(String cacheKey, Object value);
	
	boolean set(String cacheKey, Byte value);
	
	boolean set(String cacheKey, Integer value);

	boolean set(String cacheKey, Character value);
	
	boolean set(String cacheKey, String value);
	
	boolean set(String cacheKey, StringBuffer value);

	boolean set(String cacheKey, StringBuilder value);
	
	boolean set(String cacheKey, Float value);
	
	boolean set(String cacheKey, Short value);
	
	boolean set(String cacheKey, Double value);
	
	boolean set(String cacheKey, Date value);
	
	boolean set(String cacheKey, byte[] value);
	
	boolean set(String cacheKey, Boolean value);

	boolean set(String cacheKey, Long value);

	boolean set(String cacheKey, Byte value, int secTTL);
	
	boolean set(String cacheKey, Integer value, int secTTL);
	
	boolean set(String cacheKey, Character value, int secTTL);
	
	boolean set(String cacheKey, String value, int secTTL);
	
	boolean set(String cacheKey, StringBuffer value, int secTTL);

	boolean set(String cacheKey, StringBuilder value, int secTTL);
	
	boolean set(String cacheKey, Float value, int secTTL);
	
	boolean set(String cacheKey, Short value, int secTTL);
	
	boolean set(String cacheKey, Double value, int secTTL);
	
	boolean set(String cacheKey, Date value, int secTTL);
	
	boolean set(String cacheKey, byte[] value, int secTTL);
	
	boolean set(String cacheKey, Boolean value, int secTTL);

	boolean set(String cacheKey, Long value, int secTTL);
	
	/**
	 * 在缓存中存放一个K-V键值对，同名键会被覆盖。设置多少秒后超时
	 * 
	 * @param cacheKey
	 * @param value
	 * @param secTTL 多少秒后超时
	 * @return 执行成功返回true，否则返回false。
	 */
	boolean set(String cacheKey, Object value, int secTTL);
	
	/**
	 * 删除缓存中cacheKey对应的缓存对象
	 * 
	 * @param cacheKey
	 * @return 原缓存中有cacheKey时返回true，否则返回false
	 */
	boolean delete(String cacheKey);
	
	/**
	 * 递增计数器（线程安全），步长为1，当递增到long可保存的最大值时，进行回绕。
	 * 
	 * 注：memcached会确保计数器线程安全的执行。
	 * 
	 * @param cacheKey
	 * @return 返回计数器的当前值
	 */
	long incr(String cacheKey);
	
	/**
	 * 递增计数器（线程安全），步长为inc。
	 * 
	 * 注：memcached会确保计数器线程安全的执行。
	 * 
	 * @param cacheKey
	 * @param inc 步长
	 * @return 返回计数器的当前值
	 */
	long incr(String cacheKey, int inc);
	
	/**
	 * 递增计数器（线程安全），带超时的，超时是指key生成的那个时间。
	 * 
	 * 注：memcached会确保计数器线程安全的执行。
	 * 
	 * @param cacheKey
	 * @param secTTL 超时时间，单位:秒
	 * @return 返回计数器的当前值
	 */
	long incrWithTTL(String cacheKey, int secTTL);
	
	/**
	 * 递增计数器（线程安全），步长为inc，带超时，超时是指key生成的那个时间。
	 * 
	 * 注：memcached会确保计数器线程安全的执行。
	 * 
	 * @param cacheKey
	 * @param inc 步长
	 * @param secTTL 超时时间，单位:秒
	 * @return 返回计数器的当前值
	 */
	long incrWithTTL(String cacheKey, int inc, int secTTL);
	
	/**
	 * 递减计数器， 步长为1，当递减到0时，始终为0。
	 * 
	 * 注：memcached会确保计数器线程安全的执行。
	 * 
	 * @param cacheKey
	 * @return 返回计数器的当前值
	 */
	long decr(String cacheKey);
	
	/**
	 * 递减计数器（线程安全）， 步长为inc，当递减到0时，始终为0。
	 * 
	 * 注：memcached会确保计数器线程安全的执行。
	 * 
	 * @param cacheKey
	 * @param inc
	 * @return 返回计数器的当前值
	 */
	long decr(String cacheKey, int inc);

	/**
	 * 往后顺延超时时间，单位：秒。
	 * 
	 * 注：memcached-1.4.15，及以上版本开始支持！
	 * 
	 * @param cacheKey
	 * @param secTTL
	 * @return 成功touch返回true，否则返回false
	 */
	boolean touch(String cacheKey, int secTTL);

}
