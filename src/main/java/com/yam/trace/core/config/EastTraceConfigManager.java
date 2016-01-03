/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.config;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yam.trace.core.util.PatternUtil;
import com.yam.trace.core.util.TraceLogger;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月1日 下午2:28:34
 *
 */
public class EastTraceConfigManager {
	private static final String SYSTEM_CONFIG_PATH_PARAM = "com.yam.trace.CONFIG_PATH";
	private static final String ESAY_TRACE_XML = "/esay-trace.xml";
	private static final String CLASS_METHODS = "class-methods";
	private static final String TRACE_RECORDS = "trace-records";
	private static final String MESSAGE_FILTERS = "message-filters";

	private static final EastTraceConfigManager instance = new EastTraceConfigManager();
	
	private ClassMethodsConfig classMethodConfig;
	private TraceRecordConfig traceRecordConfig;
	private MessageFilterConfig messageFilterConfig;
	
	private EastTraceConfigManager() {
		initConfig();
	}

	public static EastTraceConfigManager getInstance() {
		return instance;
	}
	
	public ClassMethodsConfig getClassMethodConfig() {
		return classMethodConfig;
	}
	
	public TraceRecordConfig getTraceRecordConfig() {
		return traceRecordConfig;
	}
	
	public MessageFilterConfig getMessageFilterConfig() {
		return messageFilterConfig;
	}
	
	// 初始化配置
	private void initConfig() {
		loadConfigFromXml();
		
		checkConfig();
	}
	
	private void loadConfigFromXml() {
		String configPath = System.getProperty(SYSTEM_CONFIG_PATH_PARAM, ESAY_TRACE_XML);
		InputStream is = ClassMethodsConfig.class.getResourceAsStream(configPath);
		if (null == is) {
			TraceLogger.printWarnToConsole("cannot find config file: " + configPath + ", use default config.", true);
			return;
		}
		
		// 尽量少依赖外部类，因此用原始java的xml解析
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance(); 
		Document doc = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(is);
		} catch (Exception e) {
			TraceLogger.printToConsole(e);
			
			return;
		} 
		
		Node root = doc.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		for (int i = 0, size = nodeList.getLength(); i < size; i++) {
			Node node = nodeList.item(i);
			if (!(node instanceof Element)) {
				continue;
			}
				
			String nodeName = node.getNodeName();
			try {
				// 隔离错误
				if (CLASS_METHODS.equals(nodeName)) {
					classMethodConfig = new ClassMethodsConfigParser().parseToConfig(node);
				} else if (TRACE_RECORDS.equals(nodeName)) {
					traceRecordConfig = new TraceRecordConfigParser().parseToConfig(node);
				} else if (MESSAGE_FILTERS.equals(nodeName)) {
					messageFilterConfig = new MessageFilterConfigParser().parseToConfig(node);
				}
			} catch (Exception e) {
				TraceLogger.printToConsole(e);
			} 
		}
	}
	
	private void checkConfig() {
		if (null == classMethodConfig) {
			classMethodConfig = new ClassMethodsConfig();
			new ClassMethodsConfigParser().configDefault(classMethodConfig);
		}
		
		if (null == traceRecordConfig) {
			traceRecordConfig = new TraceRecordConfig();
			new TraceRecordConfigParser().configDefault(traceRecordConfig);
		}
		
		if (null == messageFilterConfig) {
			messageFilterConfig = new MessageFilterConfig();
			new MessageFilterConfigParser().configDefault(messageFilterConfig);
		}
		
		Set<Class<?>> excludeSet = ExcludeClassHolder.getExcludeSet();
		if (null != excludeSet) {
			for (Class<?> cls : excludeSet) {
				registerExclude(cls, classMethodConfig);
			}
			
			ExcludeClassHolder.destory();
		}
	}
	
	private static void registerExclude(Class<?> cls, ClassMethodsConfig config) {
		if (null == config) {
			return;
		}
		
		String className = "^" + cls.getName().replace('.', '/');
		remove(className, config.getIncludeUserClass());
		remove(className, config.getIncludeSystemClass());
		
		add(className, config.getExcludeSystemClass());
	}
	
	private static void remove(String exp, List<ClassMethodConfig> expList) {
		if (isEmpty(expList)) {
			return;
		}
		
		for (int i = 0, size = expList.size(); i < size; i++) {
			ClassMethodConfig cmc = expList.get(i);
			if (PatternUtil.checkRegex(cmc.getClassRegex() , exp)) {
				expList.remove(i--);
			}
		}
	}
	
	private static void add(String exp, List<ClassMethodConfig> expList) {
		if (isEmpty(expList)) {
			return;
		}
		
		for (int i = 0, size = expList.size(); i < size; i++) {
			ClassMethodConfig cmc = expList.get(i);
			if (PatternUtil.checkRegex(cmc.getClassRegex() , exp)) {
				return;
			}
		}
		
		// 没找到再添加
		expList.add(ClassMethodConfig.generate(exp));
	}
	
	private static boolean isEmpty(List<?> confList) {
		return null == confList || confList.isEmpty();
	}
	
	public static void main(String[] args) {
		InputStream is = ClassMethodsConfig.class.getResourceAsStream("/esay-trace-demo.xml");
		System.out.println(is);
	}
}
