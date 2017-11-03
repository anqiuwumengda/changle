package com.tgb.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.base.BaseController;
import com.tgb.service.cust.CustNzNeedService;
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/custnzneed")
public class CustNzNeedController extends BaseController {
	Logger log=Logger.getLogger(CustNzNeedController.class);
	@Autowired
	private CustNzNeedService custNzNeedService;
	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户的农资需求")
	public String getNeedInfo(@RequestBody Map<String, Object> map,HttpServletRequest request){
		log.info("查询客户的农资需求接收的JSON:"+map.toString());
		try {
			map.put("DIC_PARENTID", "NZ_TYPE");
			List<Map<String, Object>> resultList = custNzNeedService.query(map);
			this.setSuccess(resultList, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
}
