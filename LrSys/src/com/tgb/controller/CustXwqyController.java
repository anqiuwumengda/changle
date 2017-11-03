package com.tgb.controller;

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
import com.tgb.controller.base.BaseController;
import com.tgb.model.User;
import com.tgb.service.cust.CustBaseService;
import com.tgb.service.cust.CustXwqyService;
import com.tgb.util.DateTools;
import com.tgb.util.DicReplace;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/custxwqy")
public class CustXwqyController extends BaseController {
	Logger log = Logger.getLogger(CustXwqyController.class);
	@Autowired
	private CustXwqyService custXwqyService;
	@Autowired
	private DataSourceTransactionManager txManager;
	@Autowired
	private CustBaseService custBaseService;

	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询工作经营情况-小微企业")
	public String query(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			User user = this.getSessionUser(request);
			// map.put("cust_grp_jl", user.getId());
			isPage(map);
			List<Map<String, Object>> resultList = custXwqyService.query(map);
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("SSHY", "GBHY");
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "保存工作经营情况-小微企业")
	public String save(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			// User user = this.getSessionUser(request);
			List<Map<String, Object>> resultList = custXwqyService.query(map);
			if (null != resultList && resultList.size() > 0) {
				map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				custXwqyService.update(map);
				map.put("CUSTBASEMTN_DATE",
						DateTools.getCurrentSysData("yyyyMMdd"));
				custBaseService.updCustBaseMtnData(map);
			} else {
				// map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				custXwqyService.save(map);
				map.put("CUSTBASEMTN_DATE",
						DateTools.getCurrentSysData("yyyyMMdd"));
				custBaseService.updCustBaseMtnData(map);
			}
			txManager.commit(status);
			this.setSuccess(null, page);

		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/update", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	public String update(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			// User user = this.getSessionUser(request);
			custXwqyService.update(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "删除工作经营情况-小微企业")
	public String deleteCustZg(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			// User user = this.getSessionUser(request);
			custXwqyService.deleteByPk(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
}
