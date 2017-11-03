package com.tgb.controller;

import java.text.SimpleDateFormat;
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
import com.tgb.service.cust.CustApplyService;
import com.tgb.service.cust.CustBaseService;
import com.tgb.util.DateTools;
import com.tgb.util.DicReplace;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/custapply")
public class CustApplyController extends BaseController {
	Logger log=Logger.getLogger(CustApplyController.class); 
	@Autowired
	private CustApplyService custApplyService;
	@Autowired
	private DataSourceTransactionManager txManager;
	@Autowired
	private CustBaseService custBaseService;
	
	@ResponseBody
	@RequestMapping(value = "/queryDsh", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户-待审核信息")
	public String queryDsh(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		log.info("接收的JSON:"+map.toString());
		try {
			User user = this.getSessionUser(request);
			//map.put("cust_grp_jl", user.getId());
			isPage(map);
			map.put("USER_ID_SQ", user.getUser_id());
			map.put("STAT", "0");
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
			String newDate = format.format(new Date());
			int date=0;
			date=Integer.valueOf(newDate)-3;
			map.put("newDate", date);
			List<Map<String, Object>> resultList = custApplyService.query(map);
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("CUST_TYPE", "CUST_TYPE");
			dicMap.put("JL_TYPE", "JL_TYPE");
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/querYsq", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户-已申请授权信息")
	public String queryYsq(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		log.info("接收的JSON:"+map.toString());
		try {
			User user = this.getSessionUser(request);
			//map.put("cust_grp_jl", user.getId());
			isPage(map);
			map.put("USER_ID", user.getUser_id());
			map.put("STAT", "0");
			
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
			String newDate = format.format(new Date());
			int date=0;
			date=Integer.valueOf(newDate)-3;
			map.put("newDate", date);
			List<Map<String, Object>> resultList = custApplyService.query(map);
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("CUST_TYPE", "CUST_TYPE");
			dicMap.put("JL_TYPE", "JL_TYPE");
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);

		} catch (Exception e) {	
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户-可查看信息")
	public String query(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		log.info("接收的JSON:"+map.toString());
		try {
			User user = this.getSessionUser(request);
			//map.put("cust_grp_jl", user.getId());
			isPage(map);
			map.put("USER_ID", user.getUser_id());
			map.put("noSTAT", "0");
			
			
			
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
			String newDate = format.format(new Date());
			int date=0;
			date=Integer.valueOf(newDate)-3;
			
			
			
			map.put("newDate", date);
			List<Map<String, Object>> resultList = custApplyService.query(map);
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("CUST_TYPE", "CUST_TYPE");
			dicMap.put("JL_TYPE", "JL_TYPE");
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);

		} catch (Exception e) {	
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "维护客户-授权信息")
	public String save(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			
				map.put("USER_ID", this.getSessionUser(request).getUser_id());
				List<Map<String,Object>> isMap = custApplyService.queryOne(map);
				if(null!=isMap && !isMap.isEmpty()){
					this.setErrCode("01",page, "正在申请中");
				}else{
					map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					custApplyService.save(map);
					txManager.commit(status);
					this.setSuccess(null, page);
				}
				
			//User user = this.getSessionUser(request);	

		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/update", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	public String update(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			//User user = this.getSessionUser(request);
			map.put("USER_ID_SQ", this.getSessionUser(request).getUser_id());
			custApplyService.update(map);
			txManager.commit(status);
			this.setSuccess(null, page);

		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/deleteGq", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "删除客户-过期信息")
	public String deleteGq(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			//User user = this.getSessionUser(request);
			

			SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
			String newDate = format.format(new Date());
			int date=0;
			date=Integer.valueOf(newDate)-3;
			
			
			
			map.put("newDate", date);
			custApplyService.deleteGq(map);
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
	@SystemControllerLog(description = "删除客户-授权信息")
	public String deleteCustZg(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			//User user = this.getSessionUser(request);
			custApplyService.deleteByPk(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
}


