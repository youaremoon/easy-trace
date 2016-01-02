/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月2日
 * @version V1.0
 */
package com.yam.trace.core.config;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月2日 下午2:35:08
 *
 */
class ExcludeClassHolder {
	private static Set<Class<?>> excludeSet = new HashSet<Class<?>>();
	
	public static void registerExclude(Class<?> excludeClass) {
		excludeSet.add(excludeClass);
	}
	
	public static Set<Class<?>> getExcludeSet() {
		return excludeSet;
	}
	
	public static void destory() {
		excludeSet.clear();
		excludeSet = null;
	}
}
