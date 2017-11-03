package com.tgb.controller.propertymanage;

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

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.sun.swing.internal.plaf.basic.resources.basic;
import com.tgb.aop.SystemControllerLog;
import com.tgb.controller.base.BaseController;
import com.tgb.model.User;
import com.tgb.service.propertymanage.LrdOrgService;
import com.tgb.service.propertymanage.LrdRoleService;
import com.tgb.util.DicReplace;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/lrdorg")
public class LrdOrgController extends BaseController {
	Logger log=Logger.getLogger(LrdOrgController.class);
	
	@Autowired
	private LrdOrgService lrdOrgService;
	@Autowired
	private LrdRoleService lrdRoleService;
	@Autowired
	private DataSourceTransactionManager txManager;
	
	
	
	@ResponseBody
	@RequestMapping(value = "/querybyrela", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "机构查询")
	public String queryMenu(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:"+map.toString());
		try {
			//登录人的机构
			User user = (User) request.getSession().getAttribute("user");
			String orgCD = user.getOrgCD();
			
			map.put("USER_ID", user.getUser_id());
			List<Map<String,Object>> userRole = lrdRoleService.queryUserRole(map);//权限
			for (Map<String, Object> map2 : userRole) {
				String roleFw = map2.get("ROLE_FW").toString();
				//本级
				if(roleFw.equals("00")){ 
					if(map.get("ROLE_FW")=="01"||map.get("ROLE_FW")=="02")continue;
					map.put("ROLE_FW","00");
				}else if(roleFw.equals("02")){ //本级及下级
					map.put("ROLE_FW","02");
				}else if(roleFw.equals("01")){//本级及所有下级
					if(map.get("ROLE_FW")=="02")continue;
					map.put("ROLE_FW","01");
				}
			}
			map.put("ROLE_CD", orgCD);
			map.put("ORG_CD", orgCD);
			List<Map<String, Object>> resultList = lrdOrgService.queryMenu(map);
			this.setSuccess(resultList, page);
		} catch (Exception e) {
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询")
	public String query(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		
		log.info("接收的JSON:"+map.toString());
		try {
			
			List<Map<String,Object>> resultList = lrdOrgService.query(map);
			this.setSuccess(resultList, page);
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("ORG_VFLAG", "SYS_ORG_VFLAG");
			dicMap.put("ORG_FLAG", "SYS_ORG_FLAG");
			DicReplace.replaceDic(resultList, dicMap);
			this.setSuccess(resultList, page);
		} catch (Exception e) {
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryInsert", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "机构添加--查询")
	public String queryInsert(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		
		log.info("接收的JSON:"+map.toString());
		try {
			
			List<Map<String,Object>> resultList = lrdOrgService.query(map);
			this.setSuccess(resultList, page);
			
		} catch (Exception e) {
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "机构添加--添加")
	public String save(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		
		log.info("接收的JSON:"+map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			
			lrdOrgService.deletepc(map);
			lrdOrgService.insertpc(map);
			
			lrdOrgService.save(map);
			txManager.commit(status);
			this.setSuccess(null, page);
			
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryupdate", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "机构修改--查询")
	public String queryUpdate(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		
		log.info("接收的JSON:"+map.toString());
		
		try {
			List<Map<String,Object>> resultList = lrdOrgService.query(map);
			this.setSuccess(resultList, page);
			
		} catch (Exception e) {
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/update", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "机构修改--修改")
	public String update(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		
		log.info("接收的JSON:"+map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			lrdOrgService.deletepc(map);
			lrdOrgService.insertpc(map);
			boolean update = lrdOrgService.update(map);
			txManager.commit(status);
			this.setSuccess(update, page);
			
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "机构删除")
	public String delete(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		
		log.info("接收的JSON:"+map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			lrdOrgService.deletepc(map);
			boolean delete = lrdOrgService.delete(map);
			txManager.commit(status);
			this.setSuccess(delete, page);
			
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(null, e.getMessage());
		}
		return this.js.toString();
	}
}
