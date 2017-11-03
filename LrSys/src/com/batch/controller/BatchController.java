package com.batch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.batch.service.BatchService;
import com.tgb.controller.base.BaseController;

@Controller
@RequestMapping(value = "/batch")
public class BatchController extends BaseController{
	@Autowired
	private BatchService batchService;
	
	/**
	 * 导出微信平台所需sql
	 * @return
	 */
	@RequestMapping(value = "/exportSql")
	@ResponseBody
	public ResponseEntity<byte[]> exportSql() throws Exception{
		ResponseEntity<byte[]> result = batchService.exportSql();
		return result;
	}
}
