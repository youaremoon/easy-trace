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
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

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
	private volatile boolean stop;
	private AtomicBoolean started;
	private Queue<Object> msgQueue;
	private Thread th;
	private FileOutputStream fos;
	
	public FileTraceRecord() {
		stop = false;
		started = new AtomicBoolean(false);
		msgQueue = new ConcurrentLinkedQueue<Object>();
	}
	
	public void stop() {
		stop = true;
		th.interrupt();
		th = null;
//		started.set(false);
	}
	
	@Override
	protected void output(Object msg) {
		msgQueue.add(msg);
		
		initThread();
	}
	
	private void doWork() throws IOException {
		StringBuilder sb = new StringBuilder();
		int max = 10;
		for (int i = 0; i < max; i++) {
			Object msg = msgQueue.poll();
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
		
		if (started.get() || !started.compareAndSet(false, true)) {
			return;
		}
		
		th = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!stop) {
					try {
						doWork();
						// sleep久了肉眼就能看出来了 -，-
						Thread.sleep(10);
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
		th.setDaemon(true);
		th.start();
	}
}