/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.yam.trace.core.util.TraceLogger;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月1日 下午11:17:26
 *
 */
public abstract class AbstractXmlConfigParser<T> implements IXmlConfigParser<T> {
	public T parseToConfig(Node parent) {
		T config = doParseToConfig(parent);
		
		configDefault(config);
		
		return config;
	}
	
	public T configDefault(T config) {
		doConfigDefault(config);
		
		return config;
	}
	
	@SuppressWarnings({ "hiding", "unchecked" })
	protected <T> T create(String className) {
		try {
			Class<?> cls = Class.forName(className);
			T result = (T)cls.newInstance();
			// 放到排除列表，防止递归
			ExcludeClassHolder.registerExclude(cls);
			
			return result;
		} catch (Exception e) {
			TraceLogger.printToConsole(e);
			return null;
		}
	}
	
	protected String getNodeText(Node node) {
		if (!(node instanceof Element)) {
			return "";
		}
		
		return ((Element)node).getTextContent().trim();
	}
	
	protected abstract T doParseToConfig(Node parent);
	protected abstract void doConfigDefault(T config);
}
