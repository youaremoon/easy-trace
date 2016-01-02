/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import com.yam.trace.core.config.ClassMethodConfig;
import com.yam.trace.core.intercept.ClassMethodConfigSearch;
import com.yam.trace.core.proxy.ByteCodeProxy;
import com.yam.trace.core.proxy.IInterceptProxy;
import com.yam.trace.core.util.TraceLogger;

/**
 * @Description: 根据配置生成类代理
 * @author youaremoon
 * @date 2016年1月1日 下午2:03:16
 *
 */
public class EasyTraceTransformer implements ClassFileTransformer {
	
	private IInterceptProxy[] proxys = { new ByteCodeProxy() };

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, 
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		if (null != classBeingRedefined) {
			TraceLogger.printToConsole("redefine " + classBeingRedefined.getClass().getName(), true);
		}
		
		// 获取拦截配置信息
		ClassMethodConfig classMethodConfig = ClassMethodConfigSearch.searchFor(className);
		if (null == classMethodConfig) {
			return classfileBuffer;
		}
		
		// 生成代理类
		for (int i = 0; i < proxys.length; i++) {
			byte[] result = proxys[i].proxyFor(loader, protectionDomain, className, classfileBuffer, classMethodConfig);
			if (null != result) {
				return result;
			}
		}
		
		// 生成失败则返回原信息
		return classfileBuffer;
	}
}
