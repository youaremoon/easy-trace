/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月3日
 * @version V1.0
 */
package com.yam.trace.core.formatter;

import com.yam.trace.core.trace.StackDepth;
import com.yam.trace.core.util.TraceLogger;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月3日 下午1:54:15
 *
 */
public class StringMessageFormatter implements MessageFormatter<String> {

	@Override
	public String formatAfterConstructor(Object proxy, Object[] params) {
		return format("new\t" + getIdentity(proxy));
	}

	@Override
	public String formatBeforeMethod(Object proxy, String method, Object[] params) {
		return format("begin\t" + getIdentity(proxy) + "." + method);
	}

	@Override
	public String formatAfterMethod(Object proxy, String method, Object[] params, Object result) {
		return format("end\t" + getIdentity(proxy) + "." + method + " return " + getIdentity(result));
	}

	@Override
	public String formatBeforeStaticMethod(Class<?> cls, String method, Object[] params) {
		return format("begin static\t" + cls.getName() + "." + method);
	}

	@Override
	public String formatAfterStaticMethod(Class<?> cls, String method, Object[] params, Object result) {
		return format("end static\t" + cls.getName() + "." + method);
	}
	
	private String format(String msg) {
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
	
	private String getIdentity(Object obj) {
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
