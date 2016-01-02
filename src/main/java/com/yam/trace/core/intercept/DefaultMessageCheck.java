/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月2日
 * @version V1.0
 */
package com.yam.trace.core.intercept;

import java.util.List;

import com.yam.trace.core.config.EastTraceConfigManager;
import com.yam.trace.core.config.MessageFilterConfig;
import com.yam.trace.core.util.PatternUtil;

/**
 * @Description:
 * @author youaremoon
 * @date 2016年1月2日 上午9:20:14
 *
 */
public class DefaultMessageCheck implements IMessageCheck {

	@Override
	public boolean checkMessage(Object proxy, String method, Object[] params) {
		MessageFilterConfig config = EastTraceConfigManager.getInstance().getMessageFilterConfig();
		if (null == config) {
			return true;
		}
		
		String threadName = Thread.currentThread().getName();
		if (!check(threadName, config.getExcludeThreadName(), config.getIncludeThreadName())) {
			return false;
		}
		
		String msg = getClassIndentifier(proxy) + " " + method;
		return check(msg, config.getExcludeMessage(), config.getIncludeMessage());
	}

	@Override
	public boolean checkCurrentThread(MessageFilterConfig config) {
		return checkThread(Thread.currentThread(), config);
	}
	
	@Override
	public boolean checkThread(Thread th, MessageFilterConfig config) {
		if (null == config) {
			config = EastTraceConfigManager.getInstance().getMessageFilterConfig();
		}
		if (null == config) {
			return true;
		}
		
		return check(th.getName(), config.getExcludeThreadName(), config.getIncludeThreadName());
	}

	@Override
	public boolean checkMessage(String msg, MessageFilterConfig config) {
		if (null == config) {
			config = EastTraceConfigManager.getInstance().getMessageFilterConfig();
		}
		if (null == config) {
			return true;
		}
		
		return check(msg, config.getExcludeMessage(), config.getIncludeMessage());
	}
	
	private boolean check(String msg, List<String> excludeList, List<String> includeList) {
		if(findFilter(msg, excludeList)) {
			return false;
		}
		
		if (findFilter(msg, includeList)) {
			return true;
		} else if (null == includeList || includeList.isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	private boolean findFilter(String msg, List<String> filterList) {
		if (null == msg || msg.length() == 0) {
			return false;
		}
		
		if (null == filterList || filterList.isEmpty()) {
			return false;
		}
		
		for (String filter : filterList) {
			if (PatternUtil.checkRegex(filter, msg)) {
				return true;
			}
		}
		
		return false;
	}
	
	private static String getClassIndentifier(Object proxy) {
		if (null == proxy) {
			return "null";
		}
		
		if (proxy instanceof Class) {
			return ((Class<?>) proxy).getName();
		} else {
			return proxy.getClass().getName();
		}
	}
}
