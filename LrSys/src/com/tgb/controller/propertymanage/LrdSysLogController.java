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
import com.tgb.aop.SystemServiceLog;
import com.tgb.controller.base.BaseController;
import com.tgb.service.propertymanage.LrdOrgService;
import com.tgb.service.propertymanage.LrdSysLogService;
import com.tgb.service.propertymanage.LrdUserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/syslog")
public class LrdSysLogController extends BaseController  {
	Logger log = Logger.getLogger(LrdFunctionController.class);
	@Autowired
	private LrdOrgService lrdOrgService;
	@Autowired
	private LrdSysLogService sysLogService;
	@Autowired
	private LrdUserService lrdUserService; 
	@Autowired
	private DataSourceTransactionManager txManager;
	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "日志管理-查询")
	public String query(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			isPage(map);
			List<Map<String, Object>> resultList = sysLogService.query(map);
		    this.setSuccess(resultList, page);;

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryPK", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "日志管理-查询")
	public String queryPK(HttpServletRequest request,@RequestBody Map<String, Object> map ) {
		//log.info("接收的JSON:" + map.toString());
		try {
			Map<String,Object> resultmap=new HashMap<String, Object>();
			List<Map<String,Object>> resultUser = lrdUserService.query(map);
			List<Map<String, Object>> resultOrg = lrdOrgService.queryMenu(map);
			resultmap.put("ORG", resultOrg);
			resultmap.put("USER", resultUser);
			this.setSuccess(resultmap, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "日志管理--删除")
	public String delete(HttpServletRequest request) {
		try {
			Map<String,Object> map=new HashMap<String, Object>();

			 boolean delete = sysLogService.delete(map);
			this.setSuccess(delete, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/deleteByPk", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
    @SystemServiceLog(description = "日志管理-删除一条")
	public String deleteCustZg(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			// User user = this.getSessionUser(request);
			sysLogService.deleteByPk(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
}
