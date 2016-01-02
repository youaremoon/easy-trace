/**
 * Copyright: Copyright (c) 2015 
 * 
 * @author youaremoon
 * @date 2016年1月1日
 * @version V1.0
 */
package com.yam.trace.core.config;

import java.util.List;

import com.yam.trace.core.trace.record.ITraceRecord;

/**
 * @Description: TODO
 * @author youaremoon
 * @date 2016年1月1日 下午10:01:01
 *
 */
public class TraceRecordConfig {
	private List<ITraceRecord> recordList;

	public List<ITraceRecord> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<ITraceRecord> recordList) {
		this.recordList = recordList;
	}
}
