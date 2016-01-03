/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.trace;

import java.util.List;

import com.yam.trace.core.config.EastTraceConfigManager;
import com.yam.trace.core.intercept.IMessageCheck;
import com.yam.trace.core.trace.record.ITraceRecord;
import com.yam.trace.core.util.TraceLogger;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月1日 下午3:20:51
 *
 */
public class TraceEntry {
	public static final String TRACE_CLASSNAME = TraceEntry.class.getName();
	public static final String TRACE_NEW_INSTANCE = TRACE_CLASSNAME + ".create()";
	public static final String TRACE_DEFAULT_INSTANCE = TRACE_CLASSNAME + ".getDefault()";
	
	public static final String TRACE_CONSTRUCTOR_AFTER_PREFIX = "afterConstructor(";
	
	public static final String TRACE_METHOD_BEFORE_PREFIX = "beforeMethod(";
	public static final String TRACE_METHOD_AFTER_PREFIX = "afterMethod(";
	
	public static final String TRACE_STATIC_METHOD_BEFORE_PREFIX = "beforeStaticMethod(";
	public static final String TRACE_STATIC_METHOD_AFTER_PREFIX = "afterStaticMethod(";
	
	private static final TraceEntry DEFAULT_INSTANCE = new TraceEntry();
	
	public static TraceEntry getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static TraceEntry create() {
		return new TraceEntry();
	}
	
	public void afterConstructor(int posId, boolean trace, final Object proxy, final Object[] params) {
		if (!trace || !checkMessage(posId, proxy, null, params)) {
			return;
		}
		
		trace(new TraceExecutor() {
			@Override
			public void execute(ITraceRecord trace) {
				trace.afterConstructor(proxy, params);
			}
		});
	}

	public void beforeMethod(int posId, boolean trace, final Object proxy, final String method, final Object[] params) {
		StackDepth.addCallStack(posId);
		if (!trace || !checkMessage(posId, proxy, method, params)) {
			return;
		}
		
		trace(new TraceExecutor() {
			@Override
			public void execute(ITraceRecord trace) {
				trace.beforeMethod(proxy, method, params);
			}
		});
	}

	public void afterMethod(int posId, boolean trace, final Object proxy, final String method, final Object[] params, final Object result) {
		try {
			if (!trace || !checkMessage(posId, proxy, method, params)) {
				return;
			}
			
			trace(new TraceExecutor() {
				@Override
				public void execute(ITraceRecord trace) {
					trace.afterMethod(proxy, method, params, result);
				}
			});
		} finally {
			StackDepth.subCallStack(posId);
		}
	}
	
	public void beforeStaticMethod(int posId, boolean trace, final Class<?> cls, final String method, final Object[] params) {
		StackDepth.addCallStack(posId);
		
		if (!trace || !checkMessage(posId, cls, method, params)) {
			return;
		}
		
		trace(new TraceExecutor() {
			@Override
			public void execute(ITraceRecord trace) {
				trace.beforeStaticMethod(cls, method, params);
			}
		});
	}
	
	public void afterStaticMethod(int posId, boolean trace, final Class<?> cls, final String method, final Object[] params, final Object result) {
		try {
			if (!trace || !checkMessage(posId, cls, method, params)) {
				return;
			}
			
			trace(new TraceExecutor() {
				@Override
				public void execute(ITraceRecord trace) {
					trace.afterStaticMethod(cls, method, params, result);
				}
			});
		} finally {
			StackDepth.subCallStack(posId);
		}
	}
	
	/**
	 * 检查消息是否被过滤，如果过滤则返回false，否则返回true
	 * @param posId
	 * @param proxy
	 * @param method
	 * @param params
	 * @return
	 */
	private boolean checkMessage(int posId, Object proxy, String method, Object[] params) {
		if (StackDepth.checkRecurse()) {
			if (StackDepth.getContinuousRecurseTime() == 1) {
				TraceLogger.printWarnToConsole("detect direct recursion, end output。 if you want output all, "
					+ "set jvm param com.yam.trace.CLOSE_STACK_TRACE=true, "
					+ "or set com.yam.trace.MAX_RECURSE_LEVEL bigger(>8)。", true);
			}
			return false;
		}
		
		IMessageCheck check = EastTraceConfigManager.getInstance().getMessageFilterConfig().getMessageCheck();
		
		// trace本身的错误导致异常，不能影响程序运行
		try {
			return check.checkMessage(proxy, method, params);
		} catch (Exception ex) {
			TraceLogger.printToConsole(check.getClass().getName() + " cause a exception!", ex);
			return true;
		}
	}
	
	private void trace(TraceExecutor traceExecutor) {
		List<ITraceRecord> recordList = EastTraceConfigManager.getInstance().getTraceRecordConfig().getRecordList();
		for (int i = 0, size = recordList.size(); i < size; i++) {
			ITraceRecord trace = recordList.get(i);
			// trace本身的错误导致异常，不能影响程序运行
			try {
				if (!trace.getClass().isAnnotationPresent(Share.class)) {
					trace = trace.clone();
				}
				
				traceExecutor.execute(trace);
			} catch (Exception ex) {
				TraceLogger.printToConsole(trace.getClass().getName() + " cause a exception!", ex);
			}
		}
	}
	
	private interface TraceExecutor {
		void execute(ITraceRecord trace);
	}
}
