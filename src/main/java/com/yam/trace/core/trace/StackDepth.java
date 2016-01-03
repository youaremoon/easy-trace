/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月2日
 * @version V1.0
 */
package com.yam.trace.core.trace;

import java.util.LinkedList;

import com.yam.trace.core.util.TraceLogger;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月2日 上午12:39:22
 *
 */
public class StackDepth {
	/**
	 * 是否关闭stack trace, 默认为开启
	 */
	private static final boolean CLOSE_STACK_TRACE;
	private static final int MAX_RECURSE_LEVEL;
	static {
		String closeStackTrace = System.getProperty("com.yam.trace.CLOSE_STACK_TRACE");
		CLOSE_STACK_TRACE = Boolean.parseBoolean(closeStackTrace);
	
		String maxRecurseLevel = System.getProperty("com.yam.trace.MAX_RECURSE_LEVEL");
		int num = 0;
		if (null != maxRecurseLevel && maxRecurseLevel.length() > 0) {
			try {
				num = Integer.parseInt(maxRecurseLevel);
			} catch (NumberFormatException ex) {
				TraceLogger.printWarnToConsole("com.yam.trace.MAX_RECURSE_LEVEL must be a number:" + maxRecurseLevel, false);
			}
		}
		
		if (num <= 0) {
			num = 8;
		}
		
		MAX_RECURSE_LEVEL = num;
		
		TraceLogger.printToConsole("CLOSE_STACK_TRACE=" + CLOSE_STACK_TRACE 
				+ ", MAX_RECURSE_LEVEL=" + MAX_RECURSE_LEVEL, false);
	}
	
	private static final ThreadLocal<StackDepthInfo> STACK_DEPTH = new ThreadLocal<StackDepthInfo>() {
		protected StackDepthInfo initialValue() {
			return new StackDepthInfo();
		}
	};
	
	/**
	 * 获取当前调用深度
	 * @return
	 */
	public static int getDepth() {
		return STACK_DEPTH.get().depth;
	}

	/**
	 * 往调用栈顶增加一个posId
	 * @param posId
	 */
	public static void addCallStack(int posId) {
		StackDepthInfo info = STACK_DEPTH.get();
		info.depth++;
		
		if (!CLOSE_STACK_TRACE) {
			info.callStack.addFirst(posId);
		}
	}
	
	/**
	 * 去除调用栈栈顶posId， 如果传入的posId与指定元素不符则抛出异常
	 * @param posId
	 */
	public static void subCallStack(int posId) {
		StackDepthInfo info = STACK_DEPTH.get();
		info.depth--;
		
		if (!CLOSE_STACK_TRACE) {
			LinkedList<Integer> stack = info.callStack;
			Integer last = stack.removeFirst();
			if (null == last || last.intValue() != posId) {
				if (null != last) {
					stack.addFirst(posId);
				}
				throw new RuntimeException("expect " + posId + " in stack, but find " + last);
			}
		}
	}
	
	/**
	 * 检测是否发生直接递归调用
	 * @return
	 */
	public static boolean checkRecurse() {
		if (CLOSE_STACK_TRACE) {
			return false;
		}
		
		StackDepthInfo info = STACK_DEPTH.get();
		LinkedList<Integer> stack = info.callStack;
		if (stack.size() < MAX_RECURSE_LEVEL) {
			return false;
		}
		
		int depth = MAX_RECURSE_LEVEL - 1;
		Integer first = stack.getFirst();
		boolean isFirst = true;
		for (Integer posId : stack) {
			// 第一个不用比
			if (isFirst) {
				isFirst = false;
				continue;
			}
			
			if (!posId.equals(first)) {
				info.continuousRecurseTime = 0;
				return false;
			}
			
			// 如果之前就检测到递归，现在的节点又和头结点相同，则现在还是递归
			// 如果递归达到阈值，判定检查到递归
			if (info.continuousRecurseTime > 0 || --depth == 0) {
				info.continuousRecurseTime++;
				return true;
			}
		}
		
		info.continuousRecurseTime = 0;
		return false;
	}
	
	/**
	 * 连续递归次数
	 * @return
	 */
	public static int getContinuousRecurseTime() {
		return STACK_DEPTH.get().continuousRecurseTime;
	}
	
	private static class StackDepthInfo {
		/**
		 * 调用深度
		 */
		int depth = 0;
		/**
		 * 调用栈，LinkedList中存的是posId,后调用的posId在前面
		 */
		LinkedList<Integer> callStack = CLOSE_STACK_TRACE ? null : new LinkedList<Integer>();
		// 连续递归的次数，当检查到非递归时，此值清零
		int continuousRecurseTime = 0;
	}
}
