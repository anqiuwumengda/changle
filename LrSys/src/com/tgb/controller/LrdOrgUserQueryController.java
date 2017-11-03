package com.tgb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.base.BaseController;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/newFilterConditionQuery")
public class LrdOrgUserQueryController extends BaseController{
	@ResponseBody
	@RequestMapping(value = "/queryConditions", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "获取筛选需要的两个条件：机构和客户经理")
	public Object getFilterConditionsInfo(){
		
		return null;
	}
}
