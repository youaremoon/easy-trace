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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yam.trace.core.formatter.MessageFormatter;
import com.yam.trace.core.trace.record.ConsoleTraceRecord;
import com.yam.trace.core.trace.record.ITraceRecord;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月1日 下午11:21:54
 *
 */
public class TraceRecordConfigParser extends AbstractXmlConfigParser<TraceRecordConfig> {
	
	private static final String RECORD_CLASS = "record-class";
	private static final String RECORD_CLASS_INFO = "record-class-info";
	private static final String MESSAGE_FORMATTER_CLASS = "message-formatter-class";

	@Override
	protected TraceRecordConfig doParseToConfig(Node parent) {
		TraceRecordConfig config = new TraceRecordConfig();
		
		NodeList nodeList = parent.getChildNodes();
		int size = nodeList.getLength();
		if (size == 0) {
			return config;
		}
		
		List<ITraceRecord> recordList = new ArrayList<ITraceRecord>(size);
		for (int i = 0; i < size; i++) {
			Node node = nodeList.item(i);
			String text = getNodeText(node);
			if (text.length() == 0) {
				continue;
			}
			
			String nodeName = node.getNodeName();
			if (RECORD_CLASS.equals(nodeName)) {
				ITraceRecord record = create(text);
				if (null != record) {
					recordList.add(record);
				}
			} else if (RECORD_CLASS_INFO.equals(nodeName)) {
				ITraceRecord record = parseRecordClass(node);
				if (null != record) {
					recordList.add(record);
				}
			}
		}
		
		if (!recordList.isEmpty()) {
			config.setRecordList(recordList);
		}
		
		return config;
	}
	
	private ITraceRecord parseRecordClass(Node parent) {
		NodeList nodeList = parent.getChildNodes();
		int size = nodeList.getLength();
		if (size == 0) {
			return null;
		}
		
		ITraceRecord record = null;
		MessageFormatter<?> messageFormatter = null;
		for (int i = 0; i < size; i++) {
			Node node = nodeList.item(i);
			String nodeName = node.getNodeName();
			if (RECORD_CLASS.equals(nodeName)) {
				record = create(getNodeText(node));
			} else if (MESSAGE_FORMATTER_CLASS.equals(nodeName)) {
				messageFormatter = create(getNodeText(node));
			}
		}
		
		if (null != record) {
			record.setMessageFormatter(messageFormatter);
		}
		
		return record;
	}
	
	@Override
	protected void doConfigDefault(TraceRecordConfig config) {
		List<ITraceRecord> recordList = config.getRecordList();
		if (null == recordList) {
			recordList = new ArrayList<ITraceRecord>(1);
		}
		
		if (recordList.isEmpty()) {
			recordList.add(new ConsoleTraceRecord());
			config.setRecordList(recordList);
		}
	}

}
