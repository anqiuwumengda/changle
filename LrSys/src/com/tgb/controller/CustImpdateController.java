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
import com.tgb.service.cust.CustImpdateService;
import com.tgb.service.propertymanage.LrdOrgService;
import com.tgb.util.DateTools;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/custimpdate")
public class CustImpdateController extends BaseController {
	Logger log = Logger.getLogger(CustFeedbackController.class);
	
	@Autowired
	private LrdOrgService lrdOrgService;
	@Autowired
	private CustImpdateService custImpdateService;
	@Autowired
	private DataSourceTransactionManager txManager;
	@Autowired
	private CustBaseService custBaseService;

	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询客户纪念日提醒")
	public String query(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			User user = this.getSessionUser(request);
			// map.put("cust_grp_jl", user.getId());
			isPage(map);
			List<Map<String, Object>> resultList = custImpdateService
					.query(map);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/querytx", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "首页查询客户纪念日提醒")
	public String queryTx(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("接收的JSON:" + map.toString());
		try {
			//List<Map<String, Object>> resList2 = new ArrayList<Map<String, Object>>();

			User user = this.getSessionUser(request);
			// map.put("cust_grp_jl", user.getId());
			isPage(map);

			map.put("CUST_GRP_JL", user.getUser_id());
			List<Map<String, Object>> custList = custImpdateService.queryTx(map);
	/*		for (Map<String, Object> tmpMap : custList) {
				tmpMap.put("WARN_FLAG", "0");
				List<Map<String, Object>> resultList = custImpdateService
						.queryTx(tmpMap);
				String currYmd = DateTools.getCurrentSysData("yyyyMMdd");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Date currDate = sdf.parse(currYmd);
				Object name= tmpMap.get("CUST_NAME");
				String type = tmpMap.get("CUST_TYPE").toString();
				Object tel = "";
				if("XWQY".equals(type)){
					 tel = tmpMap.get("TEL_NO");
				}else{
					 tel = tmpMap.get("LXR_TEL");
				}
				
				//Calendar can1 = Calendar.getInstance();
				//can1.setTime(currDate);// 当前日期

				
				 * Calendar can2 = Calendar.getInstance(); can2.setTime(date2);
				 
				for (Map<String, Object> resMap : resultList) {
					String warnType = resMap.get("WARN_TYPE").toString();
					Object tyepaa = resMap.get("JNR_TYPE");
					if(null!=tyepaa &&  !"".equals(tyepaa)){
						int num = Integer.parseInt(warnType.toString());
						if ("1".equals(tyepaa)) {// 提醒一次
							String date = resMap.get("DATE_YM").toString()
									.replace("/", "");
							Date date2 = sdf.parse(date);
							//Calendar can2 = Calendar.getInstance();
							//can2.setTime(date2);// 实际日期
							long day =  (date2.getTime()-currDate.getTime()  )
									/ (24 * 60 * 60 * 1000);
							if (day > 0 && (int)day <= num) {
								resMap.put("CUST_NAME", name);
								resMap.put("TEL_NO", tel);
								resList2.add(resMap);
							}

						} else {// 重复提醒
							String temp = resMap.get("DATE_YM").toString()
							.replace("/", "");
							String date = DateTools.getCurrentSysData("yyyy")+temp.substring(4,temp.length());
							Date date2 = sdf.parse(date);
							//Calendar can2 = Calendar.getInstance();
							//can2.setTime(date2);// 实际日期
							long day =  (date2.getTime()-currDate.getTime())
									/ (24 * 60 * 60 * 1000);
							if (day > 0 && (int)day <= num) {
								resMap.put("CUST_NAME", name);
								resMap.put("TEL_NO", tel);
								resList2.add(resMap);
							}
						}
					}
					
					
					

				}
			}*/

			this.setSuccess(custList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "保存客户纪念日提醒")
	public String save(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			// User user = this.getSessionUser(request);
			Object seqNo = map.get("SEQ_NO");
			if (null != seqNo && !"".equals(seqNo)) {
				List<Map<String, Object>> resultList = custImpdateService
						.query(map);
				if (null != resultList && resultList.size() > 0) {
					map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					custImpdateService.update(map);
					map.put("CUSTBASEMTN_DATE",
							DateTools.getCurrentSysData("yyyyMMdd"));
					custBaseService.updCustBaseMtnData(map);
				} else {
					// map.put("CRT_DATE",
					// DateTools.getCurrentSysData("yyyyMMdd"));
					custImpdateService.save(map);
					map.put("CUSTBASEMTN_DATE",
							DateTools.getCurrentSysData("yyyyMMdd"));
					custBaseService.updCustBaseMtnData(map);
				}
			} else {
				custImpdateService.save(map);
				map.put("CUSTBASEMTN_DATE",
						DateTools.getCurrentSysData("yyyyMMdd"));
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
	public String update(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			// User user = this.getSessionUser(request);
			custImpdateService.update(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "删除客户纪念日提醒")
	public String deleteCustZg(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			// User user = this.getSessionUser(request);
			custImpdateService.deleteByPk(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/saveMore", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "保存客户纪念日批量提醒")
	public String saveMore(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			// User user = this.getSessionUser(request);
			//多个客户id
			String[] custIds = map.get("CUST_IDS").toString().split(",");
			
			Object seqNo = map.get("SEQ_NO");
			if (null != seqNo && !"".equals(seqNo)) {
				List<Map<String, Object>> resultList = custImpdateService
						.query(map);
				if (null != resultList && resultList.size() > 0) {
					for (String CUST_ID : custIds) {
						map.remove("CUST_ID");
						map.put("CUST_ID", CUST_ID);
						map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
						custImpdateService.update(map);
						map.put("CUSTBASEMTN_DATE",
								DateTools.getCurrentSysData("yyyyMMdd"));
						custBaseService.updCustBaseMtnData(map);
					}
					
				} else {
					// map.put("CRT_DATE",
					// DateTools.getCurrentSysData("yyyyMMdd"));
					for (String CUST_ID : custIds) {
						map.remove("CUST_ID");
						map.put("CUST_ID", CUST_ID);
						custImpdateService.save(map);
						map.put("CUSTBASEMTN_DATE",
							DateTools.getCurrentSysData("yyyyMMdd"));
					custBaseService.updCustBaseMtnData(map);
					}
				}
			} else {
				for (String CUST_ID : custIds) {
					map.remove("CUST_ID");
					map.put("CUST_ID", CUST_ID);
					custImpdateService.save(map);
					map.put("CUSTBASEMTN_DATE",
						DateTools.getCurrentSysData("yyyyMMdd"));
				custBaseService.updCustBaseMtnData(map);
				}
			}

			txManager.commit(status);
			this.setSuccess(null, page);

		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	

	public static void main(String[] args) {
		String mmdd = DateTools.getCurrentSysData("mmdd");
		String s = "12345678";
		System.out.println(s.substring(4, s.length()));
	}
}
