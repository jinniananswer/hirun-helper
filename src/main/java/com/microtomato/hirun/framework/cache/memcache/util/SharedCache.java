package com.microtomato.hirun.framework.cache.memcache.util;

import com.microtomato.hirun.framework.cache.memcache.MemCacheFactory;
import com.microtomato.hirun.framework.cache.memcache.interfaces.IMemCache;
import lombok.extern.slf4j.Slf4j;

/**
 * 共享缓存
 *
 * @author Steven
 * @date 2019-10-15
 */
@Slf4j
public final class SharedCache {

	private static final IMemCache cache = MemCacheFactory.getCache("shc_cache");
		
	/**
	 * 往共享缓存中存一个K-V
	 * 
	 * @param cacheKey
	 * @param value
	 * @return
	 */
	public static final boolean set(final String cacheKey, final Object value) {
		if (log.isDebugEnabled()) {
			log.debug("set cacheKey:" + cacheKey);
		}
		return cache.set(cacheKey, value);
	}
	
	/**
	 * 往共享缓存中存一个K-V，secTTL秒后超时
	 * 
	 * @param cacheKey
	 * @param value
	 * @param secTTL
	 * @return
	 */
	public static final boolean set(final String cacheKey, final Object value, int secTTL) {
		if (log.isDebugEnabled()) {
			log.debug("set cacheKey:" + cacheKey + ", secTTL:" + secTTL);
		}
		return cache.set(cacheKey, value, secTTL);
	}
	
	/**
	 * 判断Key是否存在于共享缓存中
	 * 
	 * @param cacheKey
	 * @return
	 */
	public static final boolean keyExist(final String cacheKey) {
		if (log.isDebugEnabled()) {
			log.debug("keyExist cacheKey:" + cacheKey);
		}
		return cache.keyExists(cacheKey);
	}
	
	/**
	 * 从共享缓存中获取Key所对应的Value
	 * 
	 * @param cacheKey
	 * @return
	 */
	public static final Object get(final String cacheKey) {
		if (log.isDebugEnabled()) {
			log.debug("get cacheKey:" + cacheKey);
		}
		return cache.get(cacheKey);
	}
	
	/**
	 * 在共享缓存中删除Key所对应的Value
	 * 
	 * @param cacheKey
	 * @return
	 */
	public static final boolean delete(final String cacheKey) {
		if (log.isDebugEnabled()) {
			log.debug("delete cacheKey:" + cacheKey);
		}
		return cache.delete(cacheKey);
	}
	
	/**
	 * 在共享缓存中将Key所对应的Value延迟
	 * 
	 * @param cacheKey
	 * @param secTTL
	 * @return
	 */
	public static final boolean touch(final String cacheKey, int secTTL) {
		if (log.isDebugEnabled()) {
			log.debug("touch cacheKey:" + cacheKey + ", secTTL:" + secTTL);
		}
		return cache.touch(cacheKey, secTTL);
	}
	
}
