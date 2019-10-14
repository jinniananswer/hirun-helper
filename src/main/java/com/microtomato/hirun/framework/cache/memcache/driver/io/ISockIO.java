package com.microtomato.hirun.framework.cache.memcache.driver.io;

import java.io.IOException;

/**
 * Copyright: Copyright (c) 2013 Asiainfo-Linkage
 * 
 * @className: ISockIO
 * @description: SockIO接口
 * 
 * @version: v1.0.0
 * @author: zhoulin2
 * @date: 2013-3-19
 */
public interface ISockIO {
		
	boolean init();
	
	void write(byte[] bytes) throws IOException;

	int read(byte[] b, int off, int len) throws IOException;

	void flush() throws IOException;

	boolean isConnected();

	boolean isAlive();

	void close() throws IOException;

	byte[] readLineBytes() throws IOException;

	void release();
	
	SockIOBucket getBucket();
	
	String getHost();

	int getPort();
	
	/**
	 * 连接版本号
	 * 
	 * @return
	 */
	int getVersion();
	
	/**
	 * 是否为主地址连接
	 * 
	 * @return
	 */
	boolean isMaster();
}
