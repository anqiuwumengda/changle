package com.tgb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

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
import com.tgb.service.cust.CustDgService;
import com.tgb.service.cust.CustJsService;
import com.tgb.service.cust.CustYzService;
import com.tgb.service.cust.CustZgService;
import com.tgb.service.cust.CustZzService;
import com.tgb.service.cust.RptDtlService;
import com.tgb.util.DateTools;
import com.tgb.util.DicReplace;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/rptdtl")
public class RptDtlController extends BaseController {
	Logger log=Logger.getLogger(RptDtlController.class); 
	@Autowired
	private RptDtlService rptDtlService;
	@Autowired
	private DataSourceTransactionManager txManager;
	@Autowired
	private CustBaseService custBaseService;
	@Autowired
	private CustZgService custZgService;
	@Autowired
	private CustZzService custZzService;
	@Autowired
	private CustYzService custYzService;
	@Autowired
	private CustDgService custDgService;
	@Autowired
	private CustJsService custJsService;
	
	
	
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户-财报明细信息")
	public String query(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		
		try {
			User user = this.getSessionUser(request);
			//map.put("cust_grp_jl", user.getId());
			isPage(map);
			List<Map<String, Object>> resultList = rptDtlService.query(map);
			Object zbdm = map.get("SUBJ_CD");
			if(null!=zbdm && !"".equals(zbdm)){
				if("A0005".equals(zbdm.toString())){
					Map<String, String> dicMap = new HashMap<String, String>();
					dicMap.put("DTL_DESC", "FIN_ITEM");
					DicReplace.replaceDic(resultList, dicMap);
				}
			}
			
			
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "维护客户-财报明细信息")
	public String save(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			//User user = this.getSessionUser(request);
			//List<Map<String, Object>> resultList= rptDtlService.query(map);
			if(map.get("RPT_DTL_ID")==""||map.get("RPT_DTL_ID")==null){
				//添加
				map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				String RPT_DTL_ID=UUID.randomUUID().toString().replaceAll("-", "");
				map.put("RPT_DTL_ID", RPT_DTL_ID);
				rptDtlService.save(map);
				map.put("CUSTBASEMTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				custBaseService.updCustBaseMtnData(map);
			}else{
				map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				rptDtlService.update(map);
				map.put("CUSTBASEMTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				custBaseService.updCustBaseMtnData(map);
			}
//			if(null!=resultList && resultList.size()>0){
//				map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
//				rptDtlService.update(map);
////				map.put("CUSTBASEMTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
////				custBaseService.updCustBaseMtnData(map);
//			}else{
//				map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
//				String RPT_DTL_ID=UUID.randomUUID().toString().replaceAll("-", "");
//				map.put("RPT_DTL_ID", RPT_DTL_ID);
//				rptDtlService.save(map);
////				map.put("CUSTBASEMTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
////				custBaseService.updCustBaseMtnData(map);
//				
//			}
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
	@SystemControllerLog(description = "修改客户-财报明细信息")
	public String update(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			//User user = this.getSessionUser(request);
			rptDtlService.update(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "删除客户-财报明细信息")
	public String deleteCustZg(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			//User user = this.getSessionUser(request);
			rptDtlService.delete(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/querySR1Y", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "删除客户-财报明细信息")
	public String querySR1Y(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		log.info("接收的JSON:"+map.toString());
		try {
			double SR_1Y=0.0;
			if("XWQY".equals(map.get("CUST_TYPE"))){
				
				Map<String, Object> allsr=rptDtlService.queryXVQYSR1Y(map);
				Set<Entry<String,Object>> entrySet = allsr.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					double minnum=0.0;
					try {
						minnum = Double.valueOf(entry.getValue() + "");
					} catch (Exception e) {}
					SR_1Y=SR_1Y+minnum;
				}
				
			}else{
				//Map<String, Object> allsr=rptDtlService.queryQtSR1Y(map);
				
				List<Map<String, Object>> zg = custZgService.query(map);
				 List<Map<String, Object>> zz = custZzService.query(map);
				List<Map<String, Object>> yz = custYzService.query(map);
				List<Map<String, Object>> dg = custDgService.query(map);
				List<Map<String, Object>> Js = custJsService.query(map);
				
				for (Map<String, Object> map2 : zg) {
					double minnum=0.0;
					try {
						minnum=Double.valueOf(map2.get("YEARAMT")+"");
					} catch (Exception e) {}
					SR_1Y=SR_1Y+minnum;
				}
				
				
				for (Map<String, Object> map2 : zz) {
					double minnum=0.0;
					try {
						minnum=Double.valueOf(map2.get("INCOME_Y")+"");
					} catch (Exception e) {}
					SR_1Y=SR_1Y+minnum;			
				}
				
				
				for (Map<String, Object> map2 : yz) {
					double minnum=0.0;
					try {
						minnum=Double.valueOf(map2.get("INCOME_Y")+"");
					} catch (Exception e) {}
					SR_1Y=SR_1Y+minnum;
				}
				
				
				for (Map<String, Object> map2 : dg) {
					
					double minnum=0.0;
					try {
						minnum=Double.valueOf(map2.get("INCOME_Y")+"");
					} catch (Exception e) {}
					SR_1Y=SR_1Y+minnum;
				}
				
				
				for (Map<String, Object> map2 : Js) {
					double minnum=0.0;
					try {
						minnum=Double.valueOf(map2.get("INCOME_Y")+"");
					} catch (Exception e) {}
					SR_1Y=SR_1Y+minnum;
				}
				
				Map<String, Object> allsr=rptDtlService.queryXVQYSR1Y(map);
				Set<Entry<String,Object>> entrySet = allsr.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					double minnum=0.0;
					try {
						minnum = Double.valueOf(entry.getValue() + "");
					} catch (Exception e) {}
					SR_1Y=SR_1Y+minnum;
				}
				
		
				
				
				/*Set<Entry<String,Object>> entrySet = allsr.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					double minnum=0.0;
					try {
						minnum = Double.valueOf(entry.getValue() + "");
					} catch (Exception e) {}
					SR_1Y=SR_1Y+minnum;
				}*/
			}
			List<Map<String, Object>> result=new ArrayList<Map<String,Object>>();
			Map<String, Object> num=new HashMap<String, Object>();
			num.put("SR_1Y", SR_1Y);
			result.add(num);
			this.setSuccess(result, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString());;
	}
}
