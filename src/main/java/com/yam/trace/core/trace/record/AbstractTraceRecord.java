/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月2日
 * @version V1.0
 */
package com.yam.trace.core.trace.record;

import com.yam.trace.core.trace.StackDepth;
import com.yam.trace.core.util.TraceLogger;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月2日 上午11:53:33
 *
 */
public abstract class AbstractTraceRecord implements ITraceRecord {
	@Override
	public void afterConstructor(Object proxy, Object[] params) {
		formatAndOutput("new\t" + getIdentity(proxy));
	}

	@Override
	public void beforeMethod(Object proxy, String method, Object[] params) {
		formatAndOutput("begin\t" + getIdentity(proxy) + "." + method);
	}

	@Override
	public void afterMethod(Object proxy, String method, Object[] params, final Object result) {
		formatAndOutput("end\t" + getIdentity(proxy) + "." + method + " return " + getIdentity(result));
	}
	
	@Override
	public void beforeStaticMethod(Class<?> cls, String method, Object[] params) {
		formatAndOutput("begin\t" + cls.getName() + "." + method);
	}
	
	@Override
	public void afterStaticMethod(Class<?> cls, String method, Object[] params, final Object result) {
		formatAndOutput("end\t" + cls.getName() + "." + method);
	}
	
	protected void formatAndOutput(String msg) {
		output(format(msg));
	}
	
	protected String format(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append(Thread.currentThread().getName()).append(" ");
		
		int depth = StackDepth.getDepth();
		if (depth > 0) {
			for (int i = 0; i < depth - 1; i++) {
				sb.append("--");
			}
			sb.append("->");
		}
		
		sb.append(msg);
		
		return TraceLogger.getLogInfo(sb.toString());
	}
	
	public AbstractTraceRecord clone() {
		try {
			return (AbstractTraceRecord)super.clone();
		} catch (CloneNotSupportedException e) {
			TraceLogger.printToConsole(e);
			
			return null;
		}
	}
	
	protected abstract void output(String msg);

	protected String getIdentity(Object obj) {
		if (null == obj) {
			return "null";
		}
		
		if (obj instanceof Class) {
			return ((Class<?>) obj).getName();
		} else {
			return obj.getClass().getName();
		}
	}
}
