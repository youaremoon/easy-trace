/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.trace.record;

/**
 * @Description: 拦截跟踪
 * @author youaremoon
 * @date 2016年1月1日 下午9:55:02
 *
 */
public interface ITraceRecord extends Cloneable {
	/**
	 * 构造方法执行完成后触发
	 * @param trace
	 * @param proxy
	 * @param params
	 */
	void afterConstructor(Object proxy, Object[] params);

	/**
	 * 方法执行前触发
	 * @param trace
	 * @param proxy
	 * @param method
	 * @param params
	 */
	void beforeMethod(Object proxy, String method, Object[] params);

	/**
	 * 方法执行后触发
	 * @param trace
	 * @param proxy
	 * @param method
	 * @param params
	 */
	void afterMethod(Object proxy, String method, Object[] params, final Object result);

	/**
	 * 静态方法执行前触发
	 * @param trace
	 * @param cls
	 * @param method
	 * @param params
	 */
	void beforeStaticMethod(Class<?> cls, String method, Object[] params);

	/**
	 * 静态方法执行后触发
	 * @param trace
	 * @param cls
	 * @param method
	 * @param params
	 */
	void afterStaticMethod(Class<?> cls, String method, Object[] params, final Object result);

	ITraceRecord clone();
}