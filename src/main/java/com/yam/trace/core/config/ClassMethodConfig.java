/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Description: 拦截配置<br>
 * classRegex表示拦截类的正则表达式，如^com/yam/, 则com.yam.trace.core.agent.EsayAgent满足；<br/>
 * methodRegex表示需要被代理的方法的正则表达式。
 * interst 为关注的时间，
 * @author youaremoon
 * @date 2016年1月1日 下午2:33:07
 *
 */
public class ClassMethodConfig {
	private String classRegex;
	private String methodRegex;
	private Set<Interst> interstSet;
	private Set<VisitScope> visitScopeSet;
	private String writeFile;
	
	public static enum Interst {
		/**
		 * 构造方法
		 */
		CONSTRUCTOR,
		/**
		 * 普通方法开始前
		 */
		METHOD_BEFORE,
		/**
		 * 普通方法结束后
		 */
		METHOD_AFTER,
		/**
		 * 静态方法开始前
		 */
		STATIC_BEFORE,
		/**
		 * 静态方法结束后
		 */
		STATIC_AFTER
	}
	
	public static enum VisitScope {
		/**
		 * 私有
		 */
		PRIVATE,
		/**
		 * 公共
		 */
		PUBLIC,
		/**
		 * 
		 */
		PROTECTED,
		/**
		 * 包级别
		 */
		PACK
	}
	
	/**
	 * 是否关注指定类型的方法
	 * @param interst
	 * @return
	 */
	public boolean interstAt(Interst interst) {
		if (null == interstSet || interstSet.isEmpty()) {
			return true;
		}
		
		return interstSet.contains(interst);
	}
	
	/**
	 * 是否访问对应访问权限的方法
	 * @param visitScope
	 * @return
	 */
	public boolean canVisit(VisitScope visitScope) {
		if (null == visitScopeSet || visitScopeSet.isEmpty()) {
			return true;
		}
		
		return visitScopeSet.contains(visitScope);
	}
	
	public String getClassRegex() {
		return classRegex;
	}

	public void setClassRegex(String classRegex) {
		this.classRegex = classRegex;
	}

	public String getMethodRegex() {
		return methodRegex;
	}

	public void setMethodRegex(String methodRegex) {
		this.methodRegex = methodRegex;
	}
	
	public Set<Interst> getInterstSet() {
		return interstSet;
	}

	public void setInterstSet(Set<Interst> interstSet) {
		this.interstSet = interstSet;
	}
	
	public Set<VisitScope> getVisitScopeSet() {
		return visitScopeSet;
	}

	public void setVisitScopeSet(Set<VisitScope> visitScopeSet) {
		this.visitScopeSet = visitScopeSet;
	}

	public String getWriteFile() {
		return writeFile;
	}

	public void setWriteFile(String writeFile) {
		this.writeFile = writeFile;
	}

	public static List<ClassMethodConfig> generateList(String...classRegexs) {
		if (null == classRegexs || classRegexs.length == 0) {
			return null;
		}
		
		List<ClassMethodConfig> result = new ArrayList<ClassMethodConfig>(classRegexs.length);
		for (String classRegex : classRegexs) {
			result.add(generate(classRegex));
		}
		return result;
	}
	
	public static ClassMethodConfig generate(String classRegex) {
		ClassMethodConfig ii = new ClassMethodConfig();
		ii.setClassRegex(classRegex);
		
		return ii;
	}
}
