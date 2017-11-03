package com.tgb.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.tgb.service.cust.CustZgService;
import com.tgb.util.DateTools;
import com.tgb.util.DicReplace;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/custzg")
public class CustZgController extends BaseController {
	Logger log = Logger.getLogger(CustZgController.class);
	@Autowired
	private CustZgService custZgService;
	@Autowired
	private DataSourceTransactionManager txManager;
	@ResponseBody
	@RequestMapping(value = "/queryCustZgList", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户-职工信息")
	public String queryCustZgList(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String income = "";
			User user = this.getSessionUser(request);
			// map.put("cust_grp_jl", user.getId());
			isPage(map);
			List<Map<String, Object>> resultList = custZgService.query(map);
			Object year="";
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
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/saveCustZg", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "维护客户-职工信息")
	public String saveCustZg(@RequestBody Map<String, Object> map, HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			List<Map<String, Object>> resultList = custZgService.query(map);
			if (null != resultList && resultList.size() > 0) {
				Map<String, Object> tmp = resultList.get(0);
				if (null != tmp && !tmp.isEmpty()) {
					// 存在记录做修改操作
					map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					map.put("CUSTBASEMTN_DATE",
							DateTools.getCurrentSysData("yyyyMMdd"));
					custZgService.update(map);
					custZgService.updCustBaseMtnData(map);
				} else {
					map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					map.put("CUSTBASEMTN_DATE",
							DateTools.getCurrentSysData("yyyyMMdd"));
					custZgService.save(map);
					custZgService.updCustBaseMtnData(map);
				}
			} else {
				map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				map.put("CUSTBASEMTN_DATE",
						DateTools.getCurrentSysData("yyyyMMdd"));
				custZgService.save(map);
				//Double.parseDouble("jjj");
				custZgService.updCustBaseMtnData(map);
			}
			txManager.commit(status);
			// User user = this.getSessionUser(request);
			// map.put("CUST_GRP_JL", "61794");
			// custZgService.save(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/updateCustZg", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	public String updateCustZg(@RequestBody Map<String, Object> map, HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			// User user = this.getSessionUser(request);
			custZgService.update(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/deleteCustZg", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "删除客户-职工信息")
	public String deleteCustZg(@RequestBody Map<String, Object> map, HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			// User user = this.getSessionUser(request);
			custZgService.deleteByPk(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
}
