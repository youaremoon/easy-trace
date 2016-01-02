/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月2日
 * @version V1.0
 */
package com.yam.trace.core.config;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yam.trace.core.intercept.DefaultMessageCheck;
import com.yam.trace.core.intercept.IMessageCheck;
import com.yam.trace.core.util.TraceLogger;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月2日 上午12:05:52
 *
 */
public class MessageFilterConfigParser extends AbstractXmlConfigParser<MessageFilterConfig> {
	private static final String MESSAGE_CHECK = "message-check";
	private static final String INCLUDE_THREAD_NAME = "include-thread-name";
	private static final String EXCLUDE_THREAD_NAME = "exclude-thread-name";
	private static final String INCLUDE_MESSAGE = "include-message";
	private static final String EXCLUDE_MESSAGE = "exclude-message";
	
	@Override
	protected MessageFilterConfig doParseToConfig(Node parent) {
		MessageFilterConfig config = new MessageFilterConfig();
		
		NodeList nodeList = parent.getChildNodes();
		int size = nodeList.getLength();
		if (size == 0) {
			return config;
		}
		
		List<String> includeThreadName = new ArrayList<String>();
		List<String> excludeThreadName = new ArrayList<String>();
		List<String> includeMessage = new ArrayList<String>();
		List<String> excludeMessage = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			Node node = nodeList.item(i);
			String text = getNodeText(node);
			if (text.length() == 0) {
				continue;
			}
			
			String nodeName = node.getNodeName();
			if (INCLUDE_THREAD_NAME.equals(nodeName)) {
				includeThreadName.add(text);
			} else if (EXCLUDE_THREAD_NAME.equals(nodeName)) {
				excludeThreadName.add(text);
			} else if (INCLUDE_MESSAGE.equals(nodeName)) {
				includeMessage.add(text);
			} else if (EXCLUDE_MESSAGE.equals(nodeName)) {
				excludeMessage.add(text);
			} else if (MESSAGE_CHECK.equals(nodeName)) {
				if (null != config.getMessageCheck()) {
					TraceLogger.printToConsole("MessageCheck is config as " + config.getMessageCheck().getClass().getName()
							+ ", other config is ignore! className:" + text, true);
				} else {
					IMessageCheck mc = create(text);
					config.setMessageCheck(mc);
				}
			}
		}
		
		if (!includeThreadName.isEmpty()) {
			config.setIncludeThreadName(includeThreadName);
		}
		if (!excludeThreadName.isEmpty()) {
			config.setExcludeThreadName(excludeThreadName);
		}
		if (!includeMessage.isEmpty()) {
			config.setIncludeMessage(includeMessage);
		}
		if (!excludeMessage.isEmpty()) {
			config.setExcludeMessage(excludeMessage);
		}
		
		return config;
	}

	@Override
	protected void doConfigDefault(MessageFilterConfig config) {
		if (null == config.getMessageCheck()) {
			config.setMessageCheck(new DefaultMessageCheck());
		}
	}
}
