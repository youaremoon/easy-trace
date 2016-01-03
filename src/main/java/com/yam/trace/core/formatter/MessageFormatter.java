/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月3日
 * @version V1.0
 */
package com.yam.trace.core.formatter;

/**
 * @Description: 消息格式化
 * @author youaremoon
 * @date 2016年1月3日 下午1:42:17
 *
 */
public interface MessageFormatter<T> {

	/**
	 * 构造方法调用信息格式化
	 * @param proxy
	 * @param params
	 * @return
	 */
	T formatAfterConstructor(Object proxy, Object[] params);

	/**
	 * 方法执行前调用信息格式化
	 * @param proxy
	 * @param method
	 * @param params
	 * @return
	 */
	T formatBeforeMethod(Object proxy, String method, Object[] params);

	/**
	 * 方法执行后调用信息格式化
	 * @param proxy
	 * @param method
	 * @param params
	 * @param result
	 * @return
	 */
	T formatAfterMethod(Object proxy, String method, Object[] params, final Object result);
	
	/**
	 * 静态方法执行前调用信息格式化
	 * @param cls
	 * @param method
	 * @param params
	 * @return
	 */
	T formatBeforeStaticMethod(Class<?> cls, String method, Object[] params);
	
	/**
	 * 静态方法执行后调用信息格式化
	 * @param cls
	 * @param method
	 * @param params
	 * @param result
	 * @return
	 */
	T formatAfterStaticMethod(Class<?> cls, String method, Object[] params, final Object result);
}
