/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月1日 下午7:42:19
 *
 */
public class TraceLogger {
	private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
		}
	};
	
	public static void printToConsole(String info, Throwable th) {
		printWarnToConsole(info, true);
		th.printStackTrace();
	}
	
	/**
	 * 将信息打印到控制台
	 * @param info
	 */
	public static void printToConsole(String info, boolean addTime) {
		System.out.println(addTime ? getLogInfo(info) : info);
	}
	
	public static void printWarnToConsole(String info, boolean addTime) {
		System.err.println(addTime ? getLogInfo(info) : info);
	}
	
	public static void printToConsole(Throwable th) {
		th.printStackTrace();
	}
	
	public static String getLogInfo(String info) {
		return getCurrentTime() + "\t" + info;
	}
	
	public static String getCurrentTime() {
		return DATE_FORMAT.get().format(new Date());
	}
}
