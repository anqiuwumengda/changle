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
import com.tgb.service.cust.CustInputSumService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/custinputsum")
public class CustInputSumController extends BaseController {
	Logger log=Logger.getLogger(CustInputSumController.class); 
	@Autowired
	private CustInputSumService custInputSumService;
	@Autowired
	private DataSourceTransactionManager txManager;
	@Autowired
	private CustBaseService custBaseService;
	
	@ResponseBody
	@RequestMapping(value = "/querySum", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "统计-客户经理月录入信息")
	public String querySum(HttpServletRequest request) {
		try {
			User user = this.getSessionUser(request);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("user_id", user.getUser_id());
			List<Map<String, Object>> resultList = custInputSumService.query(map);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
}


