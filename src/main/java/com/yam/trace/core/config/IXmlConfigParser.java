/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.config;

import org.w3c.dom.Node;

/**
 * @Description: xml配置解析
 * @author youaremoon
 * @date 2016年1月1日 下午10:18:25
 *
 */
public interface IXmlConfigParser<T> {
	/**
	 * 解析节点中的配置
	 * @param parent
	 * @return
	 */
	T parseToConfig(Node parent);
	
	/**
	 * 默认配置
	 * @return
	 */
	T configDefault(T empty);
}
