package com.microtomato.hirun.framework.cache.util.serial;

/**
 * 序列化接口
 *
 * @author Steven
 * @date 2019-10-15
 */
public interface ISerializable {
	
	/**
	 * 将对象编码成byte数组
	 * 
	 * @param obj
	 * @return
	 */
	public byte[] encode(Object obj);
	
	/**
	 * 对字节数组进行压缩
	 * 
	 * @param data
	 * @return
	 */
	public byte[] encodeGzip(byte[] data);
	
	/**
	 * 将byte数组解码成对象
	 * 
	 * @param bytes
	 * @return
	 */
	public Object decode(byte[] bytes);
	
	/**
	 * byte数组解压
	 * 
	 * @param bytes
	 * @return
	 */
	public byte[] decodeGzip(byte[] bytes);
}
