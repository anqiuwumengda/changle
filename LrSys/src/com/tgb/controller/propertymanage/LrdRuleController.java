package com.tgb.controller.propertymanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.base.BaseController;
import com.tgb.service.propertymanage.LrdRuleService;
import com.tgb.util.DicReplace;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/lrdrule")
public class LrdRuleController extends BaseController {
	Logger log = Logger.getLogger(LrdRuleController.class);
	@Autowired
	private LrdRuleService lrdRuleService;
	@Autowired
	private DataSourceTransactionManager txManager;

	@ResponseBody
	@RequestMapping(value = "/queryList", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询制度列表")
	public String queryList(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			isPage(map);
			List<Map<String, Object>> resultList = lrdRuleService.queryList(map);
			if (null != page) {
				this.page.removeContext();
			}
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/queryById", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "根据制度编号查询")
	public String queryById(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			Map<String, Object> resultList = lrdRuleService.queryById(map);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "添加制度")
	public String save(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			lrdRuleService.save(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/update", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "修改制度")
	public String update(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			 boolean update = lrdRuleService.update(map);
			this.setSuccess(update, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "制度删除")
	public String delete(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			boolean delete = lrdRuleService.delete(map);
			this.setSuccess(delete, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	

}
