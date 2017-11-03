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
import com.tgb.service.propertymanage.LrdFunctionService;
import com.tgb.util.DicReplace;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/lrdfunc")
public class LrdFunctionController extends BaseController {
	Logger log = Logger.getLogger(LrdFunctionController.class);
	@Autowired
	private LrdFunctionService lrdFunctionService;
	@Autowired
	private DataSourceTransactionManager txManager;

	@ResponseBody
	@RequestMapping(value = "/querymenu", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询功能列表")
	public String queryTree(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			
			List<Map<String, Object>> resultList = lrdFunctionService.queryTree(map);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询")
	public String qurey(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("FUNC_TYPE", "SYS_FUNC_TYPE");
			List<Map<String, Object>> resultList = lrdFunctionService
					.query(map);
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/queryInsert", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "添加功能-查询")
	public String qureyInsert(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("SYS_FUNC_TYPE", "SYS_FUNC_TYPE");
			List<Map<String, Object>> resultList = lrdFunctionService
					.queryByPK(map);
			DicReplace.replaceDic(resultList, dicMap);
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
			
			lrdFunctionService.save(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/queryupdate", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "功能修改--查询")
	public String queryUpdate(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("SYS_FUNC_TYPE", "SYS_FUNC_TYPE");
			List<Map<String, Object>> resultList = lrdFunctionService
					.query(map);
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/update", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "功能修改--查询")
	public String update(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			 boolean update = lrdFunctionService.update(map);
			this.setSuccess(update, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "功能删除")
	public String delete(HttpServletRequest request,
			@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			String funcCD = map.get("FUNC_CD").toString();
			Map<String, Object> queryMap=new HashMap<String, Object>();
			queryMap.put("FUNC_PCD", funcCD);
			List<Map<String,Object>> queryByPK = lrdFunctionService.queryByPK(queryMap);
			if(queryByPK.size()>0){
				this.setErr(null,"该菜单包含子功能，请先删除子功能");
				return this.js.toString();
			}
			boolean delete = lrdFunctionService.delete(map);
			this.setSuccess(delete, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	

}
