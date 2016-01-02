/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月2日
 * @version V1.0
 */
package com.yam.trace.core.trace.record;

import java.io.FileOutputStream;
import java.io.IOException;

import com.yam.trace.core.util.IOUtil;
import com.yam.trace.core.util.TraceLogger;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月2日 下午12:42:50
 *
 */
public class FilePerThreadTraceRecord extends AbstractTraceRecord {
	private static final ThreadLocal<FileOutputStream> FOS = new ThreadLocal<FileOutputStream>() {
		private FileOutputStream fos;
		protected FileOutputStream initialValue() {
			String fileName = "./logs/trace_" + Thread.currentThread().getName() + ".log";
	        return fos = IOUtil.safeOpenFile(fileName, true);
	    }
		
		public void remove() {
			if (null != fos) {
				try {
					fos.flush();
				} catch (IOException e) {
					TraceLogger.printToConsole(e);
				}
				
				IOUtil.close(fos);
				fos = null;
			}
		}
	};

	@Override
	protected void output(String msg) {
		try {
			FOS.get().write((msg + "\r\n").getBytes("utf-8"));
		} catch (IOException e) {
			TraceLogger.printToConsole(e);
		}
	}
}
