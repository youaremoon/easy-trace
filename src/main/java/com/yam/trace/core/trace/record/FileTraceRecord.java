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
import java.util.LinkedList;
import java.util.Queue;

import com.yam.trace.core.trace.Share;
import com.yam.trace.core.util.IOUtil;
import com.yam.trace.core.util.TraceLogger;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月2日 下午12:15:30
 *
 */
@Share
public class FileTraceRecord extends AbstractTraceRecord {
	private boolean stop;
	private Queue<String> msgQueue;
	private Thread th;
	private FileOutputStream fos;
	
	public FileTraceRecord() {
		stop = false;
		msgQueue = new LinkedList<String>();
	}
	
	public void stop() {
		stop = true;
		th = null;
	}
	
	@Override
	protected synchronized void output(String msg) {
		msgQueue.add(msg);
		
		initThread();
	}
	
	private void doWork() throws IOException {
		StringBuilder sb = new StringBuilder();
		int max = 10;
		for (int i = 0; i < max; i++) {
			String msg = msgQueue.poll();
			if (null == msg) {
				break;
			}
			sb.append(msg).append("\r\n");
		}
		
		if (null == fos) {
			fos = IOUtil.openFile("./logs/trace.log", true);
		}
		fos.write(sb.toString().getBytes("utf-8"));
	}
	
	private void initThread() {
		if (stop) {
			throw new IllegalStateException("thread is stop!");
		}
		
		if (null != th) {
			return;
		}
		
		th = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!stop) {
					try {
						doWork();
						// sleep久了肉眼就能看出来了 -，-
						Thread.sleep(100);
					} catch (Exception ex) {
						TraceLogger.printToConsole(ex);
					}
				}
				
				msgQueue.clear();
				if (null != fos) {
					try {
						fos.flush();
						fos.close();
					} catch (IOException e) {
						TraceLogger.printToConsole(e);
					}
					fos = null;
				}
			}
		});
		th.start();
	}
}