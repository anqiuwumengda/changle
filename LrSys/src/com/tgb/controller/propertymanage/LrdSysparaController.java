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
import com.tgb.service.propertymanage.LrdSysparaService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/lrdsyspara")
public class LrdSysparaController extends BaseController {
	Logger log = Logger.getLogger(LrdFunctionController.class);
	@Autowired
	private LrdSysparaService lrdSysparaService;
	@Autowired
	private DataSourceTransactionManager txManager;
	
	@ResponseBody
	@RequestMapping(value = "/queryByPK", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "系统参数-查询")
	public String queryByPK(HttpServletRequest request) {
		//log.info("接收的JSON:" + map.toString());
		try {
			Map<String,Object> map=new HashMap<String, Object>();
			List<Map<String, Object>> resultList = lrdSysparaService.query(map);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "去添加功能")
	public String save(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			
			lrdSysparaService.save(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/update", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "系统参数--修改")
	public String update(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:"+map.toString());
		try {
			
			boolean update = lrdSysparaService.update(map);
			this.setSuccess(update, page);
			
		} catch (Exception e) {
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}
	
	
}
