/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.intercept;

import java.util.List;

import com.yam.trace.core.config.ClassMethodConfig;
import com.yam.trace.core.config.ClassMethodsConfig;
import com.yam.trace.core.config.EastTraceConfigManager;
import com.yam.trace.core.util.PatternUtil;

/**
 * @Description: 拦截信息管理，暂不考虑扩展
 * @author youaremoon 
 * @date 2016年1月1日 下午8:20:28
 *
 */
public class ClassMethodConfigSearch {
	private static final ClassMethodConfig EMPTY_INTERCEPT_INFO = new ClassMethodConfig();
	
	private ClassMethodConfigSearch() {
	}
	
	/**
	 * 根据类名查找对应的ClassMethodConfig
	 * @param className
	 * @return
	 */
	public static ClassMethodConfig searchFor(String className) {
		ClassMethodsConfig config = getConfig();
		if (null == config) {
			return null;
		}
		
		// 先从system include中找，找到则说明该className需要代理
		ClassMethodConfig fromSystemInclude = findClassMethodConfig(className, config.getIncludeSystemClass());
		if (null != fromSystemInclude) {
			return fromSystemInclude;
		}
		
		// 从system exclude中找,找到表示该className不需要代理
		ClassMethodConfig fromSystemExclude = findClassMethodConfig(className, config.getExcludeSystemClass());
		if (null != fromSystemExclude) {
			return null;
		}
		
		// 从user exclude中找，如果找到说明不需要代理，否则需要代理
		List<ClassMethodConfig> userExclude = config.getExcludeUserClass();
		ClassMethodConfig fromUserExclude = findClassMethodConfig(className, userExclude);
		if (null != fromUserExclude) {
			return null;
		}
		
		// 从user include中找，找到说明该className要代理
		List<ClassMethodConfig> userInclude = config.getIncludeUserClass();
		ClassMethodConfig fromUserInclude = findClassMethodConfig(className, userInclude);
		if (null != fromUserInclude) {
			return fromUserInclude;
		} 
		
		// 如果未配置user include又未排除则需要代理
		if (null == userInclude || userInclude.isEmpty()) {
			return EMPTY_INTERCEPT_INFO;
		}
		
		return null;
	}
	
	private static ClassMethodConfig findClassMethodConfig(String className, List<ClassMethodConfig> classList) {
		if (null == classList) {
			return null;
		}
		
		for (ClassMethodConfig cmc : classList) {
			if (PatternUtil.checkRegex(cmc.getClassRegex() ,className)) {
				return cmc;
			}
		}
		
		return null;
	}
	
	private static ClassMethodsConfig getConfig() {
		return EastTraceConfigManager.getInstance().getClassMethodConfig();
	}
}
