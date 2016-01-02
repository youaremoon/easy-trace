/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月2日
 * @version V1.0
 */
package com.yam.trace.core.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月2日 上午12:23:04
 *
 */
public class PatternUtil {
	private static ConcurrentHashMap<String, Pattern> CACHE = new ConcurrentHashMap<String, Pattern>();
	
	public static boolean checkRegex(String regex, String text) {
		Pattern p = CACHE.get(regex);
		if (null == p) {
			p = Pattern.compile(regex);
			CACHE.put(regex, p);
		}
		
		return p.matcher(text).find();
	}
	
	public static void main(String[] args) {
		System.out.println(checkRegex("^com/java/", "com/java/h"));
	}
}
