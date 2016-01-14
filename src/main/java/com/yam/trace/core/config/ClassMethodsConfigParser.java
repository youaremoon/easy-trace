/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yam.trace.core.config.ClassMethodConfig.Interst;
import com.yam.trace.core.config.ClassMethodConfig.VisitScope;
import com.yam.trace.core.util.TraceLogger;

/**
 * @Description: ClassMethodConfig解析
 * @author youaremoon
 * @date 2016年1月1日 下午10:17:41
 *
 */
public class ClassMethodsConfigParser extends AbstractXmlConfigParser<ClassMethodsConfig> {

	private static final String INCLUDE_USER_CLASS = "include-user-class";
	private static final String EXCLUDE_SYSTEM_CLASS = "exclude-system-class";
	private static final String INCLUDE_SYSTEM_CLASS = "include-system-class";
	private static final String EXCLUDE_USER_CLASS = "exclude-user-class";
	private static final String INTERCEPT_INFO = "class-method";
	private static final String CLASS_PATTERN = "class-regex";
	private static final String METHOD_PATTERN = "method-regex";
	private static final String INTERST = "interst";
	private static final String VISIT_SCOPE = "visit-scope";
	private static final String WRITE_FILE = "write-file";
	
	@Override
	public ClassMethodsConfig doParseToConfig(Node parent) {
		ClassMethodsConfig loadConfig = new ClassMethodsConfig();
		NodeList nodeList = parent.getChildNodes();
		for (int i = 0, size = nodeList.getLength(); i < size; i++) {
			Node node = nodeList.item(i);
			if (!(node instanceof Element)) {
				continue;
			}
			
			String nodeName = node.getNodeName();
			if (EXCLUDE_SYSTEM_CLASS.equals(nodeName)) {
				loadConfig.setExcludeSystemClass(parseInterceptInfoList(node));
			} else if (INCLUDE_SYSTEM_CLASS.equals(nodeName)) {
				loadConfig.setIncludeSystemClass(parseInterceptInfoList(node));
			} else if (EXCLUDE_USER_CLASS.equals(nodeName)) {
				loadConfig.setExcludeUserClass(parseInterceptInfoList(node));
			} else if (INCLUDE_USER_CLASS.equals(nodeName)) {
				loadConfig.setIncludeUserClass(parseInterceptInfoList(node));
			}
		}
		
		return loadConfig;
	}
	
	private List<ClassMethodConfig> parseInterceptInfoList(Node parent) {
		NodeList nodeList = parent.getChildNodes();
		int size = nodeList.getLength();
		List<ClassMethodConfig> result = new ArrayList<ClassMethodConfig>(size);
		for (int i = 0; i < size; i++) {
			Node node = nodeList.item(i);
			if (!INTERCEPT_INFO.equals(node.getNodeName())) {
				continue;
			}
			
			ClassMethodConfig ii = parseInterceptInfo(node);
			if (null != ii) {
				result.add(ii);
			}
		}
		
		return result;
	}
	
	private ClassMethodConfig parseInterceptInfo(Node node) {
		NodeList subNodeList = node.getChildNodes();
		
		ClassMethodConfig ii = new ClassMethodConfig();
		boolean hasSet = false;
		for (int j = 0, len = subNodeList.getLength(); j < len; j++) {
			Node subNode = subNodeList.item(j);
			String text = getNodeText(subNode);
			if (text.length() == 0) {
				continue;
			}
			
			String nodeName = subNode.getNodeName();
			if (CLASS_PATTERN.equals(nodeName)) {
				ii.setClassRegex(text);
				hasSet = true;
			} else if (METHOD_PATTERN.equals(nodeName)) {
				ii.setMethodRegex(text);
				hasSet = true;
			} else if (INTERST.equals(nodeName)) {
				Set<Interst> interstSet = parseSet(text, ClassMethodConfig.Interst.class);
				if (!interstSet.isEmpty()) {
					ii.setInterstSet(interstSet);
					hasSet = true;
				}
			} else if (VISIT_SCOPE.equals(nodeName)) {
				Set<VisitScope> visitScopeSet = parseSet(text, ClassMethodConfig.VisitScope.class);
				if (!visitScopeSet.isEmpty()) {
					ii.setVisitScopeSet(visitScopeSet);
					hasSet = true;
				}
			} else if (WRITE_FILE.equals(nodeName)) {
				ii.setWriteFile(text);
				hasSet = true;
			}
		}
		
		return hasSet ? ii : null;
	}
	
	private <T extends Enum<T>> Set<T> parseSet(String value, Class<T> cls) {
		String[] vals = value.split(",");
		Set<T> valSet = new HashSet<T>();
		for (String val : vals) {
			try {
				valSet.add(Enum.valueOf(cls, val));
			} catch (Exception ex) {
				TraceLogger.printToConsole("not right config:" + val, ex);
			}
		}
		
		return valSet;
	}

	@Override
	protected void doConfigDefault(ClassMethodsConfig config) {
		// 加入固定的系统包
		List<ClassMethodConfig> oldList = config.getExcludeSystemClass();
		List<ClassMethodConfig> fixList = ClassMethodConfig.generateList(
				"^java/", "^javax/", "^org/ietf/jgss/", 
				"^org/omg/", "^org/w3c/dom/", "^org/xml/sax/", 
				"^sun/", "^com/sun/", "^com/yam/trace/", 
				"^jdk/", "^javassist/");
		if (null == oldList) {
			config.setExcludeSystemClass(fixList);
		} else {
			oldList.addAll(fixList);
		}
	}
}