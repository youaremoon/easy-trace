/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.config;

import java.util.List;

/**
 * @Description: ageng配置类 <br>
 * excludeSystemClass 被排除的系统类，
 * includeSystemClass 被包含的系统类，includeSystemClass优先级高于excludeSystemClass，
 * excludeUserClass 被排除的用户类
 * includeUserClass 被包含的用户类，excludeUserClass优先级高于includeUserClass, 如果excludeUserClass未过滤掉，且includeUserClass未配置，则不排除
 * @author youaremoon
 * @date 2016年1月1日 下午2:29:30
 *
 */
public class ClassMethodsConfig {
	private List<ClassMethodConfig> excludeSystemClass;
	private List<ClassMethodConfig> includeSystemClass;
	private List<ClassMethodConfig> excludeUserClass;
	private List<ClassMethodConfig> includeUserClass;
	public List<ClassMethodConfig> getExcludeSystemClass() {
		return excludeSystemClass;
	}
	public void setExcludeSystemClass(List<ClassMethodConfig> excludeSystemClass) {
		this.excludeSystemClass = excludeSystemClass;
	}
	public List<ClassMethodConfig> getIncludeSystemClass() {
		return includeSystemClass;
	}
	public void setIncludeSystemClass(List<ClassMethodConfig> includeSystemClass) {
		this.includeSystemClass = includeSystemClass;
	}
	public List<ClassMethodConfig> getExcludeUserClass() {
		return excludeUserClass;
	}
	public void setExcludeUserClass(List<ClassMethodConfig> excludeUserClass) {
		this.excludeUserClass = excludeUserClass;
	}
	public List<ClassMethodConfig> getIncludeUserClass() {
		return includeUserClass;
	}
	public void setIncludeUserClass(List<ClassMethodConfig> includeUserClass) {
		this.includeUserClass = includeUserClass;
	}
}
