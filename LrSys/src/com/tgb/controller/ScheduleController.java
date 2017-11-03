package com.tgb.controller;

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
import com.tgb.aop.SystemServiceLog;
import com.tgb.controller.base.BaseController;
import com.tgb.model.User;
import com.tgb.service.cust.CustBaseService;
import com.tgb.service.cust.ScheduleService;
import com.tgb.util.DateTools;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/schedule")
public class ScheduleController extends BaseController {
	Logger log = Logger.getLogger(ScheduleController.class);
	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private DataSourceTransactionManager txManager;
	@Autowired
	private CustBaseService custBaseService;

	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemServiceLog(description = "查询客户-日程计划")
	public String query(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			User user = this.getSessionUser(request);
			 map.put("USER_ID", user.getUser_id());
			isPage(map);
			List<Map<String, Object>> resultList = scheduleService.query(map);
			this.setSuccess(resultList, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/queryNum", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemServiceLog(description = "查询客户-日程计划")
	public String queryNum(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			Object date = map.get("DATE");
			if(null!=date && !"".equals(date.toString())){
				User user = this.getSessionUser(request);
				// map.put("cust_grp_jl", user.getId());
				isPage(map);
				map.put("USER_ID", user.getUser_id());
				List<Map<String, Object>> resultList = scheduleService.queryNum(map);
				
				this.setSuccess(resultList, page);
			}else{
				this.setErrCode("01", page, "参数错误");
			}
			

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemServiceLog(description = "新增客户-日程计划")
	public String save(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		User user = this.getSessionUser(request);
		try {
			// User user = this.getSessionUser(request);
			Object SCHEDULE_ID=map.get("SCHEDULE_ID");
			if(null!=SCHEDULE_ID &&!"".equals(SCHEDULE_ID.toString())){
				List<Map<String, Object>> resultList = scheduleService.queryByPk(map);
				if (null != resultList && resultList.size() > 0) {
					map.put("MTN_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					scheduleService.update(map);
					/*map.put("CUSTBASEMTN_DATE",
							DateTools.getCurrentSysData("yyyyMMdd"));
					custBaseService.updCustBaseMtnData(map);*/
				} else {
					map.put("USER_ID", user.getUser_id());
					 map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
					 map.put("SCHEDULE_ID", DateTools.getCurrentSysData("yyyyMMddHHmmsss")+(int)((Math.random()*9+1)*10000));
					scheduleService.save(map);
					/*map.put("CUSTBASEMTN_DATE",
							DateTools.getCurrentSysData("yyyyMMdd"));
					custBaseService.updCustBaseMtnData(map);*/
				}
			}else{
				
				 map.put("USER_ID", user.getUser_id());
				 map.put("CRT_DATE", DateTools.getCurrentSysData("yyyyMMdd"));
				 map.put("SCHEDULE_ID", DateTools.getCurrentSysData("yyyyMMddHHmmsss")+(int)((Math.random()*9+1)*10000));
				scheduleService.save(map);
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
			scheduleService.update(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
    @SystemServiceLog(description = "删除客户-日程计划")
	public String deleteCustZg(@RequestBody Map<String, Object> map,
			HttpServletRequest request) {
		log.info("接收的JSON:" + map.toString());
		try {
			// User user = this.getSessionUser(request);
			scheduleService.deleteByPk(map);
			this.setSuccess(null, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
}
