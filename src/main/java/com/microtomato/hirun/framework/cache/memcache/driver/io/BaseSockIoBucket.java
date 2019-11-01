package com.microtomato.hirun.framework.cache.memcache.driver.io;

import java.io.IOException;

/**
 * SockIO 桶
 *
 * @author Steven
 * @date 2019-10-15
 */
public abstract class BaseSockIoBucket implements Comparable<BaseSockIoBucket> {

	/**
	 * 桶的状态码
	 */

	/**
	 * 主备地址都不可用
	 */
	static final int STATE_ERER = 0x00;

	/**
	 * 主地址可用，备地址不可用
	 */
	static final int STATE_OKER = 0x01;

	/**
	 * 主地址不可用，备地址可用
	 */
	static final int STATE_EROK = 0x02;

	/**
	 * 主备地址都可用
	 */
	static final int STATE_OKOK = 0x03;

	/**
	 * 主地址不可用，无备地址
	 */
	static final int STATE_ER   = 0x04;

	/**
	 * 主地址可用，无备地址
	 */
	static final int STATE_OK   = 0x05;

	/**
	 * 未初始化
	 */
	static final int STATE_RAW  = -0x01;
	
	static final String[] STATES = {"STATE_ERER", "STATE_OKER", "STATE_EROK", "STATE_OKOK", "STATE_ER", "STATE_OK"};

	/**
	 * 桶的初始化
	 *
	 * @return 初始化成功返回 true，否则返回 false
	 * @throws IOException 异常
	 */
	public abstract boolean init() throws IOException;
	
	/**
	 * 桶的销毁，关闭桶中所有的连接
	 */
	public abstract void close();
	
	/**
	 * 非阻塞方式获取一个socket连接
	 * 
	 * @return
	 */
	public abstract ISockio borrowSockio();

	/**
	 * 非阻塞方式获取一个socket连接
	 *
	 * @param timeout 获取等待超时时间
	 * @return
	 */
	public abstract ISockio borrowSockio(long timeout);
	
	/**
	 * 归还一个socket连接
	 * 
	 * @param sock
	 */
	public abstract void returnSockio(ISockio sock);

	/**
	 *
	 * @param sock
	 * @return
	 */
	public abstract boolean delSock(ISockio sock);

	/**
	 *
	 * @return
	 */
	public abstract String getAddress();

	/**
	 * 健康检测函数，返回桶的状态码
	 *
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public abstract int healthCheck() throws IOException, InterruptedException;

	/**
	 *
	 * @return
	 */
	public abstract int getStateCode();
	
	/**
	 * 设置桶状态
	 * 
	 * @param stateCode
	 */
	public abstract void setStateCode(int stateCode);

	/**
	 * 比较两个 SockIOBucket
	 *
	 * @param o other SockIOBucket
	 * @return
	 */
	@Override
	public abstract int compareTo(BaseSockIoBucket o);
}
