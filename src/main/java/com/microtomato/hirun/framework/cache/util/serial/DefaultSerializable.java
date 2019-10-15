package com.microtomato.hirun.framework.cache.util.serial;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 默认基于 Java 序列化与反序列化实现
 *
 * @author Steven
 * @date 2019-10-15
 */
@Slf4j
public class DefaultSerializable extends AbstractSerializable {

	/**
	 * 将对象编码成byte数组
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	public byte[] encode(Object obj) {

		byte[] rtn = null;

		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;

		try {
			
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			rtn = baos.toByteArray();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			  if (null != oos) { 
				  try { 
					  oos.close(); 
				  } catch (IOException e) {
				
				  } 
			  }
		}

		return rtn;
	}

	/**
	 * 将byte数组解码成对象
	 * 
	 * @param bytes
	 * @return
	 */
	@Override
	public Object decode(byte[] bytes) {

		Object rtn = null;

		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;

		try {
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			rtn = ois.readObject();
		} catch (IOException e) {
			log.error("", e);
		} catch (ClassNotFoundException e) {
			log.error("", e);
		} finally {
			 if (null != ois) { 
				 try { 
					 ois.close(); 
				 } catch (IOException e) {
				 } 
			 }
		}

		return rtn;
	}

}
