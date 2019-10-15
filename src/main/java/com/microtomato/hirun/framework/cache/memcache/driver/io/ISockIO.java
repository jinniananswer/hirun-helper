package com.microtomato.hirun.framework.cache.memcache.driver.io;

import java.io.IOException;

/**
 * SockIO 接口
 *
 * @author Steven
 * @date 2019-10-15
 */
public interface ISockIO {

	/**
	 * 初始化
	 *
	 * @return
	 */
	boolean init();

	/**
	 * 写入数据
	 *
	 * @param bytes 字节数组
	 * @throws IOException 异常
	 */
	void write(byte[] bytes) throws IOException;

	/**
	 * 读取一个字节
	 *
	 * @param b 字节数组
	 * @param off 偏移量
	 * @param len 读取长度
	 * @return 本次读取长度
	 * @throws IOException 异常
	 */
	int read(byte[] b, int off, int len) throws IOException;

	/**
	 * 刷新
	 *
	 * @throws IOException 异常
	 */
	void flush() throws IOException;

	/**
	 * 是否连通
	 *
	 * @return 通的返回 true，否则返回 false
	 */
	boolean isConnected();

	/**
	 * 是否存活
	 *
	 * @return 活的返回 true，否则返回 false
	 */
	boolean isAlive();

	/**
	 * 关闭连接
	 *
	 * @throws IOException 异常
	 */
	void close() throws IOException;

	/**
	 * 读取一行
	 *
	 * @return 返回一行的字节数组
	 * @throws IOException 异常
	 */
	byte[] readLineBytes() throws IOException;

	/**
	 * 释放
	 */
	void release();

	/**
	 * 获取桶
	 *
	 * @return 返回桶对象
	 */
	SockIOBucket getBucket();

	/**
	 * 获取主机地址
	 *
	 * @return 主机地址
	 */
	String getHost();

	/**
	 * 获取端口
	 *
	 * @return 端口
	 */
	int getPort();
	
	/**
	 * 连接版本号
	 *
	 * @return 版本号
	 */
	int getVersion();
	
	/**
	 * 是否为主地址连接
	 * 
	 * @return 是 Master 节点返回 true，否则返回 false
	 */
	boolean isMaster();
}
