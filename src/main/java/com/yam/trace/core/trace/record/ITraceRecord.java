/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.trace.record;

import com.yam.trace.core.formatter.MessageFormatter;

/**
 * @Description: 拦截跟踪
 * @author youaremoon
 * @date 2016年1月1日 下午9:55:02
 *
 */
public interface ITraceRecord extends Cloneable {
	
	/**
	 * 获取消息格式化实例，帮助对消息进行格式化
	 * @return
	 */
	MessageFormatter<?> getMessageFormatter();
	
	/**
	 * 设置消息格式化实例
	 * @param messageFormatter
	 */
	void setMessageFormatter(MessageFormatter<?> messageFormatter);
	
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