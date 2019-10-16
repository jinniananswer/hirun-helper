package com.microtomato.hirun.framework.cache.memcache.driver.io;

import com.microtomato.hirun.framework.cache.entity.MemCacheAddress;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Socket 连接池
 *
 * @author Steven
 * @date 2019-10-15
 */
@Slf4j
public class SockIOPool {

	/**
	 * 一个Pool可以跨多个memcached实例，每个实例是一个SockIOBucket桶，根据cacheKey的hash值定位到桶
	 */
	private List<BaseSockIoBucket> buckets = new ArrayList<BaseSockIoBucket>();

	/**
	 * 挂掉的桶列表
	 */
	private List<BaseSockIoBucket> deadBuckets = new ArrayList<BaseSockIoBucket>();

	private int heartbeatSecond = 5;

	/**
	 * 连接池的构造函数
	 *
	 * @param address 地址(复数)
	 * @param poolSize 连接池大小
	 * @param heartbeatSecond 心跳间隔，单位: 秒
	 * @param useNio 是否启用 NIO 模式
	 */
	public SockIOPool(MemCacheAddress[] address, int poolSize, int heartbeatSecond, boolean useNio) {
		
		this.heartbeatSecond = heartbeatSecond;
		
		for (MemCacheAddress addr : address) {

			// 主地址解析
			String[] masterPart = StringUtils.split(addr.getMaster(), ':');
			if (2 != masterPart.length) {
				throw new IllegalArgumentException("memcached主地址格式不正确！" + addr.getMaster());
			}

			String masterHost = masterPart[0];
			int masterPort = Integer.parseInt(masterPart[1]);
			
			// 备地址解析
			String[] slavePart = StringUtils.split(addr.getSlave(), ':');
			if (null != slavePart) {
				if (2 != slavePart.length) {
					throw new IllegalArgumentException("memcached备地址格式不正确！" + addr.getSlave());
				}
			}
			
			BaseSockIoBucket bucket = null;

			try {
				if (null == addr.getSlave()) {
					bucket = new SimpleBaseSockIoBucket(masterHost, masterPort, poolSize, useNio);
				} else {
					String slaveHost = slavePart[0];
					int slavePort = Integer.parseInt(slavePart[1]);
					bucket = new HABaseSockIoBucket(masterHost, masterPort, slaveHost, slavePort, poolSize, useNio);
				}
				
				buckets.add(bucket);
				bucket.init();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		/**
		 * 启动连接池心跳线程
		 */
		MaintTask task = new MaintTask();
		task.setDaemon(true);
		task.start();
	}

	/**
	 * 根据key获取一个socket连接
	 * 
	 * @param cacheKey
	 * @return
	 */
	public ISockio getSock(String cacheKey) {
		int hashCode = hash(cacheKey);
		int divisor = buckets.size();

		if (0 == divisor) {
			return null; // 当桶全部挂死时
		}

		int position = hashCode % divisor;
		position = (position < 0) ? -position : position;

		BaseSockIoBucket bucket = buckets.get(position);
		return bucket.borrowSockio();
	}

	/**
	 * 给cacheKey做hash运算
	 * 
	 * @param cacheKey
	 * @return
	 */
	private static int hash(String cacheKey) {
		int h = cacheKey.hashCode(); // 当前是直接调用的String.hashCode()，是否不同版本JDK实现不一致?
		return h;
	}

	/**
	 * 连接池后台维护线程
	 */
	private final class MaintTask extends Thread {

		@Override
		public void run() {

			while (true) {
				try {
					Thread.sleep(1000 * heartbeatSecond);

					// 桶心跳检查
					bucketHeartbeat();

					// 桶失败重连
					bucketReconnect();
				} catch (Exception e) {
					log.error("memcache连接池心跳线程发生异常!", e);
				}
			}
		}

		/**
		 * 桶心跳检查
		 */
		private void bucketHeartbeat() {
			try {
				Iterator<BaseSockIoBucket> iter = buckets.iterator();
				while (iter.hasNext()) {
					BaseSockIoBucket bucket = iter.next();
					
					int preStateCode = bucket.getStateCode();
					int curStateCode = bucket.healthCheck();
					
					if (BaseSockIoBucket.STATE_ER == curStateCode || BaseSockIoBucket.STATE_ERER == curStateCode) {
						bucket.close();
						iter.remove();
						deadBuckets.add(bucket);
						log.error("memcached桶心跳失败！" + bucket.getAddress());
					} else if (preStateCode != curStateCode) { // 触发状态变更
						log.info("桶状态变更: " + BaseSockIoBucket.STATES[preStateCode] + " -> " + BaseSockIoBucket.STATES[curStateCode]);
						
						// 1. 回收资源
						bucket.close();
						
						// 2. 设置新状态
						bucket.setStateCode(curStateCode);
						
						// 3. 按新状态申请资源
						bucket.init();
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}

		}

		/**
		 * 重试心跳失败的桶，重试成功的桶放回原来的位置，并从待检查桶列表里挪走
		 */
		private void bucketReconnect() {
			
			Iterator<BaseSockIoBucket> iter = deadBuckets.iterator();
			while (iter.hasNext()) {
				BaseSockIoBucket bucket = iter.next();
				try {
					boolean success = bucket.init();
					if (!success) {
						continue;
					}
					
					iter.remove();
					buckets.add(bucket);
					
					Collections.sort(buckets);
					log.info("-------------------------");
					for (BaseSockIoBucket bkt : buckets) {
						log.info("-- " + bkt.getAddress());
					}
					log.info("-------------------------");
					log.info("memcached桶复活！" + bucket.getAddress());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
