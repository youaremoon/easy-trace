/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月2日
 * @version V1.0
 */
package com.yam.trace.core.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月2日 下午1:39:14
 *
 */
public class IOUtil {
	public static FileOutputStream safeOpenFile(String filePath, boolean append) {
		try {
			return openFile(filePath, append);
		} catch (FileNotFoundException e) {
			TraceLogger.printToConsole(e);
			
			return null;
		}
	}
	
	public static FileOutputStream openFile(String filePath, boolean append) throws FileNotFoundException {
		File f = new File(filePath);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		return new FileOutputStream(f, append);
	}
	
	public static void close(Closeable clo) {
		if (null == clo) {
			return;
		}
		try {
			clo.close();
		} catch (IOException e) {
			TraceLogger.printToConsole(e);
		}
	}
}
