package com.batch.service;

import org.springframework.http.ResponseEntity;

public interface BatchService {
	
	/**
	 * 导出微信平台所需数据sql
	 * @return
	 * @throws Exception
	 */
	ResponseEntity<byte[]> exportSql() throws Exception;
	
	
	boolean exportSqltoPC() throws Exception;

}
