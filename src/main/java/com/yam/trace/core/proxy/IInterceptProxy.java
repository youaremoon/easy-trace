/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.proxy;

import java.security.ProtectionDomain;

import com.yam.trace.core.config.ClassMethodConfig;

/**
 * @Description: 代理类生成
 * @author youaremoon
 * @date 2016年1月1日 下午3:04:38
 *
 */
public interface IInterceptProxy {
	/**
	 * 不生成代理类时的返回值
	 */
	byte[] NOT_PROXY = new byte[0];
	
	/**
	 * 根据拦截信息创建代理类
	 * @param loader
	 * @param className
	 * @param classfileBuffer
	 * @param interceptInfo
	 * @return 不需要创建代理类时返回NOT_PROXY或长度为0的byte[], 创建失败时返回null, 创建成功时返回修改后的字节码
	 */
	byte[] proxyFor(ClassLoader loader, ProtectionDomain protectionDomain, String className, byte[] classfileBuffer, ClassMethodConfig interceptInfo); 
}
