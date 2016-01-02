/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月2日
 * @version V1.0
 */
package com.yam.trace.core.intercept;

import com.yam.trace.core.config.MessageFilterConfig;

/**
 * @Description: 用于消息的检测，通过检测的消息才能被trace
 * @author youaremoon
 * @date 2016年1月2日 上午10:01:50
 *
 */
public interface IMessageCheck {

	/**
	 * 检查消息是否可以处理
	 * 检查内容：1、thread name，2、class name + method name
	 * @param proxy
	 * @param method
	 * @param params
	 * @return
	 */
	public abstract boolean checkMessage(Object proxy, String method, Object[] params);

	/**
	 * 检测当前thread name是否可以处理
	 * @param config
	 * @return
	 */
	public abstract boolean checkCurrentThread(MessageFilterConfig config);

	/**
	 * 检测指定线程是否可以处理
	 * @param th
	 * @param config
	 * @return
	 */
	public abstract boolean checkThread(Thread th, MessageFilterConfig config);

	/**
	 * 检测指定消息是否可以处理
	 * @param msg
	 * @param config
	 * @return
	 */
	public abstract boolean checkMessage(String msg, MessageFilterConfig config);

}