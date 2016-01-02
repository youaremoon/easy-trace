/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.agent;

import java.lang.instrument.Instrumentation;

import com.yam.trace.core.util.TraceLogger;

/**
 * @Description: agent入口
 * @author youaremoon
 * @date 2016年1月1日 下午1:59:26
 *
 */
public class EasyTraceAgent {
	public static void premain(String args, Instrumentation inst){  
		TraceLogger.printToConsole("Easy Agent start!", true);  
        inst.addTransformer(new EasyTraceTransformer());  
    }
}
