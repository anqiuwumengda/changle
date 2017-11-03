package com.tgb.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.tgb.service.cust.CustZzService;
import com.tgb.service.propertymanage.LrdRoleService;
import com.tgb.util.DateTools;
import com.tgb.util.DicReplace;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/custzz")
public class CustZzController extends BaseController {
	Logger log=Logger.getLogger(CustZzController.class); 
	@Autowired
	private CustZzService custZzService;
	@Autowired
	private DataSourceTransactionManager txManager;
	@Autowired
	private CustBaseService custBaseService;
	@Autowired
	private LrdRoleService lrdRoleService;
	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户-种植信息")
	public String query(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String income = "";
			User user = this.getSessionUser(request);
			//map.put("cust_grp_jl", user.getId());
			Object year="";
			isPage(map);
			List<Map<String, Object>> resultList = custZzService.query(map);
			for (Map<String, Object> resMap :resultList) {
				year =resMap.get("BIZ_DATE" );
				if(null!=year && !"".equals(year)){
					String sdate = DateTools.getCurrentSysData("yyyy");
					int result = sdate.compareTo(year.toString());
					if (result > 0) {
						
						income = String.valueOf(Integer.parseInt(sdate)-Integer.parseInt(year.toString()));
					} else {
						income.valueOf(0).toString();
					}
					resMap.put("BIZ_YEARS", income);
				}
				
			}
			
			Map<String,String> dicMap = new HashMap<String,String>();
			dicMap.put("ZW_TYPE", "ZW_Type");
			dicMap.put("ZZ_MODEL", "ZZ_MODEL");
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "维护客户-种植信息")
	public String save(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			//User user = this.getSessionUser(request);
			List<Map<String, Object>> resultList= custZzService.query(map);
			if(null!=resultList && resultList.size()>0){
				map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				custZzService.update(map);
				map.put("CUSTBASEMTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				custBaseService.updCustBaseMtnData(map);
			}else{
				map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				custZzService.save(map);
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
			custZzService.update(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "删除客户-种植信息")
	public String deleteCustZg(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			//User user = this.getSessionUser(request);
			custZzService.deleteByPk(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/filterQuery", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "按照种植作物筛选")
	public String filterQuery(@RequestBody Map<String, Object> map,HttpServletRequest request){
		log.info("种植作物筛选接收的JSON:"+map.toString());
		try {
			User user = (User) request.getSession().getAttribute("user");
			map.put("USER_ID", user.getUser_id());
			List<Map<String, Object>> userRole = lrdRoleService.queryUserRole(map);//获取用户权限
			List<Map<String,Object>> roleList = user.getROLE_CD();//权限列表
			String fw="";
			
			for (Map<String, Object> map2 : roleList) {
				fw=map2.get("ROLE_FW")+"";
			}
			
			
			//寻找最大范围
			if (fw.contentEquals("01")) {//可查看所有---不对数据进行处理
				
				
			}else if (fw.contentEquals("02")) {//可查看本级及下级
				
				map.put("ZH_ID",user.getOrgCD());
				
			}else {//查询本级的
				map.put("ZH_ID",user.getOrgCD());
				map.put("JL_ID",user.getUser_id());
				
			}
			String selectItem = (String) map.get("selectItem");
			map.put("ZW_TYPE", selectItem);
			isPage(map);
			List<Map<String, Object>> resultList = custZzService.filterQuery(map);
			Map<String, String> dicMap = new HashMap<String, String>();
			if (null != page) {
				this.page.removeContext();
			}
			dicMap.put("ZZ_MODEL", "ZZ_MODEL");
			dicMap.put("ZW_TYPE", "ZW_TYPE");
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			this.setErr(page, e.getLocalizedMessage());
		}
		return this.js.toString();
	}
}
