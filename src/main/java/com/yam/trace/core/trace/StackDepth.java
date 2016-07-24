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
	
	private static final ThreadLocal<Integer> STACK_DEPTH = new ThreadLocal<Integer>() {
		protected Integer initialValue() {
			return 0;
		}
	};
	
	/**
	 * 获取当前调用深度
	 * @return
	 */
	public static int getDepth() {
		return STACK_DEPTH.get();
	}

	/**
	 * 调用深度+1
	 * @param posId
	 */
	public static void addCallStack() {
		STACK_DEPTH.set(STACK_DEPTH.get() + 1);
	}
	
	/**
	 * 调用深度-1
	 * @param posId
	 */
	public static void subCallStack() {
		STACK_DEPTH.set(STACK_DEPTH.get() - 1);
	}
}
