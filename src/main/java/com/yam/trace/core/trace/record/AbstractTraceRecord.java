/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月2日
 * @version V1.0
 */
package com.yam.trace.core.trace.record;

import com.yam.trace.core.formatter.MessageFormatter;
import com.yam.trace.core.formatter.StringMessageFormatter;
import com.yam.trace.core.util.TraceLogger;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月2日 上午11:53:33
 *
 */
public abstract class AbstractTraceRecord implements ITraceRecord {
	private static final MessageFormatter<String> DEFAULT_FORMATTER = new StringMessageFormatter();
	private MessageFormatter<?> messageFormatter;

	@Override
	public MessageFormatter<?> getMessageFormatter() {
		if (null == messageFormatter) {
			return DEFAULT_FORMATTER;
		}
		
		return messageFormatter;
	}

	@Override
	public void setMessageFormatter(MessageFormatter<?> messageFormatter) {
		this.messageFormatter = messageFormatter;
	}
	
	@Override
	public void afterConstructor(Object proxy, Object[] params) {
		output(getMessageFormatter().formatAfterConstructor(proxy, params));
	}

	@Override
	public void beforeMethod(Object proxy, String method, Object[] params) {
		output(getMessageFormatter().formatBeforeMethod(proxy, method, params));
	}

	@Override
	public void afterMethod(Object proxy, String method, Object[] params, final Object result) {
		output(getMessageFormatter().formatAfterMethod(proxy, method, params, result));
	}
	
	@Override
	public void beforeStaticMethod(Class<?> cls, String method, Object[] params) {
		output(getMessageFormatter().formatBeforeStaticMethod(cls, method, params));
	}
	
	@Override
	public void afterStaticMethod(Class<?> cls, String method, Object[] params, final Object result) {
		output(getMessageFormatter().formatAfterStaticMethod(cls, method, params, result));
	}
	
	public AbstractTraceRecord clone() {
		try {
			return (AbstractTraceRecord)super.clone();
		} catch (CloneNotSupportedException e) {
			TraceLogger.printToConsole(e);
			
			return null;
		}
	}
	
	protected String toString(Object msg) {
		return null == msg ? "null" : msg.toString();
	}
	
	protected abstract void output(Object msg);
}
