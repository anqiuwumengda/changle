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
import com.tgb.service.cust.CustRelaService;
import com.tgb.util.DateTools;
import com.tgb.util.DicReplace;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/custrela")
public class CustRelaController extends BaseController {
	Logger log=Logger.getLogger(CustRelaController.class); 
	@Autowired
	private CustRelaService custRelaService;
	@Autowired
	private DataSourceTransactionManager txManager;
	@Autowired
	private CustBaseService custBaseService;
	
	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户关联信息")
	public String query(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			User user = this.getSessionUser(request);
			//map.put("cust_grp_jl", user.getId());
			isPage(map);
			List<Map<String, Object>> resultList = custRelaService.query(map);
			Map<String,String> dicMap = new HashMap<String,String>();
			dicMap.put("RELA_TYPE", "RELA_TYPE");
			dicMap.put("ID_TYPE", "ID_TYPE");
			dicMap.put("VOCATION", "OCCPUATION");
			dicMap.put("JH_FLAG", "JH_FLAG");
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "维护客户关联信息")
	public String save(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			//User user = this.getSessionUser(request);
			Object seqNo=map.get("SEQ_NO");
			if(null!=seqNo &&!"".equals(seqNo.toString())){
				List<Map<String,Object>> list = custRelaService.query(map);
				if(null!=list && list.size()>0){
					map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					custRelaService.update(map);
					map.put("CUSTBASEMTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					custBaseService.updCustBaseMtnData(map);
				}else{
					map.put("SEQ_NO", DateTools.getCurrentSysData("yyyyMMddHHmmsss")+(int)((Math.random()*9+1)*10000));
					map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					custRelaService.save(map);
					map.put("CUSTBASEMTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					custBaseService.updCustBaseMtnData(map);
				}
			}else{

				map.put("SEQ_NO", DateTools.getCurrentSysData("yyyyMMddHHmmsss")+(int)((Math.random()*9+1)*10000));
				map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				custRelaService.save(map);
				map.put("CUSTBASEMTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				custBaseService.updCustBaseMtnData(map);
			}
			txManager.commit(status);
			
			this.setSuccess(null, page);

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
		try {
			//User user = this.getSessionUser(request);
			custRelaService.update(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "删除客户关联信息")
	public String deleteCustZg(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			//User user = this.getSessionUser(request);
			custRelaService.deleteByPk(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
}
