/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月2日
 * @version V1.0
 */
package com.yam.trace.core.config;

import java.util.List;

import com.yam.trace.core.intercept.IMessageCheck;

/**
 * @Description: 消息过滤config
 * 其中excludeXXX优先级高于includeXXX
 * @author youaremoon
 * @date 2016年1月2日 上午12:05:21
 *
 */
public class MessageFilterConfig {
	// messageCheck作为一个chain更合适，暂时懒得改
	private IMessageCheck messageCheck;
	private List<String> includeThreadName;
	private List<String> excludeThreadName;
	private List<String> includeMessage;
	private List<String> excludeMessage;
	
	public IMessageCheck getMessageCheck() {
		return messageCheck;
	}
	public void setMessageCheck(IMessageCheck messageCheck) {
		this.messageCheck = messageCheck;
	}
	public List<String> getIncludeThreadName() {
		return includeThreadName;
	}
	public void setIncludeThreadName(List<String> includeThreadName) {
		this.includeThreadName = includeThreadName;
	}
	public List<String> getExcludeThreadName() {
		return excludeThreadName;
	}
	public void setExcludeThreadName(List<String> excludeThreadName) {
		this.excludeThreadName = excludeThreadName;
	}
	public List<String> getIncludeMessage() {
		return includeMessage;
	}
	public void setIncludeMessage(List<String> includeMessage) {
		this.includeMessage = includeMessage;
	}
	public List<String> getExcludeMessage() {
		return excludeMessage;
	}
	public void setExcludeMessage(List<String> excludeMessage) {
		this.excludeMessage = excludeMessage;
	}
}
