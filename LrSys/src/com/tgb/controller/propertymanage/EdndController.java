package com.tgb.controller.propertymanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.CustApplyController;
import com.tgb.controller.base.BaseController;
import com.tgb.service.propertymanage.EdendService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/edend")
public class EdndController extends BaseController {
	Logger log = Logger.getLogger(CustApplyController.class);

	@Autowired
	private EdendService edndService;
	@Autowired
	private DataSourceTransactionManager txManager;

	@ResponseBody
	@RequestMapping(value = "/queryList", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询")
	public String queryList(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("接收的JSON:" + map);
		try {
			List<Map<String, Object>> queryList = edndService.queryList(map);
			this.setSuccess(queryList, page);
		} catch (Exception e) {
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询单个额度")
	public String query(HttpServletRequest request, @RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map);
		try {
			Map<String, Object> query = edndService.query(map);
			this.setSuccess(query, null);
		} catch (Exception e) {
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/update", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "修改")
	public String update(HttpServletRequest request, @RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map);
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			edndService.update(map);
			txManager.commit(status);
			this.setSuccess(null, page);
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}
}
