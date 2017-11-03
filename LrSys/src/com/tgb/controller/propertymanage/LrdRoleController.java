package com.tgb.controller.propertymanage;

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
import com.tgb.service.propertymanage.LrdOrgService;
import com.tgb.service.propertymanage.LrdRoleService;
import com.tgb.util.DicReplace;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/lrdrole")
public class LrdRoleController extends BaseController  {

	Logger log = Logger.getLogger(LrdRoleController.class);
	@Autowired
	private LrdRoleService lrdRoleService;
	@Autowired
	private LrdOrgService lrdOrgService;
	@Autowired
	private DataSourceTransactionManager txManager;

	@ResponseBody
	@RequestMapping(value = "/querylist", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询角色列表")
	public String queryAllList(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			User user = (User) request.getSession().getAttribute("user");

			map.put("USER_ID", user.getUser_id());
			Map<String, Object> userOrg = lrdOrgService.queryUserOrg(map);
			String orgCD = userOrg.get("ORG_CD").toString();
			map.put("ORG_CD", orgCD);
			isPage(map);
			List<Map<String, Object>> result = lrdRoleService.query(map);
			if (null != page) {
				this.page.removeContext();
			}
			Map<String, String> dicMap = new HashMap<String, String>();
			dicMap.put("ROLE_FW", "SYS_ORG_YYFW");

			DicReplace.replaceDic(result, dicMap);
			this.setSuccess(result, page);

		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/query", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "查询角色")
	public String query(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			Map<String, Object> resultmap=new HashMap<String, Object>();
			List<Map<String, Object>> result = lrdRoleService.query(map);
			List<Map<String, Object>> resultFunc = lrdRoleService.queryFunc(map);
			resultmap.put("result", result);
			resultmap.put("resultFunc", resultFunc);
			this.setSuccess(resultmap, page);
			
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryinsert", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "添加角色--查询")
	public String queryInsert(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			List<Map<String, Object>> resultList = lrdRoleService.query(map);
			this.setSuccess(resultList, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/insert", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "添加角色--添加")
	public String Insert(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
			map.put("CRT_DATE", format.format(new Date()));
			map.put("MTN_DATE", format.format(new Date()));
			List<String> funcids=  (List<String>) map.get("FUNC");
			String ROlE_CD = map.get("ROLE_CD").toString();
			HashMap<String,Object> hashMap = new HashMap<String, Object>();
			hashMap.put("ROLE_CD", ROlE_CD);
			hashMap.put("CORP_CD", "907");
			//获取当前登录用户的机构
			User user = (User) request.getSession().getAttribute("user");
			map.put("USER_ID", user.getUser_id());
			Map<String, Object> userOrg = lrdOrgService.queryUserOrg(map);
			String orgCD = userOrg.get("ORG_CD").toString();
			for (String funcid : funcids) {
				hashMap.put("FUNC_CD", funcid);
				lrdRoleService.updateFunc(hashMap);
			}
			map.put("ROLE_ORG", orgCD);
			lrdRoleService.save(map);
			this.setSuccess(null, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/queryupdate", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "修改角色--查询")
	public String queryUpdate(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			Map<String, Object> resultmap=new HashMap<String, Object>();
			List<Map<String, Object>> result = lrdRoleService.query(map);
			List<Map<String, Object>> resultFunc = lrdRoleService.queryFunc(map);
			resultmap.put("result", result);
			resultmap.put("resultFunc", resultFunc);
			this.setSuccess(resultmap, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/update", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "添加角色--修改")
	public String update(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		TransactionStatus status = this.getTransactionStatus(txManager);
		try {
			
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
			map.put("MTN_DATE", format.format(new Date()));
			List<String> funcids= (List<String>) map.get("FUNC");
			String ROlE_CD = map.get("ROLE_CD").toString();
			HashMap<String,Object> hashMap = new HashMap<String, Object>();
			hashMap.put("ROLE_CD", ROlE_CD);
			hashMap.put("CORP_CD", "907");
			lrdRoleService.deleteFunc(hashMap);
			for (String funcid : funcids) {
				hashMap.put("FUNC_CD", funcid);
				lrdRoleService.updateFunc(hashMap);
			}
			boolean update = lrdRoleService.update(map);
			txManager.commit(status);
			this.setSuccess(update, page);
			
		} catch (Exception e) {
			txManager.rollback(status);
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@SystemControllerLog(description = "删除角色")
	public String delete(HttpServletRequest request,@RequestBody Map<String, Object> map) {
		log.info("接收的JSON:" + map.toString());
		try {
			boolean delete = lrdRoleService.delete(map);
			this.setSuccess(delete, page);
		} catch (Exception e) {
			this.setErr(page, e.getMessage());
		}
		return this.js.toString();
	}
	
}
