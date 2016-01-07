/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月3日
 * @version V1.0
 */
package com.yam.trace.core.proxy;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javassist.ClassPool;
import javassist.LoaderClassPath;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月3日 下午1:38:30
 *
 */
public class ProxyUtil {
	private static final AtomicInteger POS_ID_GENERATOR = new AtomicInteger(0);
	private static ConcurrentHashMap<ClassLoader, ClassPool> CLASS_POOL_MAP = new ConcurrentHashMap<ClassLoader, ClassPool>();
	
	/**
	 * 分配一个新的位置id, 每个注入点一个位置id，该位置id可以用来做递归分析
	 * @return
	 */
	public static int newPosId() {
		return POS_ID_GENERATOR.incrementAndGet();
	}
	
	/**
	 * 不同的ClassLoader返回不同的ClassPool
	 * @param loader
	 * @return
	 */
	public static ClassPool getClassPool(ClassLoader loader) {
		if (null == loader) {
			return ClassPool.getDefault();
		}
		
		ClassPool pool = CLASS_POOL_MAP.get(loader);
		if (null == pool) {
			pool = new ClassPool(true);
			pool.appendClassPath(new LoaderClassPath(loader));
			CLASS_POOL_MAP.put(loader, pool);
		}
		return pool;
	}
}
