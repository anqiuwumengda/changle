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
import com.tgb.service.cust.CustBaseService;
import com.tgb.service.cust.CustFeedbackService;
import com.tgb.service.cust.CustNzNeedService;
import com.tgb.service.propertymanage.LrdRoleService;
import com.tgb.util.DateTools;
import com.tgb.util.DicReplace;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/custfeedback")
public class CustFeedbackController extends BaseController {
	Logger log=Logger.getLogger(CustFeedbackController.class); 
	@Autowired
	private  CustFeedbackService  custFeedbackService;
	@Autowired
	private DataSourceTransactionManager txManager;
	@Autowired
	private CustBaseService custBaseService;
	@Autowired
	private CustNzNeedService custNzNeedService;
	@Autowired
	private LrdRoleService lrdRoleService;
	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户需求信息")
	public String query(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			User user = this.getSessionUser(request);
			//map.put("cust_grp_jl", user.getId());
			isPage(map);
			List<Map<String, Object>> resultList = custFeedbackService.query(map);
			Map<String,String> dicMap = new HashMap<String,String>();
			dicMap.put("KHMYD", "KHMYD");
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "保存客户需求信息")
	public String save(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		/*
			有三种方式：1直接点击保存
						2拜访
						3回访
		*/
		log.info("接收的JSON:"+map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		
		try {
			//农资用户所需农资
			insertNzNeed(map);
			
			User user = this.getSessionUser(request);
			String user_id = user.getUser_id();
			map.put("OPER_CD", user_id);
			//1.保存
			String baifang = (String) map.get("BAIFANG");
			//String huifang =  (String) map.get("HUIFANG");
			if(baifang.equals("0") ){//正常保存
				log.info("正常保存");
				map.put("STATE", "LR");//新增的，查询录入的记录
				List<Map<String, Object>> resultList= custFeedbackService.query(map);
				if(null!=resultList && resultList.size()>0){
					map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					custFeedbackService.update(map);
					map.put("CUSTBASEMTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					custBaseService.updCustBaseMtnData(map);
					//农资需求表操作
					
				}else{
					//map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					custFeedbackService.save(map);
					map.put("CUSTBASEMTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					custBaseService.updCustBaseMtnData(map);
				}
			}else if(baifang.equals("1") ){
				/* 1.查询出“录入”类数据并且更新
				 * 2.插入一条“拜访”类数据
				*/
				log.info("拜访");
				map.put("STATE", "LR");//新增的，查询录入的记录
				List<Map<String, Object>> resultList= custFeedbackService.query(map);
				if(null!=resultList && resultList.size()>0){
					//存在“录入”类数据，更新；
					
					map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					custFeedbackService.update(map);
					map.put("CUSTBASEMTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					custBaseService.updCustBaseMtnData(map);
					
				}else{
					//不存在“录入”类数据，插入一条“录入”类数据
					//map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					
					custFeedbackService.save(map);
					map.put("CUSTBASEMTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					custBaseService.updCustBaseMtnData(map);
					
				}
				//在cust_feedback表插入一条数据，类型为“拜访”
				//“拜访”记录的插入方式同“录入”，先判断,需要更新或者插入
				map.put("STATE", "BF");
				//map.remove("CRT_DATE");
				map.put("CRT_DATE", getCurrentDate());
				List<Map<String, Object>> resultBf= custFeedbackService.query(map);
				
				if(null!=resultBf && resultBf.size()>0){
					//存在“拜访”记录，更新cust_feedback即可
					//map.put("CRT_DATE", resultBf.get(0).get("CRT_DATE"));
					map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					custFeedbackService.update(map);
				}else{
					//不存在“拜访”记录，插入一条“拜访”记录
					map.put("ISHF", "0");//默认未回访
					map.put("CRT_DATE", getCurrentDate());
					map.put("STATE", "BF");
					custFeedbackService.save(map);
				}
				
				
			}else{
				log.info("非法操作");
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
			custFeedbackService.update(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "删除客户需求信息")
	public String deleteCustZg(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			//User user = this.getSessionUser(request);
			custFeedbackService.deleteByPk(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/huifang", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "保存回访信息")
	public String huifang(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		/*1.拜访记录的状态置为“已回访”
		 * 2.生成回访记录：1：若已经存在，更新当前回访记录
		 * 					2：不存在，插入一条新的回访记录
		
		*/
		log.info("接收的JSON:"+map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			User user = this.getSessionUser(request);
			String user_id = user.getUser_id();
			//查找对应的拜访记录
			map.put("STATE", "BF");
			List<Map<String, Object>> resultListBf= custFeedbackService.query(map);
			if(resultListBf != null && resultListBf.size() > 0){
				Map<String, Object> mapBf = new HashMap<String, Object>();
				mapBf.put("ISHF", "1");
				mapBf.put("CUST_ID", map.get("CUST_ID"));
				mapBf.put("CRT_DATE", map.get("CRT_DATE"));
				mapBf.put("STATE", "BF");
				//标记为“已回访”
				custFeedbackService.update(mapBf);
			}
			//查找是否存在对应已回访的记录
			map.put("STATE", "HF");
			Map<String, Object> mapHf = new HashMap<String, Object>();
			mapHf.put("CRT_DATE", map.get("CRT_DATE"));
			mapHf.put("CUST_ID", map.get("CUST_ID"));
			mapHf.put("STATE", "HF");
			List<Map<String, Object>> resultListHf= custFeedbackService.query(mapHf);
			if(resultListHf != null && resultListHf.size() > 0){
				//已经存在回访记录，更新
				map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				custFeedbackService.update(map);
			}else{
				//不存在回访记录，插入一条记录
				//待存储的数值为对应拜访记录的数值,
				Map<String, Object> paraMap = resultListBf.get(0);
				paraMap.put("CRT_DATE", map.get("CRT_DATE"));
				paraMap.put("STATE", "HF");
				paraMap.put("HFRESULT", map.get("HFRESULT"));
				paraMap.put("HUIFANGBEIZHU", map.get("HUIFANGBEIZHU"));
				paraMap.put("OPER_CD", user_id);
				paraMap.put("HF_DATE", getCurrentDate());
				custFeedbackService.save(paraMap);
			}
			txManager.commit(status);
			this.setSuccess(null, page);
		} catch (Exception e) {
			// TODO: handle exception
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/getHfInfo", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查找回访信息")
	public String getHfInfo(@RequestBody Map<String, Object> map,HttpServletRequest request){
		log.info("查找回访信息接收的JSON:"+map.toString());
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
			isPage(map);
			List<Map<String, Object>> resultList = custFeedbackService.queryList(map);
			Map<String, String> dicMap = new HashMap<String, String>();
			if (null != page) {
				this.page.removeContext();
			}
			dicMap.put("KHMYD", "KHMYD");
			dicMap.put("HFRESULT", "HF_RESULT");
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			this.setErr(page, e.getLocalizedMessage());
		}
		return this.js.toString();
	}
	public String getCurrentDate(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		//System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		return df.format(new Date());
	}
	@SuppressWarnings("unchecked")
	public void insertNzNeed(Map<String,Object> map){
		String cust_id = (String) map.get("CUST_ID");
		 List<Object> list =  (List<Object>) map.get("nongziDetails");
		 //首先判断当前日期客户是否存在记录
		 Map<String,Object> param = new HashMap<String, Object>();
		 param.put("CUST_ID", cust_id);
		 param.put("CRT_DATE", getCurrentDate());
		 param.put("DIC_PARENTID", "NZ_TYPE");
		 List<Map<String, Object>> resultList = custNzNeedService.query(param);
		 if(resultList != null && resultList.size() > 0){
			 //更新，删除当前的，重新插入新的
			 custNzNeedService.delete(param);
		 }
		if(list != null && !list.isEmpty()){
			int length = list.size();
			for(int i = 0;i < length; i ++){
				Map<String, Object> obj =  (Map<String, Object>) list.get(i);
				String dic_id = (String) obj.get("DIC_ID");
				int num = 0 ;
				if(obj.get("num") != null){
					num = (Integer) obj.get("num");
				}
				int month = 0;
				if(obj.get("month") != null){
					month = (Integer) obj.get("month");
				}
				if(num == 0 && month == 0){
					
				}else{
					obj.put("CUST_ID", cust_id);
					obj.put("CRT_DATE", getCurrentDate());
					obj.put("NZ_TYPE", dic_id);
					obj.put("NUM", num+"");
					obj.put("MONTH", month+"");
					custNzNeedService.save(obj);
				}
				
		}
	}
}
}
