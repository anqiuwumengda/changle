package com.tgb.controller;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.tgb.service.cust.CustJsService;
import com.tgb.util.DateTools;
import com.tgb.util.DicReplace;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/custjs")
public class CustJsController extends BaseController {
	Logger log = Logger.getLogger(CustJsController.class);
	@Autowired
	private CustJsService custJsService;
	@Autowired
	private DataSourceTransactionManager txManager;
	@Autowired
	private CustBaseService custBaseService;

	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户-经商信息")
	public String query(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			User user = this.getSessionUser(request);
			// map.put("cust_grp_jl", user.getId());
			// String year = map.get("bizDate").toString();
			String income = "";
			Object year="";
			List<Map<String, Object>> resultList = custJsService.query(map);
			for (Map<String, Object> resMap :resultList) {
				year = resMap.get("BIZ_DATE" );
				if(null!=year && !"".equals(year)){
					String sdate = DateTools.getCurrentSysData("yyyy");
					int result = sdate.compareTo(year.toString());
					if (result > 0) {
						
						income = String.valueOf(Integer.parseInt(sdate)-Integer.parseInt(year.toString()));
					} else {
						income.valueOf(0).toString();
					}
					resMap.put("BIZ_YEARS", income);
				}
				
			}
			
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("DG_WCD", "CUST_GRP1");
			dicMap.put("BIZ_HY", "JYHY");
			DicReplace.replaceDic(resultList, dicMap);

			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "维护客户-经商信息")
	public String save(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			// User user = this.getSessionUser(request);
			List<Map<String, Object>> resultList = custJsService.query(map);
			if (null != resultList && resultList.size() > 0) {
				map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				custJsService.update(map);
				map.put("CUSTBASEMTN_DATE",
						DateTools.getCurrentSysData("yyyyMMdd"));
				custBaseService.updCustBaseMtnData(map);
			} else {
				map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				custJsService.save(map);
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
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			// User user = this.getSessionUser(request);
			custJsService.update(map);
			txManager.commit(status);
			this.setSuccess(null, page);

		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "删除客户-经商信息")
	public String deleteCustZg(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			// User user = this.getSessionUser(request);
			custJsService.deleteByPk(map);
			txManager.commit(status);
			this.setSuccess(null, page);

		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
}
