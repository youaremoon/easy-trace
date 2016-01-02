/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月2日
 * @version V1.0
 */
package com.yam.trace.core.trace;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月2日 上午12:39:22
 *
 */
public class StackDepth {
	private static final ThreadLocal<Integer> DEPTH = new ThreadLocal<Integer>() {
		protected Integer initialValue() {
			return Integer.valueOf(0);
		}
	};
	
	public static int getDepth() {
		return DEPTH.get();
	}
	
	public static void addDepth() {
		DEPTH.set(DEPTH.get() + 1);
	}
	
	public static void subDepth() {
		DEPTH.set(DEPTH.get() - 1);
	}
}
