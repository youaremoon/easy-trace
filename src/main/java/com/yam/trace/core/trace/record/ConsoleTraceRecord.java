/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.trace.record;

import com.yam.trace.core.trace.Share;
import com.yam.trace.core.util.TraceLogger;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月1日 下午9:58:43
 *
 */
@Share
public class ConsoleTraceRecord extends AbstractTraceRecord {
	@Override
	protected void output(Object msg) {
		TraceLogger.printToConsole(toString(msg), false);
	}
}
