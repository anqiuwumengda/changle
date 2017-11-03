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
import com.tgb.controller.base.BaseController;
import com.tgb.model.User;
import com.tgb.service.propertymanage.LrdSysService;
import com.tgb.util.DateTools;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/lrdsys")
public class LrdSysController extends BaseController {
	Logger log = Logger.getLogger(LrdOrgController.class);

	@Autowired
	private LrdSysService lrdSysService;
	@Autowired
	private DataSourceTransactionManager txManager;
	
	@ResponseBody
	@RequestMapping(value = "/querymenu", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询系统")
	public String querySys(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("接收的JSON:" + map.toString());
		try {
			
			List<Map<String, Object>> resultList = lrdSysService.query(map);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
}